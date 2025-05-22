package com.yeahpeu.user.controller.response;

import com.yeahpeu.user.service.dto.WeddingInfoDTO;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class WeddingInfoResponse {
    private String partnerName;
    private Long budget;
    private ZonedDateTime weddingDay;

    public static WeddingInfoResponse from(WeddingInfoDTO dto) {
        return WeddingInfoResponse.builder()
                .partnerName(dto.getPartnerName())
                .budget(dto.getBudget())
                .weddingDay(dto.getWeddingDay())
                .build();
    }
}
