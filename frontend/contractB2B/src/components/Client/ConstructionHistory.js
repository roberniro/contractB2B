import React, { useEffect, useState } from "react";
import {
  Container,
  Form,
  Button,
  Table,
  FormGroup,
  Dropdown
} from "react-bootstrap";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import ConstructionInfoModal from "../Modal/Client/ConstructionInfoModal";
import ConstructionChatListModal from "../Modal/Client/ConstructionChatListModal";

// 원청 공사 페이지 컴포넌트
const ConstructionHistory = () => {
  const [constructions, setConstructions] = useState([]);
  const [selectedConstruction, setSelectedConstruction] = useState(null);
  const [filteredConstructions, setFilteredConstructions] =
    useState(constructions);
  const [showConstructionInfoModal, setShowConstructionInfoModal] =
    useState(false);
  const [showConstructionChatListModal, setShowConstructionChatListModal] =
    useState(false);
  const [city, setCity] = useState("");
  const [district, setDistrict] = useState("");
  const [field, setField] = useState("");
  const [stompClient, setStompClient] = useState(null);
  const [messages, setMessages] = useState([]);

  const getDistrictOptions = () => {
    if (city === "서울시") {
      return [
        "강남구",
        "강동구",
        "강북구",
        "강서구",
        "관악구",
        "광진구",
        "구로구",
        "금천구",
        "노원구",
        "도봉구",
        "동대문구",
        "동작구",
        "마포구",
        "서대문구",
        "서초구",
        "성동구",
        "성북구",
        "송파구",
        "양천구",
        "영등포구",
        "용산구",
        "은평구",
        "종로구",
        "중구",
        "중랑구"
      ];
    } else if (city === "경기도") {
      return [
        "가평군",
        "고양시",
        "과천시",
        "광명시",
        "광주시",
        "구리시",
        "군포시",
        "김포시",
        "남양주시",
        "동두천시",
        "부천시",
        "성남시",
        "수원시",
        "시흥시",
        "안산시",
        "안성시",
        "안양시",
        "양주시",
        "양평군",
        "여주시",
        "연천군",
        "오산시",
        "용인시",
        "의왕시",
        "의정부시",
        "이천시",
        "파주시",
        "평택시",
        "포천시",
        "하남시",
        "화성시"
      ];
    } else {
      return [];
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
      client.subscribe("/topic/public", (message) => {
        const receivedMessage = JSON.parse(message.body);
        setMessages((prevMessages) => [...prevMessages, receivedMessage]);
      });
    };

    client.onConnect = onConnect;
    client.activate();

    return () => {
      if (stompClient) {
        stompClient.unsubscribe("/topic/public");
        stompClient.deactivate();
      }
    };
  }, []);

  const handleMessageAlert = (messages) => {
    if (messages.length > 0) {
      const lastMessage = messages[messages.length - 1];
      if (lastMessage.userId !== sessionStorage.getItem("userId")) {
        if (!showConstructionChatListModal)
          alert(
            `${lastMessage.username}님이 ${lastMessage.constructionName}에 새로운 메시지를 보냈습니다.`
          );
      }
    }
  };

  useEffect(() => {
    handleMessageAlert(messages);
  }, [messages]);

  const handleShowConstructionInfoModal = (construction) => {
    setSelectedConstruction(construction);
    setShowConstructionInfoModal(true);
  };

  const handleShowConstructionChatListModal = (construction) => {
    setSelectedConstruction(construction);
    setShowConstructionChatListModal(true);
  };

  const handleCloseModal = () => {
    setSelectedConstruction(null);
    setShowConstructionInfoModal(false);
  };

  const getConstruction = () => {
    const BASE_URL = process.env.REACT_APP_BASE_URL;
    fetch(`${BASE_URL}/client/construction`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${sessionStorage.getItem("token")}`
      }
    })
      .then((res) => {
        if (res.ok) {
          res.json().then((data) => {
            setConstructions(data.get_construction);
            setFilteredConstructions(data.get_construction);
          });
        } else {
          res.json().then((data) => {
            alert(data.error.get_construction);
          });
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const handleStatusChange = (construction, option) => {
    const BASE_URL = process.env.REACT_APP_BASE_URL;
    fetch(`${BASE_URL}/client/construction/${construction.id}/status`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${sessionStorage.getItem("token")}`
      },
      body: JSON.stringify({
        status: option
      })
    }).then((res) => {
      if (res.ok) {
        res.json().then((data) => {
          getConstruction();
        });
      } else {
        res.json().then((data) => {
          alert(data.error.change_construction_status);
        });
      }
    });
  };

  const handleRatingChange = (construction, rating) => {
    const BASE_URL = process.env.REACT_APP_BASE_URL;
    fetch(`${BASE_URL}/client/construction/${construction.id}/rating`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${sessionStorage.getItem("token")}`
      },
      body: JSON.stringify({
        rating: rating
      })
    }).then((res) => {
      if (res.ok) {
        res.json().then((data) => {
          getConstruction();
        });
      } else {
        res.json().then((data) => {
          alert(data.error.end_construction);
        });
      }
    });
  };

  useEffect(() => {
    if (!showConstructionInfoModal) getConstruction();
  }, [showConstructionInfoModal]);

  const handleCityChange = (e) => {
    setCity(e.target.value);
    handleFilter();
  };

  const handleDistrictChange = (e) => {
    setDistrict(e.target.value);
    handleFilter();
  };

  const handleFieldChange = (e) => {
    setField(e.target.value);
    handleFilter();
  };

  const handleFilter = () => {
    let filteredData = constructions;

    if (city) {
      filteredData = filteredData.filter(
        (construction) => construction.city === city
      );
    }

    if (district) {
      filteredData = filteredData.filter(
        (construction) => construction.district === district
      );
    }

    if (field) {
      filteredData = filteredData.filter(
        (construction) => construction.field === field
      );
    }

    setFilteredConstructions(filteredData);
  };

  useEffect(() => {
    handleFilter();
  }, [city, district, field, constructions]);

  const getStatusOptions = (status) => {
    if (status === "WAITING") {
      return ["ONGOING", "DONE", "STOP"];
    } else if (status === "STOP") {
      return ["ONGOING", "DONE"];
    } else if (status === "ONGOING") {
      return ["STOP", "DONE"];
    } else {
      return [];
    }
  };

  useEffect(() => {
    getStatusOptions();
  }, []);

  return (
    <Container style={{ width: "100%", margin: "2% auto" }}>
      <div style={{ display: "flex", marginBottom: "20px" }}>
        <FormGroup style={{ marginRight: "20px", width: "10%" }}>
          <Form.Select value={city} onChange={handleCityChange}>
            <option value="">시도</option>
            <option value="서울시">서울시</option>
            <option value="경기도">경기도</option>
          </Form.Select>
        </FormGroup>
        <FormGroup style={{ marginRight: "20px", width: "10%" }}>
          <Form.Select value={district} onChange={handleDistrictChange}>
            <option value="">시군구</option>
            {getDistrictOptions().map((option) => (
              <option key={option} value={option}>
                {option}
              </option>
            ))}
          </Form.Select>
        </FormGroup>
        <FormGroup style={{ marginRight: "20px", width: "10%" }}>
          <Form.Select value={field} onChange={handleFieldChange}>
            <option value="">공종</option>
            <option value="숏크리트">숏크리트</option>
            <option value="락볼트">락볼트</option>
            <option value="라이닝">라이닝</option>
          </Form.Select>
        </FormGroup>
      </div>
      <Table
        striped
        bordered
        hover
        style={{ textAlign: "center", tableLayout: "fixed" }}
      >
        <colgroup>
          <col style={{ width: "9%" }} />
          <col style={{ width: "13%" }} />
          <col style={{ width: "7%" }} />
          <col style={{ width: "20%" }} />
          <col style={{ width: "14%" }} />
          <col style={{ width: "10%" }} />
          <col style={{ width: "11%" }} />
          <col style={{ width: "16%" }} />
        </colgroup>
        <thead>
          <tr>
            <th>업체명</th>
            <th>공사명</th>
            <th>공종</th>
            <th>현장</th>
            <th>기간</th>
            <th>예산</th>
            <th>상태</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {filteredConstructions.map((construction, index) => (
            <tr key={index}>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {construction.contractorName}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {construction.name}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {construction.field}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {construction.city} {construction.district}{" "}
                {construction.addressDetail}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {construction.period}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {construction.budget}
              </td>
              <td
                style={{
                  verticalAlign: "middle",
                  fontSize: "80%",
                  padding: "0"
                }}
              >
                {construction.status === "END" ? (
                  <Button
                    variant="primary"
                    size="xs"
                    disabled
                    style={{ padding: "2% 0%" }}
                  >
                    평점: {construction.rating}
                  </Button>
                ) : construction.status === "DONE" ? (
                  <Dropdown style={{ display: "inline" }}>
                    <Dropdown.Toggle
                      variant="primary"
                      size="xs"
                      style={{ padding: "2% 0%" }}
                    >
                      업체 평점
                    </Dropdown.Toggle>
                    <Dropdown.Menu>
                      {["1", "2", "3", "4", "5"].map((rating) => (
                        <Dropdown.Item
                          key={rating}
                          onClick={() =>
                            handleRatingChange(construction, rating)
                          }
                        >
                          {rating}
                        </Dropdown.Item>
                      ))}
                    </Dropdown.Menu>
                  </Dropdown>
                ) : (
                  <Dropdown style={{ display: "inline" }}>
                    <Dropdown.Toggle
                      variant="primary"
                      size="xs"
                      style={{ padding: "2% 0%" }}
                    >
                      {construction.status === "WAITING"
                        ? "대기중"
                        : construction.status === "STOP"
                        ? "중단됨"
                        : construction.status === "ONGOING"
                        ? "진행중"
                        : construction.status === "DONE"
                        ? "완료됨"
                        : ""}
                    </Dropdown.Toggle>
                    <Dropdown.Menu>
                      {getStatusOptions(construction.status).map((option) => (
                        <Dropdown.Item
                          key={option}
                          onClick={() =>
                            handleStatusChange(construction, option)
                          }
                        >
                          {option === "WAITING"
                            ? "대기중"
                            : option === "STOP"
                            ? "중단됨"
                            : option === "ONGOING"
                            ? "진행중"
                            : option === "DONE"
                            ? "완료됨"
                            : ""}
                        </Dropdown.Item>
                      ))}
                    </Dropdown.Menu>
                  </Dropdown>
                )}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                <Button
                  variant="secondary"
                  size="xs"
                  onClick={() => handleShowConstructionInfoModal(construction)}
                  style={{ padding: "2% 0%" }}
                >
                  상세 정보
                </Button>
                <Button
                  variant="success"
                  size="xs"
                  onClick={() =>
                    handleShowConstructionChatListModal(construction)
                  }
                  style={{ padding: "2% 0%" }}
                >
                  민원 상담
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      {selectedConstruction && (
        <ConstructionInfoModal
          construction={selectedConstruction}
          showModal={showConstructionInfoModal}
          handleCloseModal={handleCloseModal}
        />
      )}
      {selectedConstruction && (
        <ConstructionChatListModal
          construction={selectedConstruction}
          showModal={showConstructionChatListModal}
          handleCloseModal={handleCloseModal}
        />
      )}
    </Container>
  );
};

export default ConstructionHistory;
