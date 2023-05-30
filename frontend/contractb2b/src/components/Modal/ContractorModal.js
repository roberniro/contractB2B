import React from "react";
import { Modal, Button, Table } from "react-bootstrap";

const ContractorModal = ({ company, showModal, handleCloseModal }) => {
  return (
    <Modal show={showModal} onHide={handleCloseModal}>
      <Modal.Header closeButton>
        <Modal.Title>업체 정보</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p>Company Name: {company.name}</p>
        <p>
          Address: {company.city} {company.district} {company.addressDetail}
        </p>
        <p>Phone Number: {company.phoneNumber}</p>
        <p>Rating: {company.rating}</p>
        <p>
          <Table>
            <colgroup>
              <col style={{ width: "10%" }} />
              <col style={{ width: "10%" }} />
              <col style={{ width: "10%" }} />
              <col style={{ width: "20%" }} />
              <col style={{ width: "20%" }} />
              <col style={{ width: "30%" }} />
            </colgroup>
            <thead>
              <tr>
                <th>Name</th>
                <th>Field</th>
                <th>Budget</th>
                <th>Site</th>
                <th>Period</th>
                <th>Content</th>
              </tr>
            </thead>
            <tbody>
              {company.experienceDtoList.map((experience, index) => (
                <tr key={index}>
                  <td>{experience.name}</td>
                  <td>{experience.field}</td>
                  <td>{experience.budget}</td>
                  <td>{experience.site}</td>
                  <td>{experience.period}</td>
                  <td>{experience.content}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </p>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleCloseModal}>
          닫기
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ContractorModal;
