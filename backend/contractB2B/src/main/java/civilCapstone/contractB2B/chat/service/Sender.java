package civilCapstone.contractB2B.chat.service;

import civilCapstone.contractB2B.chat.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
// 카프카 토픽으로 메시지 발신
public class Sender {
    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    public void send(String topic, Message data) {
        log.info("sending data='{}' to topic='{}'", data, topic);
        kafkaTemplate.send(topic, data);
    }
}
