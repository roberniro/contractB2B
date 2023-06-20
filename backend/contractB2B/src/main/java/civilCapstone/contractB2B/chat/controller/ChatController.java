package civilCapstone.contractB2B.chat.controller;

import civilCapstone.contractB2B.chat.entity.Message;
import civilCapstone.contractB2B.chat.repository.MessageRepository;
import civilCapstone.contractB2B.chat.service.Receiver;
import civilCapstone.contractB2B.chat.service.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = {"ws://localhost:3000", "ws://3.34.76.221:80", "ws://3.34.76.221"}) // CORS 설정
// 채팅을 위한 컨트롤러
public class ChatController {

    @Autowired
    private Sender sender;

    @Autowired
    private Receiver receiver;

    @Autowired
    private MessageRepository messageRepository;

    private static String BOOT_TOPIC = "contractB2B-chatting";

    // 채팅방에 메시지를 보내는 요청 처리
    @MessageMapping("/message")
    public void sendMessage(Message message) throws Exception {
        messageRepository.save(message);
        sender.send(BOOT_TOPIC, message);
    }

    // 채팅방id를 바탕으로 채팅 기록을 조회하는 요청 처리
    @RequestMapping("/history/{chatroomId}")
    public List<Message> getChattingHistory(@PathVariable String chatroomId) throws Exception {
        return messageRepository.findAllByChatroomId(chatroomId);
    }

    // 공사id를 바탕으로 채팅 기록을 조회하는 요청 처리
    @RequestMapping("/history/construction/{constructionId}")
    public List<Message> getChattingHistoryByConstruction(@PathVariable String constructionId) throws Exception {
        return messageRepository.findAllByConstructionId(constructionId);
    }

}