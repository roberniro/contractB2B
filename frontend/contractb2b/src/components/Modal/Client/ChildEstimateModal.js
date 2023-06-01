import React, { useEffect, useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";

const ChildEstimateModal = ({
  motherEstimate,
  showModal,
  handleCloseModal
}) => {
  const [period, setPeriod] = useState("");
  const [budget, setBudget] = useState("");
  const [clientContent, setClientContent] = useState("");

  useEffect(() => {
    setPeriod(motherEstimate.period);
    setBudget(motherEstimate.budget);
    setClientContent(motherEstimate.clientContent);
  }, [motherEstimate]);

  const handleSubmit = (e) => {
    e.preventDefault();
    const estimateData = {
      period,
      budget,
      clientContent
    };
    fetch(`http://localhost:8080/client/estimate/${motherEstimate.id}`, {
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
          setPeriod("");
          setBudget("");
          setClientContent("");
          handleCloseModal();
        } else {
          res.json().then((data) => {
            if (data.create_chiled_estimate) {
              alert(data.create_chiled_estimate);
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
        <Modal.Title>재건적 요청</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form onSubmit={handleSubmit}>
          <Form.Group controlId="name">
            <Form.Label>공사명</Form.Label>
            <Form.Control
              type="text"
              placeholder={motherEstimate.name}
              disabled
            />
          </Form.Group>
          <Form.Group controlId="field">
            <Form.Label className="mt-2">공종</Form.Label>
            <Form.Control value={motherEstimate.field} disabled></Form.Control>
          </Form.Group>
          <Form.Group controlId="site">
            <Form.Label
              className="mt-2"
              style={{ width: "50%", display: "block" }}
            >
              현장
            </Form.Label>
            <Form.Control
              className="mb-1"
              style={{ width: "50%", display: "inline" }}
              value={motherEstimate.city}
              disabled
            ></Form.Control>
            <Form.Control
              className="mb-1"
              style={{ width: "50%", display: "inline" }}
              value={motherEstimate.district}
              disabled
            ></Form.Control>
            <Form.Control
              type="text"
              value={motherEstimate.addressDetail}
              disabled
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
              placeholder={motherEstimate.clientContent}
              value={clientContent}
              onChange={(e) => setClientContent(e.target.value)}
            />
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="success" type="button" onClick={handleSubmit}>
          재견적 발송
        </Button>
        {/* <Button variant="secondary" onClick={handleCloseModal}>
          닫기
        </Button> */}
      </Modal.Footer>
    </Modal>
  );
};

export default ChildEstimateModal;
