import React, { useEffect, useState, useRef } from "react";
import { Modal, Form, Button, ListGroup } from "react-bootstrap";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

// 시민 민원 채팅 모달 컴포넌트
const ConstructionChatModal = ({
  construction,
  showModal,
  handleCloseModal
}) => {
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [stompClient, setStompClient] = useState(null);
  const messageListRef = useRef(null);

  const chatroomId = construction.id + "&" + sessionStorage.getItem("userId");

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
  }, [messages]);

  const handleSendMessage = () => {
    if (stompClient && newMessage.trim() !== "") {
      const message = {
        message: newMessage,
        userId: sessionStorage.getItem("userId"),
        username: sessionStorage.getItem("name"),
        chatroomId: chatroomId,
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
      client.subscribe("/topic/" + chatroomId, (message) => {
        const receivedMessage = JSON.parse(message.body);
        if (receivedMessage.chatroomId === chatroomId) {
          setMessages((prevMessages) => [...prevMessages, receivedMessage]);
        }
      });
    };

    client.onConnect = onConnect;
    client.activate();

    return () => {
      if (stompClient) {
        stompClient.unsubscribe("/topic/" + chatroomId);
        stompClient.deactivate();
      }
    };
  }, []);

  useEffect(() => {
    const fetchChatHistory = async () => {
      const BASE_URL = process.env.REACT_APP_BASE_URL;
      try {
        const response = await fetch(`${BASE_URL}/history/${chatroomId}`, {
          headers: {
            Authorization: `Bearer ${sessionStorage.getItem("token")}`
          }
        });
        const history = await response.json();
        setMessages(history);
      } catch (error) {
        console.log("Error fetching chat history:", error);
      }
    };

    fetchChatHistory();
  }, []);

  return (
    <Modal show={showModal} onHide={handleCloseModal}>
      <Modal.Header closeButton>
        <Modal.Title>{construction.name}에 대한 민원</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <ListGroup
          ref={messageListRef}
          style={{ maxHeight: "400px", overflowY: "scroll" }}
        >
          {messages.map((message) => (
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
              {<br />}
              {message.message}
            </ListGroup.Item>
          ))}
        </ListGroup>
      </Modal.Body>
      <Modal.Footer>
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
      </Modal.Footer>
    </Modal>
  );
};

export default ConstructionChatModal;
