import React, { useEffect, useState } from "react";
import { Container, Form, Button, Table, FormGroup } from "react-bootstrap";
import ContractorModal from "../Modal/ContractorModal";

const NewEstimate = () => {
  const [companies, setCompanies] = useState([]);
  const [showContractorModal, setShowContractorModal] = useState(false);
  const [showEstimateModal, setShowEstimateModal] = useState(false);
  const [selectedCompany, setSelectedCompany] = useState(null);
  const [filteredCompanies, setFilteredCompanies] = useState(companies);
  const [city, setCity] = useState("");
  const [district, setDistrict] = useState("");
  const [field, setFiled] = useState("");
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

  const handleShowContractorModal = (company) => {
    setSelectedCompany(company);
    setShowContractorModal(true);
  };

  const handleShowEstimateModal = (company) => {
    setSelectedCompany(company);
    setShowEstimateModal(true);
  };

  const handleCloseModal = () => {
    setShowContractorModal(false);
    setShowContractorModal(false);
  };

  const getContractors = () => {
    fetch("http://localhost:8080/client/contractor", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    })
      .then((res) => res.json())
      .then((res) => {
        setCompanies(res.getContractors);
        setFilteredCompanies(res.getContractors);
      })
      .catch((err) => console.log(err));
  };

  useEffect(() => {
    getContractors();
  }, []);

  const handleCityChange = (e) => {
    setCity(e.target.value);
    handleFilter();
  };

  const handleDistrictChange = (e) => {
    setDistrict(e.target.value);
    handleFilter();
  };

  const handleFieldChange = (e) => {
    setFiled(e.target.value);
    handleFilter();
  };

  const handleFilter = () => {
    let filteredData = companies;

    if (city) {
      filteredData = filteredData.filter((company) =>
        company.city.includes(city)
      );
    }

    if (district) {
      filteredData = filteredData.filter((company) =>
        company.district.includes(district)
      );
    }

    if (field) {
      filteredData = filteredData.filter((company) => {
        let experiences = company.experienceDtoList;
        for (let i = 0; i < experiences.length; i++) {
          if (experiences[i].field === field) {
            return true;
          }
        }
        return false;
      });
    }

    setFilteredCompanies(filteredData);
  };

  return (
    <Container style={{ width: "100%" }}>
      <div style={{ display: "flex", marginBottom: "20px" }}>
        <FormGroup style={{ marginRight: "20px", width: "10%" }}>
          <Form.Control as="select" value={city} onChange={handleCityChange}>
            <option value="">시도</option>
            <option value="서울시">서울시</option>
            <option value="경기도">경기도</option>
          </Form.Control>
        </FormGroup>
        <FormGroup style={{ marginRight: "20px", width: "10%" }}>
          <Form.Control
            as="select"
            value={district}
            onChange={handleDistrictChange}
          >
            <option value="">시군구</option>
            {getDistrictOptions().map((option) => (
              <option key={option} value={option}>
                {option}
              </option>
            ))}
          </Form.Control>
        </FormGroup>
        <FormGroup style={{ marginRight: "20px", width: "10%" }}>
          <Form.Control as="select" value={field} onChange={handleFieldChange}>
            <option value="">공종</option>
            <option value="라이닝">숏크리트</option>
            <option value="라이닝">록볼트</option>
            <option value="라이닝">라이닝</option>
            {/* Add more options as needed */}
          </Form.Control>
        </FormGroup>
      </div>
      <Table
        striped
        bordered
        hover
        style={{ textAlign: "center", tableLayout: "fixed" }}
      >
        <colgroup>
          <col style={{ width: "20%" }} />
          <col style={{ width: "30%" }} />
          <col style={{ width: "30%" }} />
          <col style={{ width: "20%" }} />
        </colgroup>
        <thead>
          <tr>
            <th>업체명</th>
            <th>상세주소</th>
            <th>전화번호</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {filteredCompanies.map((company, index) => (
            <tr key={index}>
              <td style={{ verticalAlign: "middle" }}>{company.name}</td>
              <td style={{ verticalAlign: "middle" }}>
                {company.addressDetail}
              </td>
              <td style={{ verticalAlign: "middle" }}>{company.contact}</td>
              <td style={{ verticalAlign: "middle" }}>
                <Button
                  variant="secondary"
                  size="sm"
                  onClick={() => handleShowContractorModal(company)}
                >
                  업체 정보
                </Button>
                <Button
                  variant="success"
                  size="sm"
                  onClick={() => handleShowEstimateModal(company)}
                >
                  견적 발송
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      {selectedCompany && (
        <ContractorModal
          company={selectedCompany}
          showModal={showContractorModal}
          handleCloseModal={handleCloseModal}
        />
      )}
    </Container>
  );
};
export default NewEstimate;
