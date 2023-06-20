package civilCapstone.contractB2B.chat.service;

import civilCapstone.contractB2B.chat.entity.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
// 카프카 토픽에서 메시지 수신하여 웹소켓으로 발신
public class Receiver {
    @Autowired
    private SimpMessagingTemplate template;

    @KafkaListener(id = "main-listener", topics = "contractB2B-chatting")
    public void receive(Message message) throws Exception {
        log.info("message='{}'", message);
        HashMap<String, String> msg = new HashMap<>();
        msg.put("timestamp", message.getTimestamp().toString());
        msg.put("message", message.getMessage());
        msg.put("username", message.getUsername());
        msg.put("userId", message.getUserId());
        msg.put("chatroomId", message.getChatroomId());
        msg.put("constructionId", message.getConstructionId());
        msg.put("constructionName", message.getConstructionName());

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(msg);

        this.template.convertAndSend("/topic/" + message.getConstructionId(), json);
        this.template.convertAndSend("/topic/" + message.getChatroomId(), json);
        this.template.convertAndSend("/topic/public", json);
    }
}

