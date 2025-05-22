package com.yeahpeu.event.service.dto;

import com.yeahpeu.event.domain.EventEntity;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventDto {

    private Long id;

    private String title;

    private ZonedDateTime date;

    private String location;

    private Long mainCategoryId;

    private Long subcategoryId;

    private int price;

    private boolean completed;

    @Builder
    protected EventDto(Long id, String title, ZonedDateTime date, String location,
                       Long mainCategoryId,
                       Long subcategoryId, int price, boolean completed) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.location = location;
        this.mainCategoryId = mainCategoryId;
        this.subcategoryId = subcategoryId;
        this.price = price;
        this.completed = completed;
    }

    public static EventDto from(EventEntity eventEntity) {
        return EventDto.builder()
                .id(eventEntity.getId())
                .title(eventEntity.getTitle())
                .date(eventEntity.getDate())
                .location(eventEntity.getLocation())
                .mainCategoryId(eventEntity.getMainCategory().getId())
                .subcategoryId(eventEntity.getSubCategory().getId())
                .price(eventEntity.getPrice())
                .completed(eventEntity.isCompleted())
                .build();
    }
}
