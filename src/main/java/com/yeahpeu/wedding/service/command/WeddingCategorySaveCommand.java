package com.yeahpeu.wedding.service.command;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeddingCategorySaveCommand {
    private Long weddingId;
    private List<Long> categoryIds;

    public static WeddingCategorySaveCommand from(Long weddingId, List<Long> categoryIds) {
        return new WeddingCategorySaveCommand(weddingId, categoryIds);
    }
}
