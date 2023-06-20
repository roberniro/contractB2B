import React, { useEffect, useState, useRef } from "react";
import { Modal, Form, Button, ListGroup } from "react-bootstrap";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

// 원청 민원 채팅 모달 컴포넌트
const ConstructionChatListModal = ({
  construction,
  showModal,
  handleCloseModal
}) => {
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [stompClient, setStompClient] = useState(null);
  const [showChatRoom, setShowChatRoom] = useState(false);
  const [selectedChatroom, setSelectedChatroom] = useState([]);
  const messageListRef = useRef(null);

  // const chatroomId = construction.id + "&" + sessionStorage.getItem("userId");

  const handleEnter = (e) => {
    if (e.key === "Enter") {
      e.preventDefault();
      handleSendMessage();
    }
  };

  useEffect(() => {
    if (messageListRef.current) {
      messageListRef.current.scrollTop = messageListRef.current.scrollHeight;
    }
  }, [messages, selectedChatroom]);

  const handleShowChatRoom = (selectedChatroom) => {
    setShowChatRoom(true);
    setSelectedChatroom(selectedChatroom);
  };

  const handleUnshowChatRoom = () => {
    setShowChatRoom(false);
    setSelectedChatroom([]);
  };

  const handleSendMessage = () => {
    if (stompClient && newMessage.trim() !== "") {
      const message = {
        message: newMessage,
        userId: sessionStorage.getItem("userId"),
        username: sessionStorage.getItem("name"),
        chatroomId: selectedChatroom[0].chatroomId,
        constructionId: construction.id,
        constructionName: construction.name,
        timestamp: new Date().toISOString()
      };
      stompClient.publish({
        destination: "/app/message",
        body: JSON.stringify(message)
      });
      // setMessages((prevMessages) => [...prevMessages, message]);
      setNewMessage("");
    }
  };

  useEffect(() => {
    const BASE_URL = process.env.REACT_APP_BASE_URL;
    const sokcet = new SockJS(`${BASE_URL}/chatting`);
    const headers = () => ({
      Authorization: `Bearer ${sessionStorage.getItem("token")}`
    });
    const client = new Client({
      webSocketFactory: () => sokcet,
      connectHeaders: headers,
      debug: (str) => {
        console.log(str);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000
    });

    const onConnect = () => {
      setStompClient(client);
      client.subscribe("/topic/" + construction.id, (message) => {
        const receivedMessage = JSON.parse(message.body);
        setMessages((prevMessages) => [...prevMessages, receivedMessage]);
      });
    };

    client.onConnect = onConnect;
    client.activate();

    return () => {
      if (stompClient) {
        stompClient.unsubscribe("/topic/" + construction.id);
        stompClient.deactivate();
      }
    };
  }, []);

  useEffect(() => {
    const fetchChatHistory = async () => {
      const BASE_URL = process.env.REACT_APP_BASE_URL;
      try {
        const response = await fetch(
          `${BASE_URL}/history/construction/${construction.id}`,
          {
            headers: {
              Authorization: `Bearer ${sessionStorage.getItem("token")}`
            }
          }
        );
        const history = await response.json();
        setMessages(history);
      } catch (error) {
        console.log("Error fetching chat history:", error);
      }
    };

    fetchChatHistory();
  }, []);

  useEffect(() => {
    if (messages.length === 0 || selectedChatroom.length === 0) {
      return;
    }
    if (
      messages[messages.length - 1].chatroomId ===
      selectedChatroom[0].chatroomId
    ) {
      setSelectedChatroom((prevChatroom) => [
        ...prevChatroom,
        messages[messages.length - 1]
      ]);
    }
  }, [messages]);

  const messagesByChatroom = messages.reduce((acc, message) => {
    const chatroomId = message.chatroomId;
    if (!acc[chatroomId]) {
      // 새로운 chatroom인 경우 새로운 배열로 초기화
      acc[chatroomId] = [];
    }
    // 해당 chatroom에 메시지 추가
    acc[chatroomId].push(message);
    return acc;
  }, {});

  return (
    <Modal show={showModal} onHide={handleCloseModal}>
      <Modal.Header closeButton>
        <Modal.Title onClick={handleUnshowChatRoom} role="button">
          {construction.name}에 대한 민원
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {!showChatRoom ? (
          messages.length === 0 ? (
            <h5>제기된 민원이 없습니다.</h5>
          ) : (
            <ListGroup>
              {Object.keys(messagesByChatroom).map((chatroomId) => (
                <ListGroup.Item
                  key={chatroomId}
                  onClick={() =>
                    handleShowChatRoom(messagesByChatroom[chatroomId])
                  }
                  variant="warning"
                  role="button"
                >
                  {messagesByChatroom[chatroomId][0].username}님의 민원 상담
                  요청{" "}
                  {messagesByChatroom[chatroomId][0].timestamp
                    .trim()
                    .slice(2, 10)}{" "}
                  {messagesByChatroom[chatroomId][0].timestamp
                    .trim()
                    .slice(11, 19)}
                </ListGroup.Item>
              ))}
            </ListGroup>
          )
        ) : (
          <ListGroup
            ref={messageListRef}
            style={{ maxHeight: "400px", overflowY: "scroll" }}
          >
            {selectedChatroom.map((message) => (
              <ListGroup.Item
                key={message.id}
                variant={
                  message.userId === sessionStorage.getItem("userId")
                    ? "info"
                    : "warning"
                }
              >
                {message.username} {message.timestamp.trim().slice(2, 10)}{" "}
                {message.timestamp.trim().slice(11, 19)}
                <br />
                {message.message}
              </ListGroup.Item>
            ))}
          </ListGroup>
        )}
      </Modal.Body>
      <Modal.Footer>
        {!showChatRoom ? (
          <Button variant="secondary" onClick={handleCloseModal}>
            닫기
          </Button>
        ) : (
          <div style={{ width: "100%" }}>
            <div
              style={{
                display: "flex",
                alignItems: "center",
                justifyContent: "space-between"
              }}
            >
              <Form.Control
                type="text"
                placeholder="메시지를 입력하세요"
                value={newMessage}
                onChange={(e) => setNewMessage(e.target.value)}
                onKeyDown={(e) => handleEnter(e)}
                style={{ width: "85%" }}
              />
              <Button variant="success" onClick={handleSendMessage}>
                발송
              </Button>
            </div>
          </div>
        )}
      </Modal.Footer>
    </Modal>
  );
};

export default ConstructionChatListModal;
