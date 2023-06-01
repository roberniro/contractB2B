import React, { useEffect, useState } from "react";
import { Container, Form, Button, Table, FormGroup } from "react-bootstrap";
import ContractorModal from "../Modal/Client/ContractorModal";
import EstimateModal from "../Modal/Client/EstimateModal";

const NewEstimate = () => {
  const [companies, setCompanies] = useState([]);
  const [showContractorModal, setShowContractorModal] = useState(false);
  const [showEstimateModal, setShowEstimateModal] = useState(false);
  const [selectedCompany, setSelectedCompany] = useState(null);
  const [filteredCompanies, setFilteredCompanies] = useState(companies);
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
    setShowEstimateModal(false);
  };

  const getContractors = () => {
    fetch("http://localhost:8080/client/contractor", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${sessionStorage.getItem("token")}`
      }
    })
      .then((res) => {
        if (res.ok) {
          res.json().then((data) => {
            setCompanies(data.get_contractor);
            setFilteredCompanies(data.get_contractor);
          });
        } else {
          res.json().then((data) => {
            alert(data.error.get_contractor);
          });
        }
      })
      .catch((error) => {
        console.log(error);
      });
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
    setField(e.target.value);
    handleFilter();
  };

  const handleFilter = () => {
    let filteredData = companies;

    if (city) {
      filteredData = filteredData.filter((company) => company.city === city);
    }

    if (district) {
      filteredData = filteredData.filter(
        (company) => company.district === district
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

  useEffect(() => {
    handleFilter();
  }, [city, district, field, companies]);

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
          <col style={{ width: "20%" }} />
          <col style={{ width: "30%" }} />
          <col style={{ width: "20%" }} />
          <col style={{ width: "10%" }} />
          <col style={{ width: "20%" }} />
        </colgroup>
        <thead>
          <tr>
            <th>업체명</th>
            <th>주소</th>
            <th>연락처</th>
            <th>평점</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {filteredCompanies.map((company, index) => (
            <tr key={index}>
              <td style={{ verticalAlign: "middle" }}>{company.companyName}</td>
              <td style={{ verticalAlign: "middle" }}>
                {company.city} {company.district} {company.addressDetail}
              </td>
              <td style={{ verticalAlign: "middle" }}>{company.contact}</td>
              <td style={{ verticalAlign: "middle" }}>{company.rating}</td>
              <td style={{ verticalAlign: "middle" }}>
                <Button
                  variant="secondary"
                  size="xs"
                  style={{ padding: "2% 0%" }}
                  onClick={() => handleShowContractorModal(company)}
                >
                  업체 정보
                </Button>
                <Button
                  variant="success"
                  size="xs"
                  style={{ padding: "2% 0%" }}
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
      {selectedCompany && (
        <EstimateModal
          company={selectedCompany}
          showModal={showEstimateModal}
          handleCloseModal={handleCloseModal}
        />
      )}
    </Container>
  );
};

export default NewEstimate;
