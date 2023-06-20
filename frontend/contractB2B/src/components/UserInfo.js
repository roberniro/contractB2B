import React from "react";

// 로그인한 사용자 정보 표시 컴포넌트
const UserInfo = () => {
  const name = sessionStorage.getItem("name");
  const role = sessionStorage.getItem("role");

  return <span className="text-white">{`${name}(${role})님 환영합니다.`}</span>;
};

export default UserInfo;
