package com.yeahpeu.event.controller.condition;

import static com.yeahpeu.common.exception.ExceptionCode.INVALID_SIZE;

import com.yeahpeu.common.exception.BadRequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
public class EventSearchCondition extends DateBasedCondition {

    private Boolean completed;

    private Integer size;

    public EventSearchCondition(boolean completed, int size, String startDate, String endDate) {
        super(startDate, endDate);
        this.completed = completed;
        this.size = size;
    }

    public void validateSize() {
        if (size != null && size <= 0) {
            throw new BadRequestException(INVALID_SIZE);
        }
    }
}