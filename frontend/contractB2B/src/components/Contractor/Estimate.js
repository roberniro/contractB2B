import React, { useEffect, useState } from "react";
import { Container, Form, Button, Table, FormGroup } from "react-bootstrap";
import ContractorEstimateModal from "../Modal/Contractor/ContractorEstimateModal";

// 하청 견적 페이지 컴포넌트
const Estimate = () => {
  const [estimates, setEstimates] = useState([]);
  const [showContractorEstimateModal, setShowContractorEstimateModal] =
    useState(false);
  const [selectedEstimate, setSelectedEstimate] = useState(null);
  const [filteredEstimates, setFilteredEstimates] = useState(estimates);
  const [city, setCity] = useState("");
  const [district, setDistrict] = useState("");
  const [field, setField] = useState("");

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

  const handleShowContractorEstimateModal = (estimate) => {
    setSelectedEstimate(estimate);
    setShowContractorEstimateModal(true);
  };

  const handleCloseModal = () => {
    setSelectedEstimate(null);
    setShowContractorEstimateModal(false);
  };

  const getEstimate = () => {
    const BASE_URL = process.env.REACT_APP_BASE_URL;
    fetch(`${BASE_URL}/contractor/estimate`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${sessionStorage.getItem("token")}`
      }
    })
      .then((res) => {
        if (res.ok) {
          res.json().then((data) => {
            setEstimates(data.get_estimate);
            setFilteredEstimates(data.get_estimate);
          });
        } else {
          res.json().then((data) => {
            alert(data.error.get_estimate);
          });
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const handleRejectEstimate = (estimateId) => {
    const BASE_URL = process.env.REACT_APP_BASE_URL;
    fetch(`${BASE_URL}/contractor/estimate/${estimateId}/reject`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${sessionStorage.getItem("token")}`
      }
    })
      .then((res) => {
        if (res.ok) {
          alert("견적이 거절되었습니다.");
          getEstimate();
        } else {
          res.json().then((data) => {
            alert(data.error.reject_estimate);
          });
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  useEffect(() => {
    if (!showContractorEstimateModal) getEstimate();
  }, [showContractorEstimateModal]);

  const handleCityChange = (e) => {
    setCity(e.target.value);
    handleFilter();
  };

  const handleDistrictChange = (e) => {
    setDistrict(e.target.value);
    handleFilter();
  };

  const handleFieldChange = (e) => {
    setField(e.target.value);
    handleFilter();
  };

  const handleFilter = () => {
    let filteredData = estimates;

    if (city) {
      filteredData = filteredData.filter((estimate) => estimate.city === city);
    }

    if (district) {
      filteredData = filteredData.filter(
        (estimate) => estimate.district === district
      );
    }

    if (field) {
      filteredData = filteredData.filter(
        (estimate) => estimate.field === field
      );
    }

    setFilteredEstimates(filteredData);
  };

  useEffect(() => {
    handleFilter();
  }, [city, district, field, estimates]);

  return (
    <Container style={{ width: "100%", margin: "2% auto" }}>
      <div style={{ display: "flex", marginBottom: "20px" }}>
        <FormGroup style={{ marginRight: "20px", width: "10%" }}>
          <Form.Select value={city} onChange={handleCityChange}>
            <option value="">시도</option>
            <option value="서울시">서울시</option>
            <option value="경기도">경기도</option>
          </Form.Select>
        </FormGroup>
        <FormGroup style={{ marginRight: "20px", width: "10%" }}>
          <Form.Select value={district} onChange={handleDistrictChange}>
            <option value="">시군구</option>
            {getDistrictOptions().map((option) => (
              <option key={option} value={option}>
                {option}
              </option>
            ))}
          </Form.Select>
        </FormGroup>
        <FormGroup style={{ marginRight: "20px", width: "10%" }}>
          <Form.Select value={field} onChange={handleFieldChange}>
            <option value="">공종</option>
            <option value="숏크리트">숏크리트</option>
            <option value="락볼트">락볼트</option>
            <option value="라이닝">라이닝</option>
            {/* Add more options as needed */}
          </Form.Select>
        </FormGroup>
      </div>
      <Table
        striped
        bordered
        hover
        style={{ textAlign: "center", tableLayout: "fixed" }}
      >
        <colgroup>
          <col style={{ width: "10%" }} />
          <col style={{ width: "15%" }} />
          <col style={{ width: "7%" }} />
          <col style={{ width: "20%" }} />
          <col style={{ width: "15%" }} />
          <col style={{ width: "10%" }} />
          <col style={{ width: "7%" }} />
          <col style={{ width: "16%" }} />
        </colgroup>
        <thead>
          <tr>
            <th>업체명</th>
            <th>공사명</th>
            <th>공종</th>
            <th>현장</th>
            <th>기간</th>
            <th>예산</th>
            <th>상태</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {filteredEstimates.map((estimate, index) => (
            <tr key={index}>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {estimate.contractorName}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {estimate.name}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {estimate.field}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {estimate.city} {estimate.district} {estimate.addressDetail}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {estimate.period}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {estimate.budget}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {estimate.estimateStatus === "WAITING"
                  ? "응답 대기중"
                  : estimate.estimateStatus === "PENDING"
                  ? "수락 대기중"
                  : estimate.estimateStatus === "ACCEPTED"
                  ? "수락됨"
                  : "거절됨"}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                <Button
                  variant="secondary"
                  size="xs"
                  onClick={() => handleRejectEstimate(estimate.id)}
                  style={{ padding: "2% 0%" }}
                  disabled={
                    estimate.estimateStatus === "ACCEPTED" ||
                    estimate.estimateStatus === "REJECTED" ||
                    estimate.estimateStatus === "PENDING"
                  }
                >
                  견적 거절
                </Button>
                <Button
                  variant="success"
                  size="xs"
                  onClick={() => handleShowContractorEstimateModal(estimate)}
                  style={{ padding: "2% 0%" }}
                >
                  견적 상세
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      {selectedEstimate && (
        <ContractorEstimateModal
          estimate={selectedEstimate}
          showModal={showContractorEstimateModal}
          handleCloseModal={() => setShowContractorEstimateModal(false)}
        />
      )}
    </Container>
  );
};

export default Estimate;
