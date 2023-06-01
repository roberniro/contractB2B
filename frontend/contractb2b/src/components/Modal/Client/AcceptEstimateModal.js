import React, { useState } from "react";
import { Form, Modal, Button } from "react-bootstrap";
import { json } from "react-router-dom";

const AcceptEstimateModal = ({ estimate, showModal, handleCloseModal }) => {
  const [reason, setReason] = useState("");

  const handleAcceptEstimate = () => {
    fetch(`http://localhost:8080/client/estimate/${estimate.id}/accept`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${sessionStorage.getItem("token")}`
      },
      body: JSON.stringify({ reason })
    })
      .then((res) => {
        if (res.ok) {
          alert("견적이 수락되었습니다.");
          handleCloseModal();
        } else {
          res.json().then((data) => {
            if (data.accept_estimate) {
              alert(data.accept_estimate);
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
      .catch((err) => console.log(err));
  };

  return (
    <Modal show={showModal} onHide={handleCloseModal}>
      <Modal.Header closeButton>
        <Modal.Title>견적 수락</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <h5>원청 요구사항</h5>
        <p>{estimate.clientContent}</p>
        {estimate.contractorContent && (
          <>
            <h5>하청 요구사항</h5>
            <p>{estimate.contractorContent}</p>
          </>
        )}
        <h5>업체 선정 이유를 기록해주세요</h5>
        <Form.Control
          as="textArea"
          rows={3}
          value={reason}
          onChange={(e) => setReason(e.target.value)}
        ></Form.Control>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="success" onClick={handleAcceptEstimate}>
          견적 수락
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default AcceptEstimateModal;
