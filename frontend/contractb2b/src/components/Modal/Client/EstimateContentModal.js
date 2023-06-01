import React from "react";
import { Modal, Button } from "react-bootstrap";

const EstimateContentModal = ({ estimate, showModal, handleCloseModal }) => {
  return (
    <Modal show={showModal} onHide={handleCloseModal}>
      <Modal.Header closeButton>
        <Modal.Title>견적 상세</Modal.Title>
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
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleCloseModal}>
          닫기
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default EstimateContentModal;
