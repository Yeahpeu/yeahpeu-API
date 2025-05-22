package com.yeahpeu.event.service.command;

import com.yeahpeu.event.controller.request.EventCreateRequest;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventCreateCommand {

    private Long userId;

    private String title;

    private ZonedDateTime date;

    private String location;

    private Long mainCategoryId;

    private Long subcategoryId;

    private int price;

    public static EventCreateCommand from(Long userId, EventCreateRequest eventCreateRequest) {
        return new EventCreateCommand(
                userId,
                eventCreateRequest.getTitle(),
                eventCreateRequest.getDate(),
                eventCreateRequest.getLocation(),
                eventCreateRequest.getMainCategoryId(),
                eventCreateRequest.getSubcategoryId(),
                eventCreateRequest.getPrice()
        );
    }
}
