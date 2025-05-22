package com.yeahpeu.chat.config;

import com.yeahpeu.auth.util.JwtUtil;
import com.yeahpeu.common.exception.TokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        // 메시지 컨텍스트 유지
        StompHeaderAccessor accessor = MessageHeaderAccessor
                .getAccessor(message, StompHeaderAccessor.class);

        // WebSocket CONNECT 요청에서 Authorization 헤더 추출
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // "Bearer " 제거

                if (jwtUtil.validateToken(token)) {
                    String emailAddress = jwtUtil.getSubject(token);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(emailAddress);

                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    // SecurityContext에 Authentication 설정
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // Principal 설정 추가
                    accessor.setUser(authentication);

                } else {
                    throw new TokenException("Invalid token");
                }
            } else {
                throw new TokenException("Missing Authorization header");
            }
        }

        return message;
    }
}
