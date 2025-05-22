package com.yeahpeu.wishlist.controller.condition;

import com.yeahpeu.common.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.yeahpeu.common.exception.ExceptionCode.KEYWORD_NULL;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NaverShoppingSearchCondition {

    private String keyword;
    private Integer page;

    public void setPage(Integer page) {
        this.page = (page == null || page < 1) ? 1 : page;
    }

    public void validate() {
        validateKeyword();
    }

    private void validateKeyword() {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BadRequestException(KEYWORD_NULL);
        }
    }
}
