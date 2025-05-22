package com.yeahpeu.event.service;

import com.yeahpeu.event.controller.condition.DateBasedCondition;
import com.yeahpeu.event.controller.condition.EventSearchCondition;
import com.yeahpeu.event.service.command.EventCreateCommand;
import com.yeahpeu.event.service.command.UpdateStatusCommand;
import com.yeahpeu.event.service.dto.EventDetailDto;
import com.yeahpeu.event.service.dto.EventDto;
import com.yeahpeu.event.service.dto.EventStatusDto;
import com.yeahpeu.event.service.dto.EventSummaryDto;
import com.yeahpeu.event.service.dto.ProgressDto;
import java.util.List;

public interface EventService {

    EventDto save(EventCreateCommand eventCreateCommand);

    EventDto updateEvent(Long eventId, EventCreateCommand eventCreateCommand);

    void deleteEvent(Long eventId);

    EventStatusDto updateEventStatus(UpdateStatusCommand updateStatusCommand);

    List<EventDto> getEventsBySubCategory(Long userId, Long subcategoryId);

    List<EventDto> getEventsByDateRange(EventSearchCondition condition, Long userId);

    EventDetailDto getEvent(Long eventId);

    List<EventSummaryDto> getEventsPreview(Long userId, DateBasedCondition condition);

    void validateDate(DateBasedCondition condition);

    ProgressDto getProgressBar(Long userId);
}
