package com.yeahpeu.event.controller.response;

import com.yeahpeu.event.service.dto.EventDto;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventResponse {
    private Long id;

    private String title;

    private Long mainCategoryId;

    private String location;

    private ZonedDateTime date;

    private boolean completed;

    public static EventResponse from(EventDto eventDto) {
        return new EventResponse(
                eventDto.getId(),
                eventDto.getTitle(),
                eventDto.getMainCategoryId(),
                eventDto.getLocation(),
                eventDto.getDate(),
                eventDto.isCompleted());
    }
}
