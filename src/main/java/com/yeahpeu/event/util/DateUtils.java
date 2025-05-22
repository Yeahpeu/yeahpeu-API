package com.yeahpeu.event.util;

import static com.yeahpeu.common.exception.ExceptionCode.INVALID_DATE_FORMAT;
import static com.yeahpeu.common.exception.ExceptionCode.INVALID_DATE_RANGE;

import com.yeahpeu.common.exception.BadRequestException;
import com.yeahpeu.event.controller.condition.DateBasedCondition;
import lombok.Getter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {

    private static final ZoneId ASIA_SEOUL_ZONE = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE;

    public static void validateDate(DateBasedCondition condition) {
        String startDateStr = condition.getStartDate();
        String endDateStr = condition.getEndDate();

        boolean isStartProvided = startDateStr != null && !startDateStr.trim().isEmpty();
        boolean isEndProvided = endDateStr != null && !endDateStr.trim().isEmpty();

        LocalDate parsedStartDate;
        LocalDate parsedEndDate;

        // 두 날짜 모두 제공된 경우
        if (isStartProvided && isEndProvided) {
            parsedStartDate = parseDate(startDateStr);
            parsedEndDate = parseDate(endDateStr);
        }
        // 두 날짜 모두 제공되지 않은 경우: 기본값 할당 (현재 기준 180일 전 ~ 현재)
        else if (!isStartProvided && !isEndProvided) {
            parsedStartDate = LocalDate.now(ASIA_SEOUL_ZONE).minusDays(180);
            parsedEndDate = LocalDate.now(ASIA_SEOUL_ZONE);
        }
        // 한쪽만 제공된 경우: 잘못된 요청으로 처리
        else {
            throw new BadRequestException(INVALID_DATE_FORMAT);
        }

        // 날짜 범위 체크: 시작 날짜가 종료 날짜보다 이후이면 예외 발생
        if (parsedStartDate.isAfter(parsedEndDate)) {
            throw new BadRequestException(INVALID_DATE_RANGE);
        }
    }

    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr.trim(), FORMATTER);
        } catch (DateTimeParseException e) {
            throw new BadRequestException(INVALID_DATE_FORMAT);
        }
    }

    public static ZoneId getAsiaSeoulZone(){
        return ASIA_SEOUL_ZONE;
    }
}
