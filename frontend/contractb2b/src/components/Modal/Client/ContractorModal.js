import React from "react";
import { Modal, Button, Table } from "react-bootstrap";

const ContractorModal = ({ company, showModal, handleCloseModal }) => {
  return (
    <Modal show={showModal} onHide={handleCloseModal} size="lg">
      <Modal.Header closeButton>
        <Modal.Title>업체 정보</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <ul className="list-unstyled">
          <li>
            <strong>사명:</strong> {company.companyName}
          </li>
          <li>
            <strong>주소:</strong> {company.city} {company.district}{" "}
            {company.addressDetail}
          </li>
          <li>
            <strong>연락처:</strong> {company.contact}
          </li>
          <li>
            <strong>평점:</strong> {company.rating}
          </li>
          <li>
            <strong>경력:</strong>
            <Table className="mb-0">
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
          </li>
        </ul>
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
