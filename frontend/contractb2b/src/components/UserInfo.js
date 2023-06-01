import React from "react";

const UserInfo = () => {
  const name = sessionStorage.getItem("name");
  const role = sessionStorage.getItem("role");

  return <span className="text-white">{`${name}(${role})님 환영합니다.`}</span>;
};

export default UserInfo;
