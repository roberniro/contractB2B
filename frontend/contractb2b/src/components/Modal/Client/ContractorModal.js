import React from "react";
import { Modal, Button, Table } from "react-bootstrap";

const ContractorModal = ({ company, showModal, handleCloseModal }) => {
  return (
    <Modal show={showModal} onHide={handleCloseModal} size="xl">
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
            <strong>평점:</strong> {Number(company.rating).toFixed(1)}
          </li>
          <li>
            <strong>경력:</strong>
            <Table className="mb-0">
              <colgroup>
                <col style={{ width: "15%" }} />
                <col style={{ width: "7%" }} />
                <col style={{ width: "10%" }} />
                <col style={{ width: "20%" }} />
                <col style={{ width: "18%" }} />
                <col style={{ width: "25%" }} />
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
                    <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                      {experience.name}
                    </td>
                    <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                      {experience.field}
                    </td>
                    <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                      {experience.budget}
                    </td>
                    <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                      {experience.site}
                    </td>
                    <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                      {experience.period}
                    </td>
                    <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                      {experience.content}
                    </td>
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
