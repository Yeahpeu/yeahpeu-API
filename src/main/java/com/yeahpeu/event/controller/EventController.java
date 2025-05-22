package com.yeahpeu.event.controller;

import com.yeahpeu.auth.domain.UserPrincipal;
import com.yeahpeu.common.request.UpdateStatusRequest;
import com.yeahpeu.event.controller.condition.DateBasedCondition;
import com.yeahpeu.event.controller.condition.EventSearchCondition;
import com.yeahpeu.event.controller.request.EventCreateRequest;
import com.yeahpeu.event.controller.response.EventResponse;
import com.yeahpeu.event.service.EventServiceImpl;
import com.yeahpeu.event.service.command.EventCreateCommand;
import com.yeahpeu.event.service.command.UpdateStatusCommand;
import com.yeahpeu.event.service.dto.EventDetailDto;
import com.yeahpeu.event.service.dto.EventDto;
import com.yeahpeu.event.service.dto.EventStatusDto;
import com.yeahpeu.event.service.dto.EventSummaryDto;
import com.yeahpeu.event.service.dto.ProgressDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Event Controller",
        description = "일정 정보 관리"
)
@RequestMapping("/api/v1/wedding/events")
@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventServiceImpl eventService;

    @Operation(
            summary = "일정 조회(년도별, 월별, 일별, 날짜 이후, 미완료 1개)",
            description = "년도별, 월별, 일별, 날짜 이후, 또는 미완료 일정 1개를 조회합니다."
    )
    @GetMapping
    public ResponseEntity<List<EventResponse>> getEvents(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                         @ModelAttribute @ParameterObject
                                                         EventSearchCondition condition) {
        eventService.validateDate(condition);

        List<EventDto> eventsByCondition = eventService.getEventsByDateRange(condition, extractUserId(userPrincipal));

        List<EventResponse> response = eventsByCondition.stream()
                .map(EventResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "일정 요약 조회(연도 & 월)",
            description = "등록된 일정을 요청한 연도와 월을 기준으로 카테고리를 요약하여 조회합니다."
    )
    @GetMapping("/preview")
    public ResponseEntity<List<EventSummaryDto>> getEventsPreview(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                  @ModelAttribute @ParameterObject
                                                                  DateBasedCondition condition) {
        eventService.validateDate(condition);

        List<EventSummaryDto> preview = eventService.getEventsPreview(extractUserId(userPrincipal), condition);

        return ResponseEntity.ok(preview);
    }

    @Operation(
            summary = "일정 세부 조회",
            description = "등록된 일정에 대한 세부 내용을 조회합니다."
    )
    @GetMapping("/{eventId}")
    public ResponseEntity<EventDetailDto> getEvent(@PathVariable Long eventId) {
        EventDetailDto eventDetail = eventService.getEvent(eventId);

        return ResponseEntity.ok(eventDetail);
    }

    @Operation(
            summary = "서브카테고리별 일정 조회",
            description = "등록된 일정 중 서브카테고리별로 조회합니다."
    )
    @GetMapping("/subcategories/{categoryId}")
    public ResponseEntity<List<EventResponse>> getEventsBySub(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                              @PathVariable Long categoryId) {
        List<EventDto> eventsBySubcategory = eventService.getEventsBySubCategory(extractUserId(userPrincipal),
                categoryId);

        List<EventResponse> response = eventsBySubcategory.stream()
                .map(EventResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "일정 완료 상태 변환",
            description = "등록된 일정의 상태를 변화합니다."
    )
    @PatchMapping("/{eventId}/status")
    public ResponseEntity<EventStatusDto> updateEventStatus(@PathVariable Long eventId,
                                                            @RequestBody UpdateStatusRequest updateStatusRequest) {
        UpdateStatusCommand command = UpdateStatusCommand.from(eventId, updateStatusRequest);

        EventStatusDto updated = eventService.updateEventStatus(command);

        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "일정 추가",
            description = "새로운 일정을 추가합니다."
    )

    @PostMapping
    public ResponseEntity<EventDto> addEvent(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                             @RequestBody final EventCreateRequest eventCreateRequest) {
        EventCreateCommand command = createEventCommand(userPrincipal, eventCreateRequest);

        EventDto saved = eventService.save(command);

        return ResponseEntity.ok(saved);
    }

    @Operation(
            summary = "일정 수정",
            description = "등록된 일정을 수정합니다."
    )
    @PutMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEvent(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                @PathVariable Long eventId,
                                                @RequestBody EventCreateRequest eventCreateRequest) {
        EventCreateCommand command = createEventCommand(userPrincipal, eventCreateRequest);

        EventDto updated = eventService.updateEvent(eventId, command);

        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "일정 삭제",
            description = "등록된 일정을 삭제합니다."
    )
    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);

        return ResponseEntity.noContent()
                .build();
    }

    @Operation(
            summary = "progress bar 조회",
            description = "전체 일정 중 완료된 일정의 퍼센트를 조회합니다."
    )
    @GetMapping("/bar")
    public ResponseEntity<ProgressDto> getProgressBar(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        ProgressDto response = eventService.getProgressBar(extractUserId(userPrincipal));

        return ResponseEntity.ok(response);
    }

    private EventCreateCommand createEventCommand(UserPrincipal userPrincipal, EventCreateRequest eventCreateRequest) {
        return EventCreateCommand.from(extractUserId(userPrincipal), eventCreateRequest);
    }

    private static Long extractUserId(UserPrincipal userPrincipal) {
        return Long.valueOf(userPrincipal.getUsername());
    }
}

