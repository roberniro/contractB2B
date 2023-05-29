import React from "react";

const UserInfo = () => {
  const name = sessionStorage.getItem("name");
  const role = sessionStorage.getItem("role");

  return <span className="mr-2">{`${name}(${role})님 환영합니다.`}</span>;
};

export default UserInfo;
