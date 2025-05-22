package com.yeahpeu.event.repository;

import static com.yeahpeu.event.domain.QEventEntity.eventEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeahpeu.event.controller.condition.DateBasedCondition;
import com.yeahpeu.event.controller.condition.EventSearchCondition;
import com.yeahpeu.event.domain.EventEntity;
import com.yeahpeu.event.util.DateUtils;
import com.yeahpeu.wedding.domain.WeddingEntity;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class EventRepositoryCustomImpl implements EventRepositoryCustom {

    static final String LOCAL_ZONE_ID = "Asia/Seoul";
    static final String DB_ZONE_ID = "UTC";

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EventEntity> searchEventByCondition(WeddingEntity wedding, EventSearchCondition condition) {
        JPAQuery<EventEntity> query = queryFactory
                .selectFrom(eventEntity)
                .where(
                        weddingIdEq(wedding.getId()),
                        betweenDates(DateUtils.parseDate(condition.getStartDate()),
                                DateUtils.parseDate(condition.getEndDate())),
                        completed(condition.getCompleted())
                )
                .orderBy(eventEntity.date.asc());

        if (condition.getSize() != null) {
            query.limit(condition.getSize());
        }

        return query.fetch();
    }

    @Override
    public List<EventEntity> searchEventByDates(WeddingEntity wedding, DateBasedCondition condition) {
        return queryFactory.selectFrom(eventEntity)
                .where(
                        weddingIdEq(wedding.getId()),
                        betweenDates(DateUtils.parseDate(condition.getStartDate()),
                                DateUtils.parseDate(condition.getEndDate()))
                )
                .orderBy(eventEntity.date.asc())
                .fetch();
    }

    private BooleanExpression weddingIdEq(Long weddingId) {
        return eventEntity.wedding.id.eq(weddingId);
    }

    private BooleanExpression betweenDates(LocalDate startDate, LocalDate endDate) {
        ZonedDateTime startDateTime = startDate.atStartOfDay(ZoneId.of(LOCAL_ZONE_ID))
                .withZoneSameInstant(ZoneId.of(DB_ZONE_ID));

        ZonedDateTime endDateTime = endDate.atTime(23, 59, 59)
                .atZone(ZoneId.of(LOCAL_ZONE_ID))
                .withZoneSameInstant(ZoneId.of(DB_ZONE_ID));

        return eventEntity.date.between(startDateTime, endDateTime);
    }

    private BooleanExpression completed(Boolean completed) {
        if (completed == null) {
            return null;
        }

        return completed ? eventEntity.completed.isTrue() : eventEntity.completed.isFalse();
    }
}