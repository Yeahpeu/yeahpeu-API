package com.yeahpeu.wishlist.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeahpeu.common.exception.BadRequestException;
import com.yeahpeu.common.exception.NotFoundException;
import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.user.repository.UserRepository;
import com.yeahpeu.wishlist.config.NaverAPIUtil;
import com.yeahpeu.wishlist.domain.WishItemEntity;
import com.yeahpeu.wishlist.domain.WishlistEntity;
import com.yeahpeu.wishlist.repository.WishItemRepository;
import com.yeahpeu.wishlist.repository.WishItemRepositoryImpl;
import com.yeahpeu.wishlist.repository.WishlistRepository;
import com.yeahpeu.wishlist.service.command.NaverShoppingCommand;
import com.yeahpeu.wishlist.service.command.WishItemCommand;
import com.yeahpeu.wishlist.service.dto.NaverShoppingItemDto;
import com.yeahpeu.wishlist.service.dto.WishItemDTO;
import com.yeahpeu.wishlist.service.dto.WishlistWithItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yeahpeu.common.exception.ExceptionCode.*;

@RequiredArgsConstructor
@Service
public class WishlistServiceImpl implements WishlistService {

    private final ObjectMapper objectMapper;
    private final NaverAPIUtil naverAPIutil;
    private final UserRepository userRepository;
    private final WishItemRepository wishItemRepository;
    private final WishlistRepository wishlistRepository;
    private final WishItemRepositoryImpl wishItemRepositoryImpl;

    // 쇼핑 검색 API
    @Override
    public List<NaverShoppingItemDto> getShoppingItems(NaverShoppingCommand command) {

        String clientId = naverAPIutil.getCliientId(); // 애플리케이션 클라이언트 아이디
        String clientSecret = naverAPIutil.getClientSecret(); // 애플리케이션 클라이언트 시크릿

        // 검색어 UTF-8 인코딩
        String encodedKeyword;
        try {
            encodedKeyword = URLEncoder.encode(command.getKeyword(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new BadRequestException(FAIL_KEYWORD_ENCODE);
        }

        // API 요청 URI
        String uri = "https://openapi.naver.com/v1/search/shop.json?query=" + encodedKeyword
                + "&display=100&start=" + command.getPage();

        // 요청 헤더 설정
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        // HTTP 연결
        HttpURLConnection conn = connect(uri);
        try {
            conn.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                conn.setRequestProperty(header.getKey(), header.getValue());
            }

            // 응답 코드 확인
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 정상 응답 처리
                try (InputStream res = conn.getInputStream();
                     InputStreamReader streamReader = new InputStreamReader(res, StandardCharsets.UTF_8);
                     BufferedReader br = new BufferedReader(streamReader)) {

                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }

                    return parseResponseWithJackson(response.toString());
                }
            } else {
                throw new BadRequestException(FAIL_API_CALL);
            }
        } catch (IOException e) {
            throw new BadRequestException(INVALID_API_URL);
        } finally {
            conn.disconnect();
        }
    }

    private List<NaverShoppingItemDto> parseResponseWithJackson(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode itemsNode = rootNode.get("items");

            if (itemsNode != null && itemsNode.isArray()) {
                return objectMapper.readValue(
                        objectMapper.writeValueAsString(itemsNode),
                        new TypeReference<>() {
                        }
                );
            }
        } catch (Exception e) {
            throw new BadRequestException(FAIL_API_CALL);
        }
        return Collections.emptyList();
    }

    // HTTP 연결 생성
    private static HttpURLConnection connect(String apiUri) {
        try {
            URL url = new URL(apiUri);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new BadRequestException(INVALID_API_URL);
        } catch (IOException e) {
            throw new BadRequestException(FAIL_API_CALL);
        }
    }

    @Override
    public WishlistWithItemDTO getWishlist(Long userId, Integer size) {
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(NOT_FOUND_USER_ID)
        );

        WishlistEntity wishlist = wishlistRepository.findByWedding_Id(user.getWedding().getId()).orElseThrow(
                () -> new NotFoundException(NOT_FOUND_WISHLIST_ID)
        );

        List<WishItemEntity> wishItems = wishItemRepositoryImpl.findWishItems(wishlist.getId(), size).orElseThrow(
                () -> new NotFoundException(NOT_FOUND_WISHLIST_ID)
        );

        Long total = wishItemRepository.countByWishlistId(wishlist.getId()).orElse(0L);

        return WishlistWithItemDTO.from(wishlist, wishItems, total);
    }

    public WishItemDTO addWishItem(WishItemCommand wishItemCommand) {

        UserEntity user = userRepository.findById(wishItemCommand.getUserId()).orElseThrow(
                () -> new NotFoundException(NOT_FOUND_USER_ID)
        );

        WishlistEntity wishlist = wishlistRepository.findByWedding_Id(user.getWedding().getId()).orElseThrow(
                () -> new NotFoundException(NOT_FOUND_WISHLIST_ID)
        );

        wishItemRepository
                .findByWishlist_IdAndNaverProductId(wishlist.getId(), wishItemCommand.getNaverProductId())
                .ifPresent(wishItem -> {
                    throw new BadRequestException(DUPLICATE_WISHITEM);
                });

        WishItemEntity wishItem = WishItemEntity.from(wishlist, wishItemCommand);

        WishItemEntity saved = wishItemRepository.save(wishItem);

        return WishItemDTO.from(saved);
    }

    @Override
    public void deleteWishItem(Long wishItemId) {
        WishItemEntity wishItem = wishItemRepository.findById(wishItemId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_WISHITEM_ID));

        wishItemRepository.delete(wishItem);
    }
}
