import React, { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";

// 원청 견적 요청 모달 컴포넌트
const EstimateModal = ({ company, showModal, handleCloseModal }) => {
  const [name, setName] = useState("");
  const [field, setField] = useState("");
  const [addressDetail, setAddressDetail] = useState("");
  const [period, setPeriod] = useState("");
  const [budget, setBudget] = useState("");
  const [city, setCity] = useState("");
  const [district, setDistrict] = useState("");
  const [clientContent, setClientContent] = useState("");

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

  const handleSubmit = (e) => {
    const BASE_URL = process.env.REACT_APP_BASE_URL;
    e.preventDefault();
    const estimateData = {
      name,
      contractorId: company.id,
      city,
      district,
      addressDetail,
      field,
      period,
      budget,
      clientContent
    };
    fetch(`${BASE_URL}/client/estimate`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${sessionStorage.getItem("token")}`
      },
      body: JSON.stringify(estimateData)
    })
      .then((res) => {
        if (res.ok) {
          alert("견적이 발송되었습니다.");
          setName("");
          setField("");
          setAddressDetail("");
          setPeriod("");
          setBudget("");
          setClientContent("");
          handleCloseModal();
        } else {
          res.json().then((data) => {
            if (data.create_estimate) {
              alert(data.error.create_estimate);
            } else {
              let errorMessage = "";
              for (const key in data.error) {
                errorMessage += `${data.error[key]}\n`;
              }
              alert(errorMessage);
            }
          });
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <Modal show={showModal} onHide={handleCloseModal}>
      <Modal.Header closeButton>
        <Modal.Title>견적 발송</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form onSubmit={handleSubmit}>
          <Form.Group controlId="name">
            <Form.Label>공사명</Form.Label>
            <Form.Control
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          </Form.Group>
          <Form.Group controlId="field">
            <Form.Label className="mt-2">공종</Form.Label>
            <Form.Select
              value={field}
              onChange={(e) => setField(e.target.value)}
            >
              <option value="">공종</option>
              <option value="숏크리트">숏크리트</option>
              <option value="락볼트">락볼트</option>
              <option value="라이닝">라이닝</option>
            </Form.Select>
          </Form.Group>
          <Form.Group controlId="site">
            <Form.Label
              className="mt-2"
              style={{ width: "50%", display: "block" }}
            >
              현장
            </Form.Label>
            <Form.Select
              className="mb-1"
              style={{ width: "50%", display: "inline" }}
              value={city}
              onChange={(e) => setCity(e.target.value)}
            >
              <option value="">시/도</option>
              <option value="서울시">서울시</option>
              <option value="경기도">경기도</option>
            </Form.Select>
            <Form.Select
              className="mb-1"
              style={{ width: "50%", display: "inline" }}
              value={district}
              onChange={(e) => setDistrict(e.target.value)}
            >
              <option value="">시/군/구</option>
              {getDistrictOptions().map((district, index) => (
                <option key={index} value={district}>
                  {district}
                </option>
              ))}
            </Form.Select>
            <Form.Control
              type="text"
              value={addressDetail}
              placeholder="상세 주소"
              onChange={(e) => setAddressDetail(e.target.value)}
            />
          </Form.Group>
          <Form.Group controlId="period">
            <Form.Label className="mt-2">기간</Form.Label>
            <Form.Control
              type="text"
              value={period}
              onChange={(e) => setPeriod(e.target.value)}
            />
          </Form.Group>
          <Form.Group controlId="budget">
            <Form.Label className="mt-2">예산</Form.Label>
            <Form.Control
              type="text"
              value={budget}
              onChange={(e) => setBudget(e.target.value)}
            />
          </Form.Group>
          <Form.Group controlId="clientContent">
            <Form.Label className="mt-2">의뢰 내용</Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              value={clientContent}
              onChange={(e) => setClientContent(e.target.value)}
            />
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="success" type="button" onClick={handleSubmit}>
          견적 요청
        </Button>
        {/* <Button variant="secondary" onClick={handleCloseModal}>
          닫기
        </Button> */}
      </Modal.Footer>
    </Modal>
  );
};

export default EstimateModal;
