package civilCapstone.contractB2B.chat.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages") // 몽고디비에 저장할 컬렉션 이름을 지정
// 메시지 엔티티
// 메시지 전송 시간을 기준으로 정렬하기 위해 Serializable 인터페이스를 구현
// Serializable 인터페이스를 구현하지 않으면 메시지 전송 시간을 기준으로 정렬할 수 없음
public class Message implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid") // uuid를 사용하여 id를 생성
    private String id; // 메시지 id
    private String message; // 메시지 내용
    private String userId; // 메시지를 보낸 사용자 id
    private String username; // 메시지를 보낸 사용자 이름
    private String chatroomId; // 메시지를 보낼 채팅방 id
    private String constructionId; // 메시지를 보낼 공사 id
    private String constructionName; // 메시지를 보낼 공사 이름
    private String timestamp; // 메시지 전송 시간

}
