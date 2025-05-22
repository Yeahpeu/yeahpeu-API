package com.yeahpeu.ai.util;

import com.yeahpeu.ai.controller.requset.PromptRequest;
import com.yeahpeu.budget.domain.BudgetEntity;
import com.yeahpeu.budget.service.dto.BudgetSummaryDTO;
import com.yeahpeu.event.service.dto.EventDto;
import com.yeahpeu.task.domain.TaskEntity;
import java.util.List;

public class EntityToStringUtil {

    private static String convert(EventDto eventEntity) {

        return "|일정 이름 -> " + eventEntity.getTitle() + "|" +
                "날짜 및 시간 -> " + eventEntity.getDate() + "|" +
                "지역 -> " + (eventEntity.getLocation() == null ? "미정" : eventEntity.getLocation()) + "|" +
//                "카테고리 대분류 -> " + eventEntity.getMainCategory().getName() + "|" +
//                "카테고리 소분류 -> " + eventEntity.getSubCategory().getName() + "|" +
                "사용할 예산 -> " + eventEntity.getPrice() + "|" +
                "수행여부 -> " + eventEntity.isCompleted() + "|\n";
    }

    public static String convert(String title, BudgetSummaryDTO budgetSummaryDTO) {

        return "|총 예산 -> " + budgetSummaryDTO.getTotalBudget() + "|" +
                "이미 사용된 예산 -> " + budgetSummaryDTO.getUsedBudget() + "|\n";
    }

    private static String convert(BudgetEntity budgetEntity) {

        return "| 설정예산  -> " + budgetEntity.getTotalBudget() + "|" +
                " 지금까지 사용한 예산 -> " + budgetEntity.getUsedBudget() + "|\n";
    }

    private static String convert(PromptRequest.PromptVariable promptVariable) {

        StringBuilder variable = new StringBuilder("|조건 명 -> " + promptVariable.getKey() + "|" +
                "우선순위 -> " + promptVariable.getPriority() + "|" +
                "조건 정보 -> ");
        for (String value : promptVariable.getValues()) {
            variable.append(value).append(",");
        }
        return variable + "|\n";
    }

    private static String convert(TaskEntity taskEntity) {
        return "|세부체크항목 이름 -> " + taskEntity.getName() + "|\n";
    }

    public static <E> String convertList(String title, List<E> entityList) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(title).append(" : {");
        for (E entity : entityList) {
            if (entity instanceof EventDto) {
                stringBuilder.append(convert((EventDto) entity));
            } else if (entity instanceof BudgetEntity) {
                stringBuilder.append(convert((BudgetEntity) entity));
            } else if (entity instanceof TaskEntity) {
                stringBuilder.append(convert((TaskEntity) entity));
            } else if (entity instanceof PromptRequest.PromptVariable) {
                stringBuilder.append(convert((PromptRequest.PromptVariable) entity));
            }
        }
        stringBuilder.append("}\n");
        return stringBuilder.toString();
    }
}
