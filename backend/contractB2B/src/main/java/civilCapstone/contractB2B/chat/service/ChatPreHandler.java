package civilCapstone.contractB2B.chat.service;

import civilCapstone.contractB2B.user.service.TokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
// 채팅 전처리기
public class ChatPreHandler implements ChannelInterceptor {
    @Autowired
    private TokenProvider tokenProvider;

    @Override
    // jwt 토큰 유효성 검증
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("preSend start");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (accessor.getCommand() == StompCommand.CONNECT) {
            List<String> authorizationHeaders = accessor.getNativeHeader("Authorization");
            if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
                String token = authorizationHeaders.get(0);
                try {
                    String username = tokenProvider.validateAndGetUsername(token);
                    log.info("username: {}", username);
                } catch (MalformedJwtException e) {
                    log.error("preSend: 토큰이 유효하지 않습니다.");
                    throw new AccessDeniedException("토큰이 유효하지 않습니다.");
                }
            }
        }
        return message;
    }
}

