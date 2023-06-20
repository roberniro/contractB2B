import React from "react";
import { Modal, Button } from "react-bootstrap";

// 원청 공사 상세 모달 컴포넌트
const ConstructionInfoModal = ({
  construction,
  showModal,
  handleCloseModal
}) => {
  return (
    <Modal show={showModal} onHide={handleCloseModal}>
      <Modal.Header closeButton>
        <Modal.Title>공사 상세</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <h5>계약 세부</h5>
        <p>{construction.contractContent}</p>
        <h5>업체 선정 이유</h5>
        <p>{construction.reason}</p>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleCloseModal}>
          닫기
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ConstructionInfoModal;
