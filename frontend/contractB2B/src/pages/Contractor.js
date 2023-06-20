import React, { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import Header from "../components/Header";
import Estimate from "../components/Contractor/Estimate";
import ConstructionHistory from "../components/Contractor/Expereience";

// 하청 페이지 컴포넌트
const Contractor = () => {
  const [city, setCity] = useState("");
  const [district, setDistrict] = useState("");
  const [trade, setTrade] = useState("");
  const [chosenMenu, setChosenMenu] = useState("신규 견적");

  const menu = ["견적 조회", "경력 조회"];

  const handleMenuClick = (menu) => {
    setChosenMenu(menu);
  };

  const renderMainContent = () => {
    switch (chosenMenu) {
      case "견적 조회":
        return <Estimate />;
      case "경력 조회":
        return <ConstructionHistory />;
      default:
        return <Estimate />;
    }
  };

  return (
    <>
      <Header handleMenuClick={handleMenuClick} menu={menu} />
      {renderMainContent()}
    </>
  );
};

export default Contractor;
