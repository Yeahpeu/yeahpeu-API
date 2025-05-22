package com.yeahpeu.wishlist.controller;

import com.yeahpeu.auth.domain.UserPrincipal;
import com.yeahpeu.wishlist.controller.condition.NaverShoppingSearchCondition;
import com.yeahpeu.wishlist.controller.request.WishItemRequest;
import com.yeahpeu.wishlist.controller.response.WishItemResponse;
import com.yeahpeu.wishlist.service.WishlistService;
import com.yeahpeu.wishlist.service.command.NaverShoppingCommand;
import com.yeahpeu.wishlist.service.command.WishItemCommand;
import com.yeahpeu.wishlist.service.dto.NaverShoppingItemDto;
import com.yeahpeu.wishlist.service.dto.WishlistWithItemDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/wedding/wishlist")
@RestController
public class WishlistController {

    private final WishlistService service;

    @Operation(
            summary = "네이버 쇼핑 검색 API",
            description = "검색 키워드와 페이지 번호를 통해 네이버 쇼핑에 등록된 상품을 조회"
    )
    @GetMapping("/shopping/naver")
    public ResponseEntity<List<NaverShoppingItemDto>> getItemList(
            @ModelAttribute @ParameterObject NaverShoppingSearchCondition condition) {
        condition.validate();

        List<NaverShoppingItemDto> items = service.getShoppingItems(
                NaverShoppingCommand.of(condition.getKeyword(), condition.getPage())
        );

        return ResponseEntity.ok(items);
    }

    @Operation(
            summary = "위시 아이템 조회 API",
            description = "위시리스트 아이템 조회 - 혼수 홈에서 보여줄 위시리스트 조회는 size=3 부여"
    )
    @GetMapping
    public ResponseEntity<WishlistWithItemDTO> getWishlist(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                           @RequestParam(required = false) Integer size) {
        WishlistWithItemDTO response = service.getWishlist(
                Long.valueOf(userPrincipal.getUsername()), size);

        return ResponseEntity.ok().body(response);
    }

    @Operation(
            summary = "위시 아이템 추가 API",
            description = "위시리스트에 아이템 추가"
    )
    @PostMapping
    public ResponseEntity<WishItemResponse> addWishItem(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                        @RequestBody WishItemRequest wishItemRequest) {
        WishItemCommand wishItemCommand = WishItemCommand.from(
                Long.valueOf(userPrincipal.getUsername()), wishItemRequest
        );
        WishItemResponse response = WishItemResponse.from(service.addWishItem(wishItemCommand));

        return ResponseEntity.created(URI.create("/api/v1/wedding/wishlist" + response.getId())).body(response);
    }

    @Operation(
            summary = "위시 아이템 삭제 API",
            description = "위시리스트 아이템 삭제"
    )
    @DeleteMapping("/{wishItemId}")
    public ResponseEntity<Void> deleteWishItem(@PathVariable("wishItemId") Long wishItemID) {
        service.deleteWishItem(wishItemID);

        return ResponseEntity.noContent().build();
    }
}
