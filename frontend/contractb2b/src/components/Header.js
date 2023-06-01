import React, { useEffect, useState } from "react";
import { Nav, Navbar, NavDropdown } from "react-bootstrap";
import UserInfo from "./UserInfo";
import Logout from "./Logout";
import "./Header.css";

const Header = ({ handleMenuClick, menu }) => {
  const [dropdownTitle, setDropdownTitle] = useState("");

  useEffect(() => {
    setDropdownTitle(menu[0]);
  }, []);

  const handleDropdownClick = (menu) => {
    handleMenuClick(menu);
    setDropdownTitle(menu);
  };

  return (
    <Navbar bg="dark" variant="dark">
      <Navbar.Brand href="#home" className="header-brand">
        건설 INFO
      </Navbar.Brand>
      <Navbar.Toggle aria-controls="basic-navbar-nav" />
      <Navbar.Collapse>
        <Nav>
          <NavDropdown title={dropdownTitle} className="header-dropdown">
            {menu.map((menu) => (
              <NavDropdown.Item
                key={menu}
                onClick={() => handleDropdownClick(menu)}
              >
                {menu}
              </NavDropdown.Item>
            ))}
          </NavDropdown>
        </Nav>
      </Navbar.Collapse>
      <div className="header-user-info">
        <UserInfo />
      </div>
      <div className="header-logout">
        <Logout />
      </div>
    </Navbar>
  );
};

export default Header;
