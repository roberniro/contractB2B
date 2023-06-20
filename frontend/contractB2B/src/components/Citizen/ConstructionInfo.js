import React, { useEffect, useState } from "react";
import {
  Container,
  Form,
  Button,
  Table,
  FormGroup,
  Dropdown
} from "react-bootstrap";

import ConstructionInfoModal from "../Modal/Client/ConstructionInfoModal";
import NewComplaintModal from "../Modal/Citizen/NewComplaintModal";
import ConstructionChatModal from "../Modal/Citizen/ConstructionChatModal";

// 시민 공사정보 조회 페이지 컴포넌트
const ConstructionInfo = () => {
  const [constructions, setConstructions] = useState([]);
  const [selectedConstruction, setSelectedConstruction] = useState(null);
  const [filteredConstructions, setFilteredConstructions] =
    useState(constructions);
  const [showConstructionInfoModal, setShowConstructionInfoModal] =
    useState(false);
  const [showComplaintModal, setShowComplaintModal] = useState(false);
  const [showChatModal, setShowChatModal] = useState(false);
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

  const handleShowConstructionInfoModal = (construction) => {
    setSelectedConstruction(construction);
    setShowConstructionInfoModal(true);
  };

  const handleShowComplaintModal = (construction) => {
    setSelectedConstruction(construction);
    setShowComplaintModal(true);
  };

  const handleShowChatModal = (construction) => {
    setSelectedConstruction(construction);
    setShowChatModal(true);
  };

  const handleCloseModal = () => {
    setSelectedConstruction(null);
    setShowConstructionInfoModal(false);
    setShowComplaintModal(false);
  };

  const getConstruction = () => {
    const BASE_URL = process.env.REACT_APP_BASE_URL;
    fetch(`${BASE_URL}/citizen/construction`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${sessionStorage.getItem("token")}`
      }
    })
      .then((res) => {
        if (res.ok) {
          res.json().then((data) => {
            setConstructions(data.get_construction);
            setFilteredConstructions(data.get_construction);
          });
        } else {
          res.json().then((data) => {
            alert(data.error.get_construction);
          });
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  useEffect(() => {
    if (!showConstructionInfoModal) getConstruction();
  }, [showConstructionInfoModal]);

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
    let filteredData = constructions;

    if (city) {
      filteredData = filteredData.filter(
        (construction) => construction.city === city
      );
    }

    if (district) {
      filteredData = filteredData.filter(
        (construction) => construction.district === district
      );
    }

    if (field) {
      filteredData = filteredData.filter(
        (construction) => construction.field === field
      );
    }

    setFilteredConstructions(filteredData);
  };

  useEffect(() => {
    handleFilter();
  }, [city, district, field, constructions]);

  const getStatusOptions = (status) => {
    if (status === "WAITING") {
      return ["ONGOING", "DONE", "STOP"];
    } else if (status === "STOP") {
      return ["ONGOING", "DONE"];
    } else if (status === "ONGOING") {
      return ["STOP", "DONE"];
    } else {
      return [];
    }
  };

  useEffect(() => {
    getStatusOptions();
  }, []);

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
          {filteredConstructions.map((construction, index) => (
            <tr key={index}>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {construction.contractorName}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {construction.name}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {construction.field}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {construction.city} {construction.district}{" "}
                {construction.addressDetail}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {construction.period}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {construction.budget}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {construction.status === "WAITING"
                  ? "대기중"
                  : construction.status === "STOP"
                  ? "중단됨"
                  : construction.status === "ONGOING"
                  ? "진행중"
                  : construction.status === "DONE"
                  ? "완료됨"
                  : "완료됨"}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                <Button
                  variant="secondary"
                  size="xs"
                  onClick={() => handleShowConstructionInfoModal(construction)}
                  style={{ padding: "2% 0%" }}
                >
                  상세 정보
                </Button>
                {/* <Button
                  variant="success"
                  size="xs"
                  onClick={() => handleShowComplaintModal(construction)}
                  style={{ padding: "2% 0%" }}
                  disabled={
                    construction.status === "WAITING" ||
                    construction.status === "END"
                  }
                >
                  민원 제기
                </Button> */}
                <Button
                  variant="success"
                  size="xs"
                  onClick={() => handleShowChatModal(construction)}
                  style={{ padding: "2% 0%" }}
                >
                  민원 상담
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      {selectedConstruction && (
        <ConstructionInfoModal
          construction={selectedConstruction}
          showModal={showConstructionInfoModal}
          handleCloseModal={handleCloseModal}
        />
      )}
      {selectedConstruction && (
        <NewComplaintModal
          construction={selectedConstruction}
          showModal={showComplaintModal}
          handleCloseModal={handleCloseModal}
        />
      )}
      {selectedConstruction && (
        <ConstructionChatModal
          construction={selectedConstruction}
          showModal={showChatModal}
          handleCloseModal={handleCloseModal}
        />
      )}
    </Container>
  );
};

export default ConstructionInfo;
