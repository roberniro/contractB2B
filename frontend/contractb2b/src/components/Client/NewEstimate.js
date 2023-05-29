import React, { useState } from "react";
import { Container, Form, Button, Table, FormGroup } from "react-bootstrap";

const NewEstimate = () => {
  const [companies, setCompanies] = useState([
    {
      companyName: "Company 1",
      address: "Address 1",
      phoneNumber: "1234567890"
    },
    {
      companyName: "Company 2",
      address: "Address 2",
      phoneNumber: "9876543210"
    }
  ]);

  const [filteredCompanies, setFilteredCompanies] = useState(companies);
  const [city, setCity] = useState("");
  const [district, setDistrict] = useState("");
  const [trade, setTrade] = useState("");
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

  const handleCityChange = (e) => {
    setCity(e.target.value);
  };

  const handleDistrictChange = (e) => {
    setDistrict(e.target.value);
  };

  const handleTradeChange = (e) => {
    setTrade(e.target.value);
  };

  const handleFilter = () => {
    let filteredData = companies;

    if (city) {
      filteredData = filteredData.filter((company) =>
        company.address.includes(city)
      );
    }

    if (district) {
      filteredData = filteredData.filter((company) =>
        company.address.includes(district)
      );
    }

    if (trade) {
      filteredData = filteredData.filter((company) =>
        company.trade.includes(trade)
      );
    }

    setFilteredCompanies(filteredData);
  };

  const handleSendEstimate = (companyName) => {
    // Handle sending estimate for the selected company
    console.log(`Sending estimate for ${companyName}`);
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
          <Form.Control as="select" value={trade} onChange={handleTradeChange}>
            <option value="">공종</option>
            <option value="Construction">건설</option>
            <option value="Electrical">전기</option>
            <option value="Plumbing">배관</option>
            {/* Add more options as needed */}
          </Form.Control>
        </FormGroup>
      </div>
      <Table striped bordered hover style={{ textAlign: "center" }}>
        <thead>
          <tr>
            <th>업체명</th>
            <th>상세주소</th>
            <th>전화번호</th>
            <th>견적발송</th>
          </tr>
        </thead>
        <tbody>
          {filteredCompanies.map((company) => (
            <tr key={company.companyName}>
              <td>{company.companyName}</td>
              <td>{company.address}</td>
              <td>{company.phoneNumber}</td>
              <td>
                <Button
                  variant="secondary"
                  size="sm" // Set the button size to "sm" for small
                  onClick={() => handleSendEstimate(company.companyName)}
                >
                  견적 발송
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </Container>
  );
};
export default NewEstimate;
