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
import ExperienceModal from "../Modal/Contractor/ExperienceModal";

// 하청 경력 페이지 컴포넌트
const ConstructionHistory = () => {
  const [experiences, setExperiences] = useState([]);
  const [filteredExperiences, setFilteredExperiences] = useState([]);
  const [showNewExperienceModal, setShowNewExperienceModal] = useState(false);
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

  const handleShowNewExperienceModal = () => {
    setShowNewExperienceModal(true);
  };

  const handleCloseModal = () => {
    setShowNewExperienceModal(false);
  };

  const getExperience = () => {
    const BASE_URL = process.env.REACT_APP_BASE_URL;
    fetch(`${BASE_URL}/contractor/experience`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${sessionStorage.getItem("token")}`
      }
    })
      .then((res) => {
        if (res.ok) {
          res.json().then((data) => {
            setExperiences(data.get_experience);
            setFilteredExperiences(data.get_experience);
          });
        } else {
          res.json().then((data) => {
            alert(data.error.get_experience);
            setExperiences([]);
            setFilteredExperiences([]);
          });
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // const handleStatusChange = (construction, option) => {
  //   fetch(
  //     `http://localhost:8080/client/construction/${construction.id}/status`,
  //     {
  //       method: "PUT",
  //       headers: {
  //         "Content-Type": "application/json",
  //         Authorization: `Bearer ${sessionStorage.getItem("token")}`
  //       },
  //       body: JSON.stringify({
  //         status: option
  //       })
  //     }
  //   ).then((res) => {
  //     if (res.ok) {
  //       res.json().then((data) => {
  //         getExpereience();
  //       });
  //     } else {
  //       res.json().then((data) => {
  //         alert(data.error.change_construction_status);
  //       });
  //     }
  //   });
  // };

  useEffect(() => {
    if (!showNewExperienceModal) getExperience();
  }, [showNewExperienceModal]);

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
    let filteredData = experiences;

    if (city) {
      filteredData = filteredData.filter((experience) =>
        experience.site.include(city)
      );
    }

    if (district) {
      filteredData = filteredData.filter((experience) =>
        experience.site.include(district)
      );
    }

    if (field) {
      filteredData = filteredData.filter(
        (experience) => experience.field === field
      );
    }

    setFilteredExperiences(filteredData);
  };

  useEffect(() => {
    handleFilter();
  }, [city, district, field, experiences]);

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
        <Button
          variant="success"
          size="xs"
          onClick={() => handleShowNewExperienceModal()}
          style={{
            margin: "0",
            padding: "0",
            width: "10%"
          }}
        >
          경력 추가
        </Button>
      </div>
      <Table
        striped
        bordered
        hover
        style={{ textAlign: "center", tableLayout: "fixed" }}
      >
        <colgroup>
          <col style={{ width: "10%" }} />
          <col style={{ width: "12%" }} />
          <col style={{ width: "8%" }} />
          <col style={{ width: "20%" }} />
          <col style={{ width: "15%" }} />
          <col style={{ width: "15%" }} />
          <col style={{ width: "20%" }} />
        </colgroup>
        <thead>
          <tr>
            <th>공사명</th>
            <th>원청</th>
            <th>공종</th>
            <th>현장</th>
            <th>기간</th>
            <th>예산</th>
            <th>상세내용</th>
          </tr>
        </thead>
        <tbody>
          {filteredExperiences.map((experience, index) => (
            <tr key={index}>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {experience.name}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {experience.clientName}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {experience.field}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {experience.site}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {experience.period}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {experience.budget}
              </td>
              <td style={{ verticalAlign: "middle", fontSize: "80%" }}>
                {experience.content}
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      {showNewExperienceModal && (
        <ExperienceModal
          showModal={showNewExperienceModal}
          handleCloseModal={handleCloseModal}
        />
      )}
    </Container>
  );
};

export default ConstructionHistory;
