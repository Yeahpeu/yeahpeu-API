package com.yeahpeu.event.repository;

import com.yeahpeu.event.controller.condition.DateBasedCondition;
import com.yeahpeu.event.controller.condition.EventSearchCondition;
import com.yeahpeu.event.domain.EventEntity;
import com.yeahpeu.wedding.domain.WeddingEntity;
import java.util.List;

public interface EventRepositoryCustom {
    List<EventEntity> searchEventByCondition(WeddingEntity wedding, EventSearchCondition condition);

    List<EventEntity> searchEventByDates(WeddingEntity wedding, DateBasedCondition condition);
}
