package com.yeahpeu.event.controller.request;

import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EventCreateRequest {
    private String title;

    private ZonedDateTime date;

    private String location;

    private Long mainCategoryId;

    private Long subcategoryId;

    private int price;

    //price 검증
}
