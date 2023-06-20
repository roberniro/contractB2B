import React, { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import NewEstimate from "../components/Client/NewEstimate";
import EstimateHistory from "../components/Client/EstimateHistory";
import Header from "../components/Header";
import ConstructionHistory from "../components/Client/ConstructionHistory";

// 원청 페이지 컴포넌트
const Client = () => {
  const [chosenMenu, setChosenMenu] = useState("신규 견적");

  const menu = ["신규 견적", "견적 내역", "발주 내역"];

  const handleMenuClick = (menu) => {
    setChosenMenu(menu);
  };

  const renderMainContent = () => {
    switch (chosenMenu) {
      case "신규 견적":
        return <NewEstimate />;
      case "견적 내역":
        return <EstimateHistory />;
      case "발주 내역":
        return <ConstructionHistory />;
      default:
        return <NewEstimate />;
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
