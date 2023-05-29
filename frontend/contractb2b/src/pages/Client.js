import React, { useState } from "react";
import { Container, Form, Button } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import "./Client.css";
import Logout from "../components/Logout";
import UserInfo from "../components/UserInfo";
import NewEstimate from "../components/Client/NewEstimate";

const Client = () => {
  const [city, setCity] = useState("");
  const [district, setDistrict] = useState("");
  const [trade, setTrade] = useState("");
  const [chosenMenu, setChosenMenu] = useState("신규 견적");

  const handleCityChange = (e) => {
    setCity(e.target.value);
  };

  const handleDistrictChange = (e) => {
    setDistrict(e.target.value);
  };

  const handleTradeChange = (e) => {
    setTrade(e.target.value);
  };

  const handleMenuClick = (menu) => {
    setChosenMenu(menu);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
  };

  const renderMainContent = () => {
    switch (chosenMenu) {
      case "신규 견적":
        return <NewEstimate />;
      case "견적 내역":
      // return <EstimateHistory />;
      case "발주 내역":
      // return <ConstructionHistory />;
      default:
        return null;
    }
  };

  return (
    <div>
      <div className="sidebar">
        <h1 className="mb-4 mt-0">건설B2B</h1>
        <ul className="nav flex-column mt-4 mb-0">
          <li className="nav-item">
            <button
              className="nav-link text-gray"
              onClick={() => handleMenuClick("신규 견적")}
            >
              신규 견적
            </button>
          </li>
          <li className="nav-item">
            <button
              className="nav-link text-gray"
              onClick={() => handleMenuClick("견적 내역")}
            >
              견적 내역
            </button>
          </li>
          <li className="nav-item">
            <button
              className="nav-link text-gray"
              onClick={() => handleMenuClick("발주 내역")}
            >
              발주 내역
            </button>
          </li>
        </ul>
      </div>
      <div className="header border-left border-secondary">
        <h3 style={{ marginBottom: 0 }}>{chosenMenu}</h3>
        <div className="header-buttons">
          <UserInfo />
          <Logout />
        </div>
      </div>
      <div className="main-content">{renderMainContent()}</div>
    </div>
  );
};

export default Client;
