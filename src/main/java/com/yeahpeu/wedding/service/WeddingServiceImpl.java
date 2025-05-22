package com.yeahpeu.wedding.service;

import static com.yeahpeu.common.exception.ExceptionCode.ALREADY_COUPLING;
import static com.yeahpeu.common.exception.ExceptionCode.ALREADY_JOINED;
import static com.yeahpeu.common.exception.ExceptionCode.ALREADY_ONBOARD;
import static com.yeahpeu.common.exception.ExceptionCode.INVALID_AMOUNT;
import static com.yeahpeu.common.exception.ExceptionCode.NOT_FOUND_USER_ID;
import static com.yeahpeu.common.exception.ExceptionCode.OCCUPIED_WEDDING_ROLE;
import static com.yeahpeu.common.exception.ExceptionCode.PARTNER_NOT_FOUND_MESSAGE;
import static com.yeahpeu.wedding.domain.WeddingRole.BRIDE;
import static com.yeahpeu.wedding.domain.WeddingRole.GROOM;

import com.yeahpeu.budget.domain.BudgetEntity;
import com.yeahpeu.budget.repository.BudgetRepository;
import com.yeahpeu.category.domain.CategoryEntity;
import com.yeahpeu.category.service.CategoryService;
import com.yeahpeu.common.exception.BadRequestException;
import com.yeahpeu.common.exception.NotFoundException;
import com.yeahpeu.common.validation.AmountValidator;
import com.yeahpeu.event.domain.EventEntity;
import com.yeahpeu.task.domain.TaskEntity;
import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.user.repository.UserRepository;
import com.yeahpeu.wedding.config.OnboardingEventProperties;
import com.yeahpeu.wedding.domain.Event;
import com.yeahpeu.wedding.domain.EventWrapper;
import com.yeahpeu.wedding.domain.WeddingEntity;
import com.yeahpeu.wedding.domain.WeddingRole;
import com.yeahpeu.wedding.repository.WeddingRepository;
import com.yeahpeu.wedding.service.command.JoinWeddingCommand;
import com.yeahpeu.wedding.service.command.OnboardingCommand;
import com.yeahpeu.wedding.service.command.WeddingCategorySaveCommand;
import com.yeahpeu.wedding.service.dto.BoardingCheckDto;
import com.yeahpeu.wishlist.domain.WishlistEntity;
import com.yeahpeu.wishlist.repository.WishlistRepository;
import jakarta.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class WeddingServiceImpl implements WeddingService {

    private final WeddingRepository weddingRepository;
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final WishlistRepository wishlistRepository;
    private final EntityManager entityManager;
    private final OnboardingEventProperties eventConfigProperties;

    @Transactional
    @Override
    public void processOnboarding(OnboardingCommand command) {

        // 비관적 락을 써서 사용자를 조회합니다.
        UserEntity user = userRepository.findUserByIdWithLock(command.getUserId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER_ID));

        // 현재 커맨드에
        // 웨딩 날짜,
        // 선택한 카테고리 (List<Long> cateforyIds 가 들어옵니다)
        if (user.getWedding() != null) {
            throw new BadRequestException(ALREADY_ONBOARD);
        }

        if (!AmountValidator.validate(command.getBudget())) {
            throw new BadRequestException(INVALID_AMOUNT);
        }
        // 웨딩 생성
        WeddingEntity wedding = new WeddingEntity();
        assignWeddingRole(command.getWeddingRole(), user, wedding);
        wedding.setWeddingDay(command.getWeddingDay());
        wedding.setIsOnboarded(true);
        WeddingEntity savedWedding = weddingRepository.save(wedding);

        // 위시리스트 생성
        WishlistEntity wishlist = new WishlistEntity();
        wishlist.setWedding(savedWedding);
        wishlist.setName("my wishlist");
        wishlistRepository.save(wishlist);

        // 예산 생성
        BudgetEntity budgetEntity = new BudgetEntity();
        budgetEntity.setWedding(wedding);
        budgetEntity.setTotalBudget(command.getBudget());
        budgetEntity.setUsedBudget(0L);
        budgetRepository.save(budgetEntity);

        // 유저에 웨딩 셋팅
        user.setWeddingRole(command.getWeddingRole());
        user.setWedding(savedWedding);

        // 카테고리 저장
        categoryService.saveWeddingCategories(
                WeddingCategorySaveCommand.from(savedWedding.getId(), command.getCategoryIds())
        );

        // 초기 이벤트 지정
        addInitialEvents(savedWedding, command.getCategoryIds());

    }

    private List<EventWrapper> filterEventsByCategories(List<Long> categoryIds) {
        return categoryIds.stream()  // categoryIds를 순회하면서
                .flatMap(categoryId -> Optional.ofNullable(
                                eventConfigProperties.getEvents().get(categoryId))  // categoryId에 해당하는 이벤트 리스트 가져오기
                        .map(List::stream)  // 이벤트 리스트를 스트림으로 변환
                        .orElseGet(Stream::empty))  // 해당하는 이벤트가 없으면 빈 스트림 반환
                .collect(Collectors.toList());
    }

    public void addInitialEvents(WeddingEntity wedding, List<Long> categoryIds) {
        ZonedDateTime weddingDay = wedding.getWeddingDay();
        ZonedDateTime today = ZonedDateTime.now();
        long totalDays = ChronoUnit.DAYS.between(today, weddingDay);

        // 90일 미만이면 초기 이벤트 추가하지 않음
        if (totalDays < 90) {
            return;
        }

        // 이벤트 카테고리 목록 필터링
        List<EventWrapper> onboardingEvents = filterEventsByCategories(categoryIds);

        for (EventWrapper wrapper : onboardingEvents) {
            Event onboardingEvent = wrapper.getEvent();

            long adjustedWeeks = calculateAdjustedWeeks(totalDays, onboardingEvent.getWeeksBefore());
            ZonedDateTime eventDate = weddingDay.minusWeeks(adjustedWeeks);

            // CategoryEntity를 EntityManager를 통해 직접 저장
            CategoryEntity subCategory = entityManager.getReference(CategoryEntity.class,
                    onboardingEvent.getSubCategoryId());
            CategoryEntity mainCategory = entityManager.getReference(CategoryEntity.class,
                    onboardingEvent.getMainCategoryId());

            // EventEntity 생성 및 저장
            EventEntity event = EventEntity.builder()
                    .wedding(wedding)
                    .title(onboardingEvent.getTitle())
                    .date(eventDate)
                    .location(onboardingEvent.getLocation())
                    .mainCategory(mainCategory)
                    .subCategory(subCategory)
                    .price(onboardingEvent.getPrice())
                    .build();

            // TaskEntity 생성 및 추가
            if (wrapper.getTasks() != null) {
                for (String task : wrapper.getTasks()) {
                    TaskEntity taskEntity = TaskEntity.of(event, task);
                    event.getTaskList().add(taskEntity);
                }
            }

            entityManager.persist(event); // EventEntity 저장

            // TaskEntity 저장
            for (TaskEntity taskEntity : event.getTaskList()) {
                entityManager.persist(taskEntity);
            }
        }
    }

    private long calculateAdjustedWeeks(long totalDays, long weeksBefore) {
        if (totalDays > 365) {
            return weeksBefore;  // 1년 이상 남았으면 그대로 반환
        } else {
            // 1년 이내면 비율을 계산하여 조정 (반올림하여 반환)
            double ratio = totalDays / 365.0;
            return Math.max(1, Math.round(weeksBefore * ratio));
        }
    }


    @Transactional
    @Override
    public void joinWedding(JoinWeddingCommand command) {
        UserEntity user = findUserById(command.getUserId());
        UserEntity partner = userRepository.findByMyCodeWithWedding(command.getPartnerCode())
                .orElseThrow(() -> new NotFoundException(PARTNER_NOT_FOUND_MESSAGE));

        WeddingEntity partnerWedding = partner.getWedding();

        //파트너 웨딩이 없을 때
        if (partnerWedding == null) {
            throw new NotFoundException(PARTNER_NOT_FOUND_MESSAGE);
        }

        //파트너 웨딩이 이미 매칭되어있을 때
        if (partnerWedding.getBride() != null && partnerWedding.getGroom() != null) {
            throw new BadRequestException(ALREADY_COUPLING);
        }

        WeddingEntity userWedding = user.getWedding();

        //내 웨딩이 있을 때 + 웨딩이 매칭되었다면
        if (userWedding != null && (userWedding.getBride() != null && userWedding.getGroom() != null)) {
            throw new BadRequestException(ALREADY_JOINED);
        }

        //내 웨딩이 있을 때 + 웨딩이 매칭되어있지 않으면 // 내 웨딩이 없을 때
        if (userWedding != null) {
            WeddingRole userWeddingRole = user.getWeddingRole();

            budgetRepository.deleteByWedding(userWedding);

            if (userWeddingRole != null) {
                if (userWeddingRole == BRIDE && userWedding.getGroom() != null) {
                    userWedding.getGroom().setWedding(null);
                }

                if (userWeddingRole == GROOM && userWedding.getBride() != null) {
                    userWedding.getBride().setWedding(null);
                }
            }

            weddingRepository.delete(userWedding);
        }

        matchWedding(user, partnerWedding);
        user.setWedding(partnerWedding);
    }

    @Override
    public BoardingCheckDto isOnboarded(Long userId) {
        boolean onboard = userRepository.findUserWithWedding(userId)
                .map(UserEntity::getWedding)
                .isPresent();

        return BoardingCheckDto.of(onboard);
    }

    private UserEntity findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER_ID));
    }

    private void matchWedding(UserEntity user, WeddingEntity wedding) {
        if (user.getWeddingRole() == null) {
            if (wedding.getGroom() != null && wedding.getBride() != null) {
                throw new BadRequestException(ALREADY_COUPLING);
            }

            if (wedding.getGroom() != null) {
                user.setWeddingRole(BRIDE);
            } else {
                user.setWeddingRole(GROOM);
            }
        }

        if (user.getWeddingRole() == BRIDE) {
            validateWeddingRole(wedding.getBride());
            wedding.setBride(user);
            return;
        }

        validateWeddingRole(wedding.getGroom());
        wedding.setGroom(user);
    }

    private void assignWeddingRole(WeddingRole role, UserEntity user, WeddingEntity wedding) {
        if (role == null) {
            // role이 null이면 남는 역할에 배정
            if (wedding.getBride() == null) {
                role = BRIDE;
            } else if (wedding.getGroom() == null) {
                role = GROOM;
            } else {
                throw new BadRequestException(ALREADY_COUPLING);
            }
        }

        if (role == BRIDE) {
            validateWeddingRole(wedding.getBride());
            wedding.setBride(user);
        } else if (role == GROOM) {
            validateWeddingRole(wedding.getGroom());
            wedding.setGroom(user);
        } else {
            throw new IllegalArgumentException("유효하지 않은 웨딩 역할입니다.");
        }
    }


    private void validateWeddingRole(UserEntity existingRole) {
        if (existingRole != null) {
            throw new BadRequestException(OCCUPIED_WEDDING_ROLE);
        }
    }
}
