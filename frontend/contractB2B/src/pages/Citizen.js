import React, { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import NewEstimate from "../components/Client/NewEstimate";
import EstimateHistory from "../components/Client/EstimateHistory";
import Header from "../components/Header";
import ConstructionHistory from "../components/Client/ConstructionHistory";
import ContractorInfo from "../components/Citizen/ContractorInfo";
import ConstructionInfo from "../components/Citizen/ConstructionInfo";

// 시민 페이지 컴포넌트
const Client = () => {
  const [chosenMenu, setChosenMenu] = useState("업체 조회");

  const menu = ["업체 조회", "공사 조회"];

  const handleMenuClick = (menu) => {
    setChosenMenu(menu);
  };

  const renderMainContent = () => {
    switch (chosenMenu) {
      case "업체 조회":
        return <ContractorInfo />;
      case "공사 조회":
        return <ConstructionInfo />;
      default:
        return <ContractorInfo />;
    }
  };

  return (
    <>
      <Header handleMenuClick={handleMenuClick} menu={menu} />
      {renderMainContent()}
    </>
  );
};

export default Client;
