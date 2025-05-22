package com.yeahpeu.event.service.dto;

import com.yeahpeu.event.domain.EventEntity;
import com.yeahpeu.task.service.dto.TaskDto;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventDetailDto extends EventDto {

    private List<TaskDto> checklists;

    @Builder(builderMethodName = "detailBuilder")
    private EventDetailDto(Long id, String title, ZonedDateTime date, String location,
                           Long mainCategoryId, Long subcategoryId, int price, boolean completed,
                           List<TaskDto> checklists) {
        super(id, title, date, location, mainCategoryId, subcategoryId, price, completed);
        this.checklists = checklists;
    }

    public static EventDetailDto from(EventEntity eventEntity, List<TaskDto> checklists) {
        return EventDetailDto.detailBuilder()
                .id(eventEntity.getId())
                .title(eventEntity.getTitle())
                .date(eventEntity.getDate())
                .location(eventEntity.getLocation())
                .mainCategoryId(eventEntity.getMainCategory().getId())
                .subcategoryId(eventEntity.getSubCategory().getId())
                .price(eventEntity.getPrice())
                .completed(eventEntity.isCompleted())
                .checklists(checklists)
                .build();
    }
}
