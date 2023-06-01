import React, { useState, useEffect } from "react";
import { Container, Form, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./Join.css";

const Join = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");
  const [nip, setNip] = useState("");
  const [contact, setContact] = useState("");
  const [city, setCity] = useState("");
  const [district, setDistrict] = useState("");
  const [addressDetail, setAddressDetail] = useState("");
  const [role, setRole] = useState("");
  const navigate = useNavigate();

  const [isSignUpSuccess, setIsSignUpSuccess] = useState(false);

  useEffect(() => {
    if (isSignUpSuccess) {
      navigate("/login");
    }
  }, [isSignUpSuccess, navigate]);

  const checkNull = () => {
    if (username === "" || password === "" || name === "") {
      return false;
    } else if (
      role !== "CITIZEN" &&
      (nip === "" || contact === "" || addressDetail === "")
    ) {
      return false;
    } else {
      return true;
    }
  };

  const handleJoin = () => {
    if (!checkNull()) {
      alert("필수 값을 입력해 주세요");
      return;
    }

    fetch("http://localhost:8080/user", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(
        role === "CITIZEN"
          ? {
              username,
              password,
              name,
              role,
              nip: "0000000000",
              contact: "0000000000",
              addressDetail: "주소없음",
              city: "주소없음",
              district: "주소없음"
            }
          : {
              username,
              password,
              name,
              nip,
              contact,
              city,
              district,
              addressDetail,
              role
            }
      )
    }).then((response) => {
      if (response.ok) {
        window.alert("회원가입이 완료되었습니다.");
        setIsSignUpSuccess(true);
        return response.json();
      } else {
        response.json().then((data) => {
          let errorMessage = "";
          for (const key in data.error) {
            errorMessage += `${data.error[key]}\n`;
          }
          alert(errorMessage || response.statusText);
        });
      }
    });
  };

  const handleUsernameChange = (e) => {
    setUsername(e.target.value);
  };

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
  };

  const handleNameChange = (e) => {
    setName(e.target.value);
  };

  const handleNipChange = (e) => {
    setNip(e.target.value);
  };

  const handleContactChange = (e) => {
    setContact(e.target.value);
  };

  const handleCityChange = (e) => {
    setCity(e.target.value);
  };

  const handleDistrictChange = (e) => {
    setDistrict(e.target.value);
  };

  const handleAddressDetailChange = (e) => {
    setAddressDetail(e.target.value);
  };

  const handleRoleChange = (e) => {
    setRole(e.target.value);
  };

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

  return (
    <div className="join-wrapper">
      <Container className="join-container">
        <h2>회원가입</h2>
        <Form onSubmit={handleJoin}>
          <Form.Group controlId="role">
            <Form.Control as="select" value={role} onChange={handleRoleChange}>
              <option value="">역할 선택</option>
              <option value="CLIENT">클라이언트</option>
              <option value="CONTRACTOR">계약자</option>
              <option value="CITIZEN">일반 사용자</option>
            </Form.Control>
          </Form.Group>

          <Form.Group controlId="username">
            <Form.Control
              type="text"
              placeholder="아이디 입력"
              value={username}
              onChange={handleUsernameChange}
              required
            />
          </Form.Group>

          <Form.Group controlId="password">
            <Form.Control
              type="password"
              placeholder="비밀번호 입력"
              value={password}
              onChange={handlePasswordChange}
              required
            />
          </Form.Group>

          <Form.Group controlId="companyName">
            <Form.Control
              type="text"
              placeholder={role === "CITIZEN" ? "이름 입력" : "회사명 입력"}
              value={name}
              onChange={handleNameChange}
              required
            />
          </Form.Group>

          {role !== "CITIZEN" && (
            <>
              <Form.Group controlId="nip">
                <Form.Control
                  type="text"
                  placeholder="사업자 등록번호 입력"
                  value={nip}
                  onChange={handleNipChange}
                  required
                />
              </Form.Group>

              <Form.Group controlId="contact">
                <Form.Control
                  type="tel"
                  placeholder="전화번호 입력"
                  value={contact}
                  onChange={handleContactChange}
                  required
                />
              </Form.Group>

              <div className="address-inputs">
                <div className="dropdown">
                  <Form.Control
                    as="select"
                    id="city"
                    value={city}
                    onChange={handleCityChange}
                    required
                  >
                    <option value="">시/도</option>
                    <option value="서울시">서울시</option>
                    <option value="경기도">경기도</option>
                  </Form.Control>
                </div>
                <div className="dropdown">
                  <Form.Control
                    as="select"
                    id="district"
                    value={district}
                    onChange={handleDistrictChange}
                    required
                  >
                    <option value="">시/군/구</option>
                    {getDistrictOptions().map((option) => (
                      <option key={option} value={option}>
                        {option}
                      </option>
                    ))}
                  </Form.Control>
                </div>
              </div>

              <Form.Group controlId="addressDetail">
                <Form.Control
                  type="text"
                  placeholder="상세주소 입력"
                  value={addressDetail}
                  onChange={handleAddressDetailChange}
                  required
                />
              </Form.Group>
            </>
          )}

          <Button variant="success" type="button" onClick={handleJoin} block>
            회원가입
          </Button>
        </Form>
      </Container>
    </div>
  );
};

export default Join;
