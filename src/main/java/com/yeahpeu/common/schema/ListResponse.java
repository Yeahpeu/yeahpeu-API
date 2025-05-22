package com.yeahpeu.common.schema;

import java.util.List;

public record ListResponse<T>(List<T> items) {
    public static <T> ListResponse<T> of(List<T> items) {
        return new ListResponse<>(items);
    }
}