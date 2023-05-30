import React, { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";

const EstimateModal = ({ company, showModal, handleCloseModal }) => {
  const [name, setName] = useState("");
  const [field, setField] = useState("");
  const [addressDetail, setAddressDetail] = useState("");
  const [period, setPeriod] = useState("");
  const [budget, setBudget] = useState("");
  const [clientContent, setClientContent] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    const estimateData = {
      name,
      contractorId: company.id,
      city: company.city,
      district: company.district,
      addressDetail,
      field,
      period,
      budget,
      clientContent
    };
    fetch("http://localhost:8080/client/estimate", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`
      },
      body: JSON.stringify(estimateData)
    })
      .then((res) => {
        if (res.ok) {
          alert("견적이 발송되었습니다.");
        } else {
          alert("견적 발송에 실패하였습니다.");
        }
      })
      .catch((err) => {
        console.log(err);
      });

    setName("");
    setField("");
    setAddressDetail("");
    setPeriod("");
    setBudget("");
    setClientContent("");

    handleCloseModal();
  };

  return (
    <Modal show={showModal} onHide={handleCloseModal}>
      <Modal.Header closeButton>
        <Modal.Title>견적 발송</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form onSubmit={handleSubmit}>
          <Form.Group controlId="name">
            <Form.Label>이름</Form.Label>
            <Form.Control
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          </Form.Group>
          <Form.Group controlId="field">
            <Form.Label>공종</Form.Label>
            <Form.Control
              type="text"
              value={field}
              onChange={(e) => setField(e.target.value)}
            />
          </Form.Group>
          <Form.Group controlId="field">
            <Form.Label>상세주소</Form.Label>
            <Form.Control
              type="text"
              value={addressDetail}
              onChange={(e) => setAddressDetail(e.target.value)}
            />
          </Form.Group>
          <Form.Group controlId="period">
            <Form.Label>기간</Form.Label>
            <Form.Control
              type="text"
              value={period}
              onChange={(e) => setPeriod(e.target.value)}
            />
          </Form.Group>
          <Form.Group controlId="budget">
            <Form.Label>예산</Form.Label>
            <Form.Control
              type="text"
              value={budget}
              onChange={(e) => setBudget(e.target.value)}
            />
          </Form.Group>
          <Form.Group controlId="clientContent">
            <Form.Label>의뢰 내용</Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              value={clientContent}
              onChange={(e) => setClientContent(e.target.value)}
            />
          </Form.Group>
          <Button variant="success" type="button" onClick={handleSubmit}>
            견적 발송
          </Button>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleCloseModal}>
          닫기
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default EstimateModal;
