package civilCapstone.contractB2B.chat.repository;

import civilCapstone.contractB2B.chat.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// 메시지 레포지토리
public interface MessageRepository extends MongoRepository<Message, String> {

    List<Message> findAllByChatroomId(String chatroomId); // 채팅방 id로 메시지 목록 조회

    List<Message> findAllByConstructionId(String constructionId); // 공사 id로 메시지 목록 조회
}
