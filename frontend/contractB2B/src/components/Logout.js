import React from "react";
import { useNavigate } from "react-router-dom";

// 로그아웃 컴포넌트
const Logout = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    sessionStorage.clear();
    navigate("/login");
  };

  return (
    <button className="btn-secondary btn text-white" onClick={handleLogout}>
      로그아웃
    </button>
  );
};

export default Logout;
