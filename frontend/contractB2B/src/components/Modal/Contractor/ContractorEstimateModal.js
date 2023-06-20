import React, { useEffect, useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";

// 견적 상세 모달 컴포넌트
const ContractorEstimateModal = ({ estimate, showModal, handleCloseModal }) => {
  const [contractorContent, setContractorContent] = useState("");
  const [isChildEstimate, setIsChildEstimate] = useState(false);
  const [period, setPeriod] = useState("");
  const [budget, setBudget] = useState("");
  const [clientContent, setClientContent] = useState("");

  useEffect(() => {
    setPeriod(estimate.period);
    setBudget(estimate.budget);
    setContractorContent(estimate.clientContent);
  }, [estimate]);

  const handleSubmit = (e) => {
    const BASE_URL = process.env.REACT_APP_BASE_URL;
    e.preventDefault();
    const estimateData = {
      period,
      budget,
      contractorContent
    };
    fetch(`${BASE_URL}/contractor/estimate/${estimate.id}`, {
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
          setIsChildEstimate(false);
          setPeriod("");
          setBudget("");
          setClientContent("");
          handleCloseModal();
        } else {
          res.json().then((data) => {
            let errorMessage = "";
            for (const key in data.error) {
              errorMessage += `${data.error[key]}\n`;
            }
            alert(errorMessage);
          });
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const handleAcceptEstimate = () => {
    const BASE_URL = process.env.REACT_APP_BASE_URL;
    fetch(`${BASE_URL}/contractor/estimate/${estimate.id}/accept`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${sessionStorage.getItem("token")}`
      }
    })
      .then((res) => {
        if (res.ok) {
          alert("견적이 수락되었습니다.");
          handleCloseModal();
        } else {
          res.json().then((data) => {
            alert(data.error.accept_estimate);
          });
        }
      })
      .catch((err) => console.log(err));
  };

  return (
    <Modal show={showModal} onHide={handleCloseModal}>
      <Modal.Header closeButton>
        <Modal.Title>
          {isChildEstimate ? "재견적 요청" : "견적 상세"}
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {isChildEstimate ? (
          <>
            <Form onSubmit={handleSubmit}>
              <Form.Group controlId="name">
                <Form.Label>공사명</Form.Label>
                <Form.Control
                  type="text"
                  placeholder={estimate.name}
                  disabled
                />
              </Form.Group>
              <Form.Group controlId="field">
                <Form.Label className="mt-2">공종</Form.Label>
                <Form.Control value={estimate.field} disabled></Form.Control>
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
                  value={estimate.city}
                  disabled
                ></Form.Control>
                <Form.Control
                  className="mb-1"
                  style={{ width: "50%", display: "inline" }}
                  value={estimate.district}
                  disabled
                ></Form.Control>
                <Form.Control
                  type="text"
                  value={estimate.addressDetail}
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
                <Form.Label className="mt-2">요청 내용</Form.Label>
                <Form.Control
                  as="textarea"
                  rows={3}
                  placeholder={contractorContent}
                  value={contractorContent}
                  onChange={(e) => setContractorContent(e.target.value)}
                />
              </Form.Group>
            </Form>
          </>
        ) : (
          <>
            <h5>원청 요구사항</h5>
            <p>{estimate.clientContent}</p>
            {estimate.contractorContent ? (
              <>
                <h5>하청 요구사항</h5>
                <p>{estimate.contractorContent}</p>
              </>
            ) : (
              <></>
            )}
          </>
        )}
      </Modal.Body>
      <Modal.Footer>
        {estimate.contractorContent || estimate.estimateStatus !== "WAITING" ? (
          <Button variant="secondary" onClick={handleCloseModal}>
            닫기
          </Button>
        ) : isChildEstimate ? (
          <>
            <Button
              variant="secondary"
              onClick={() => setIsChildEstimate(false)}
            >
              뒤로가기
            </Button>
            <Button variant="success" onClick={handleSubmit}>
              재견적 요청
            </Button>
          </>
        ) : (
          <>
            <Button
              variant="secondary"
              onClick={() => setIsChildEstimate(true)}
            >
              재견적 요청
            </Button>
            <Button variant="success" onClick={handleAcceptEstimate}>
              견적 수락
            </Button>
          </>
        )}
      </Modal.Footer>
    </Modal>
  );
};

export default ContractorEstimateModal;
