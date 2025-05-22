package com.yeahpeu.event.service.dto;

import com.yeahpeu.event.domain.EventEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventStatusDto {

    private Long scheduleId;

    private boolean completed;

    public static EventStatusDto from(EventEntity eventEntity) {
        return new EventStatusDto(eventEntity.getId(), eventEntity.isCompleted());
    }
}
