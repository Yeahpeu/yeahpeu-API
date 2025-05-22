package com.yeahpeu.event.service;

import static com.yeahpeu.common.exception.ExceptionCode.INVALID_AMOUNT;
import static com.yeahpeu.common.exception.ExceptionCode.NOT_FOUND_EVENT_ID;
import static com.yeahpeu.common.exception.ExceptionCode.NOT_FOUND_MAIN_CATEGORY_ID;
import static com.yeahpeu.common.exception.ExceptionCode.NOT_FOUND_SUBCATEGORY_ID;
import static com.yeahpeu.common.exception.ExceptionCode.NOT_FOUND_WEDDING_ID;
import static com.yeahpeu.common.exception.ExceptionCode.NOT_MATCHED_CATEGORY;
import static java.lang.Long.compare;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import com.yeahpeu.budget.domain.BudgetEntity;
import com.yeahpeu.budget.domain.CategoryExpenseEntity;
import com.yeahpeu.budget.repository.BudgetRepository;
import com.yeahpeu.budget.repository.CategoryExpenseRepository;
import com.yeahpeu.category.domain.CategoryEntity;
import com.yeahpeu.category.repository.CategoryRepository;
import com.yeahpeu.common.exception.BadRequestException;
import com.yeahpeu.common.exception.ExceptionCode;
import com.yeahpeu.common.exception.NotFoundException;
import com.yeahpeu.common.validation.AmountValidator;
import com.yeahpeu.event.controller.condition.DateBasedCondition;
import com.yeahpeu.event.controller.condition.EventSearchCondition;
import com.yeahpeu.event.domain.EventEntity;
import com.yeahpeu.event.repository.EventRepository;
import com.yeahpeu.event.service.command.EventCreateCommand;
import com.yeahpeu.event.service.command.UpdateStatusCommand;
import com.yeahpeu.event.service.dto.CategoryCountDto;
import com.yeahpeu.event.service.dto.EventDetailDto;
import com.yeahpeu.event.service.dto.EventDto;
import com.yeahpeu.event.service.dto.EventStatusDto;
import com.yeahpeu.event.service.dto.EventSummaryDto;
import com.yeahpeu.event.service.dto.ProgressDto;
import com.yeahpeu.event.util.DateUtils;
import com.yeahpeu.task.service.dto.TaskDto;
import com.yeahpeu.user.repository.UserRepository;
import com.yeahpeu.wedding.domain.WeddingEntity;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryExpenseRepository categoryExpenseRepository;
    private final BudgetRepository budgetRepository;

    @Transactional
    @Override
    public EventDto save(EventCreateCommand eventCreateCommand) {
        WeddingEntity wedding = findWeddingByUserId(eventCreateCommand.getUserId());

        Validated categories = getValidatedCategory(eventCreateCommand);

        EventEntity event = EventEntity.of(wedding, eventCreateCommand, categories.mainCategory,
                categories.subcategory);

        return EventDto.from(eventRepository.save(event));
    }

    @Transactional
    @Override
    public EventDto updateEvent(Long eventId, EventCreateCommand eventCreateCommand) {
        EventEntity event = findEventById(eventId);
        BudgetEntity budget = findBudgetByEvent(event);
        CategoryExpenseEntity categoryExpense = findCategoryExpenseByEvent(event);

        int beforePrice = event.getPrice();

        Validated categories = getValidatedCategory(eventCreateCommand);

        event.updateDetails(eventCreateCommand, categories.mainCategory(), categories.subcategory());

        if (event.isCompleted() && beforePrice != event.getPrice()) {
            subtractExpense(budget, categoryExpense, beforePrice);
            addExpense(budget, categoryExpense, event.getPrice());
        }

        return EventDto.from(event);
    }

    @Override
    public void deleteEvent(Long eventId) {
        EventEntity event = findEventById(eventId);
        BudgetEntity budget = findBudgetByEvent(event);
        CategoryExpenseEntity categoryExpense = findCategoryExpenseByEvent(event);

        if (event.getPrice() > 0 && event.isCompleted()) {
            subtractExpense(budget, categoryExpense, event.getPrice());
        }

        eventRepository.delete(event);
    }

    @Transactional
    @Override
    public EventStatusDto updateEventStatus(UpdateStatusCommand updateStatusCommand) {
        EventEntity event = findEventById(updateStatusCommand.getEventId());
        BudgetEntity budget = findBudgetByEvent(event);
        CategoryExpenseEntity categoryExpense = findCategoryExpenseByEvent(event);

        boolean beforeStatus = event.isCompleted();

        event.updateStatus(updateStatusCommand.isCompleted());

        if (beforeStatus != event.isCompleted() && event.getPrice() > 0) {
            if (event.isCompleted()) {
                addExpense(budget, categoryExpense, event.getPrice());
            } else {
                subtractExpense(budget, categoryExpense, event.getPrice());
            }
        }

        return EventStatusDto.from(event);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventDto> getEventsBySubCategory(Long userId, Long subcategoryId) {
        WeddingEntity wedding = findWeddingByUserId(userId);

        List<EventEntity> events = eventRepository.findByWeddingIdAndSubCategoryId(wedding.getId(), subcategoryId);

        return events.stream()
                .sorted(comparing(EventEntity::getDate))
                .map(EventDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventDto> getEventsByDateRange(@ModelAttribute EventSearchCondition condition, Long userId) {
        condition.validateSize();

        WeddingEntity wedding = findWeddingByUserId(userId);

        List<EventEntity> events = eventRepository.searchEventByCondition(wedding, condition);

        return events.stream()
                .sorted(comparing(EventEntity::getDate))
                .map(EventDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public EventDetailDto getEvent(Long eventId) {
        EventEntity event = findEventById(eventId);

        List<TaskDto> taskDtos = event.getTaskList()
                .stream()
                .map(TaskDto::from)
                .toList();

        return EventDetailDto.from(event, taskDtos);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventSummaryDto> getEventsPreview(Long userId, DateBasedCondition condition) {
        WeddingEntity wedding = findWeddingByUserId(userId);

        List<EventEntity> events = eventRepository.searchEventByDates(wedding, condition);

        // 날짜별 그룹화 및 카테고리 등장 횟수 계산
        Map<LocalDate, List<CategoryCountDto>> groupedByDate = events.stream()
                .collect(groupingBy(
                        event -> event.getDate()
                                .toInstant()
                                .atZone(ZoneId.of("Asia/Seoul"))
                                .toLocalDate(), // 날짜 기준 그룹화
                        collectingAndThen(
                                mapping(
                                        event -> event.getMainCategory().getId(), // 카테고리 ID 추출
                                        toList()
                                ),
                                this::countCategories // 카테고리 등장 횟수 계산
                        )
                ));

        return groupedByDate.entrySet()
                .stream()
                .map(EventSummaryDto::from)
                .sorted(comparing(EventSummaryDto::getDate))
                .collect(toList());
    }

    @Override
    public void validateDate(DateBasedCondition condition) {
        DateUtils.validateDate(condition);
    }

    @Transactional(readOnly = true)
    @Override
    public ProgressDto getProgressBar(Long userId) {
        WeddingEntity wedding = findWeddingByUserId(userId);

        int totalCount = eventRepository.countByWedding_Id(wedding.getId());
        int completedCount = eventRepository.countByWedding_IdAndCompleted(wedding.getId(), true);

        return ProgressDto.of(completedCount, totalCount);
    }

    private WeddingEntity findWeddingByUserId(Long userId) {
        return userRepository.findWeddingById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_WEDDING_ID));
    }

    private CategoryEntity findCategoryById(Long categoryId, ExceptionCode notFoundMessage) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(notFoundMessage));
    }

    private EventEntity findEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT_ID));
    }

    private List<CategoryCountDto> countCategories(List<Long> categoryIds) {
        return categoryIds.stream()
                .collect(groupingBy(
                        categoryId -> categoryId, // 카테고리 ID 기준으로 그룹화
                        counting()    // 각 카테고리 ID의 등장 횟수 계산
                ))
                .entrySet()
                .stream()
                .map(CategoryCountDto::from)
                .sorted((a, b) -> compare(b.getCount(), a.getCount())) // 등장 횟수 기준 내림차순 정렬
                .collect(toList());
    }

    private CategoryExpenseEntity findCategoryExpenseByEvent(EventEntity event) {
        return categoryExpenseRepository.findByCategoryIdAndWeddingId(
                        event.getSubCategory().getId(), event.getWedding().getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_SUBCATEGORY_ID));
    }

    private BudgetEntity findBudgetByEvent(EventEntity event) {
        return budgetRepository.findByWeddingId(event.getWedding().getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_WEDDING_ID));
    }

    private void subtractExpense(BudgetEntity budget, CategoryExpenseEntity categoryExpense, int price) {
        budget.subtractUsedBudget(price);
        categoryExpense.subtractExpense(price);
    }

    private void addExpense(BudgetEntity budget, CategoryExpenseEntity categoryExpense, int price) {
        categoryExpense.addExpense(price);
        budget.addUsedBudget(price);
    }

    // 카테고리 검증하기
    private Validated getValidatedCategory(EventCreateCommand eventCreateCommand) {
        CategoryEntity mainCategory = findCategoryById(eventCreateCommand.getMainCategoryId(),
                NOT_FOUND_MAIN_CATEGORY_ID);

        CategoryEntity subcategory = findCategoryById(eventCreateCommand.getSubcategoryId(),
                NOT_FOUND_SUBCATEGORY_ID);

        if (!subcategory.getParentCategory().getId().equals(mainCategory.getId())) {
            throw new BadRequestException(NOT_MATCHED_CATEGORY);
        }

        if (!AmountValidator.validate(eventCreateCommand.getPrice())) {
            throw new BadRequestException(INVALID_AMOUNT);
        }
        return new Validated(mainCategory, subcategory);
    }

    private record Validated(CategoryEntity mainCategory, CategoryEntity subcategory) {
    }
}
