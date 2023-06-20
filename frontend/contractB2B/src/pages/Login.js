import React, { useEffect, useState } from "react";
import { Container, Form, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./Login.css";

// 로그인 페이지 컴포넌트
const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isLoginSuccess, setIsLoginSuccess] = useState(false);
  const navigate = useNavigate();

  const handleLogin = (e) => {
    const BASE_URL = process.env.REACT_APP_BASE_URL;
    e.preventDefault();
    fetch(`${BASE_URL}/user/auth`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        username,
        password
      })
    })
      .then((response) => {
        if (response.ok) {
          response
            .json()
            .then((data) => {
              sessionStorage.setItem("token", data.token);
              sessionStorage.setItem("name", data.companyName);
              sessionStorage.setItem("role", data.role);
              sessionStorage.setItem("userId", data.id);
              console.log(`token=${sessionStorage.getItem("token")}`);
            })
            .then(() => {
              setIsLoginSuccess(true);
            })
            .catch((error) => {
              console.error(error);
              setIsLoginSuccess(false);
            });
        } else {
          response
            .json()
            .then((data) => {
              let errorMessage = "";
              for (const key in data.error) {
                errorMessage += data.error[key] + "\n";
              }
              alert(errorMessage || response.statusText);
              setIsLoginSuccess(false);
            })
            .catch((error) => {
              console.error(error);
              setIsLoginSuccess(false);
            });
        }
      })
      .catch((error) => {
        console.error(error);
      });
  };

  useEffect(() => {
    if (isLoginSuccess) {
      navigate(`/${sessionStorage.getItem("role").toLowerCase()}`);
    }
  }, [isLoginSuccess]);

  const handleUsernameChange = (e) => {
    setUsername(e.target.value);
  };

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
  };

  return (
    <div className="container">
      <h2>건설 INFO</h2>
      <Form onSubmit={handleLogin}>
        <Form.Group controlId="formBasicUsername">
          <Form.Control
            type="text"
            placeholder="아이디"
            value={username}
            onChange={handleUsernameChange}
            required
          />
        </Form.Group>

        <Form.Group controlId="formBasicPassword">
          <Form.Control
            type="password"
            placeholder="비밀번호"
            value={password}
            onChange={handlePasswordChange}
            required
          />
        </Form.Group>
        <Link to="/join">
          <Button variant="secondary" type="button">
            회원가입
          </Button>
        </Link>
        <Button variant="success" type="button" onClick={handleLogin}>
          로그인
        </Button>
      </Form>
    </div>
  );
};

export default Login;
