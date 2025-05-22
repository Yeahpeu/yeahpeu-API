package com.yeahpeu.event.controller.condition;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DateBasedCondition {

    @Schema(description = "yyyy-MM-dd", example = "2025-02-21")
    private String startDate;

    @Schema(description = "yyyy-MM-dd", example = "2025-02-21")
    private String endDate;
}
