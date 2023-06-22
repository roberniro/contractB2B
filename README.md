# 2023년 1학기 건설환경캡스톤디자인 IT 프로젝트
<br/>

## 프로젝트 소개
### 프로젝트명
- 건설 INFO

### 프로젝트목적
- 건설 공사 및 하청 업체 정보 공개, 원청 업체와 민원 상담 통한 건설업 투명성 및 시민 편의 증진

### 주요 기능
- 원청, 하청 업체간 견적 발송
- 원청의 공사 상태 관리, 용역업체 평점 등록
- 완료 공사 자동 경력 등록
- 원청, 시민간 실시간 채팅 상담
- 채팅방id로 Kafka 토픽 생성하여 pub, 각 토픽 sub 다중채팅방 구현
- 채팅 내용 mongoDB 도큐먼트로 저장하여 채팅 기록 관리

### 개발 기간
- 2023.05.04 ~ 2023.06.08
<br>

## 개발 환경
  <div>
  <img src="https://img.shields.io/badge/Java-007396.svg?&style=for-the-badge&logo=Java&logoColor=white">
  <img src="https://img.shields.io/badge/amazon s3-569A31?style=for-the-badge&logo=amazon s3&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/MongoDB-7A248?style=for-the-badge&logo=MongoDB&logoColor=white">
  <img src="https://img.shields.io/badge/Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white">
  </div>
  <div>
  <img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white">
  <img src="https://img.shields.io/badge/css3-1572B6?style=for-the-badge&logo=css3&logoColor=white">
  <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
  <img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=React&logoColor=black">
  <img src="https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=Bootstrap&logoColor=white">
  </div>
  <div>
  <img src="https://img.shields.io/badge/ubuntu-E95420?style=for-the-badge&logo=ubuntu&logoColor=white">
  <img src="https://img.shields.io/badge/amazon ec2-FF9900?style=for-the-badge&logo=amazon ec2&logoColor=white">
  <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
  <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/VsCode-007ACC?style=for-the-badge&logo=Visual Studio Code&logoColor=white">
  <img src="https://img.shields.io/badge/intellijidea-000000?style=for-the-badge&logo=intelliJidea&logoColor=white">
  </div>
  
### Backend
- Java Corretto JDK11
- Spring Boot v2.7.10
- MySQL v8.0.32
- MongoDB v6.0.6
- Kafka  v3.4.0
- Intellij 2022.3.1

### Frontend
- HTML5, CSS, JavaScript
- React v18.2.0
- BootStrap v5.2
- Visual Studio Code v1.78.2
  
### Cloud
- AWS EC2
  
### OS
- Ubuntu 22.04.1 LTS

### Virtualalization
- Docker v24.0.2
- Docker-Compose v2.5
<br>

## DB 설계
### 정형데이터
- User테이블 role속성으로 권한 관리
- role이 하청업체면 Contractor테이블과 매핑하여 하청업체 평점, 경력 관리
- Estimate테이블 motherId속성으로 원견적과 재견적 트리 형태로 관리 의도(view에서는 원견적, 재견적 계층 조회 구현하지 못함)
- User의 주소, Estimate와 Construction의 현장은 Address테이블과 매핑하여 시/도, 시/군/구, 상세주소별 조회 가능
<br><br>
![erd](https://github.com/roberniro/contractB2B/assets/113920417/9064873c-90e6-4ea6-8b5d-1824e494936d)

### 비정형데이터
- 채팅기록 Document로 저장하여 관리
<br><br>
![image](https://github.com/roberniro/contractB2B/assets/113920417/e9c71fbd-c657-4922-bb77-a352a1f3187a)
<br>

## 기능 설명 
### 회원가입&로그인
- 원청, 용역업체 회원가입 화면<br>
<img src="https://github.com/roberniro/contractB2B/assets/113920417/bd8299b5-063f-4057-b0c9-1e900bd04757" width=600><br>
- 시민 회원가입 화면<br>
<img src="https://github.com/roberniro/contractB2B/assets/113920417/07c4694c-e642-402a-b4a7-1fe691fce72f" width=600><br>
- JWT 토큰 인증 방식 사용
- Spring Validation 통한 유효성 검증
- Role에 따라 다른 컴포넌트 렌더링

### 원청 기능
- 견적 조회: 견적 발송, 수락, 거절, 재견적 요청, Spring Validation 통한 유효성 검증 구현<br>
![image](https://github.com/roberniro/contractB2B/assets/113920417/67416c24-cafe-46fc-bf61-edfc4b54d0c8)
- 용역 업체 정보 조회<br>
![image](https://github.com/roberniro/contractB2B/assets/113920417/151d180e-b567-4dbb-a678-87358439099e)
- 공사 정보 조회: 공사 상태 변경, 완료 공사 평점 등록, 공사 상태 완료로 변경 후 평점 등록시 하청 업체 경력 자동 등록<br>
![image](https://github.com/roberniro/contractB2B/assets/113920417/e5afe138-8d35-4a66-b0ed-3535a1149d6b)
### 하청 기능
- 견적 조회: 견적 수락, 거절, 재견적, Spring Validation 통한 유효성 검증 구현<br>
![image](https://github.com/roberniro/contractB2B/assets/113920417/88309beb-e7e1-4fd9-93fb-83460947b74e)
- 기존 경력 등록<br>
![image](https://github.com/roberniro/contractB2B/assets/113920417/0d931e30-d167-4d26-92ee-d94c28cd08c9)
### 시민 기능
- 하청 정보 조회<br>
![image](https://github.com/roberniro/contractB2B/assets/113920417/f2961ba5-cbf9-4b50-838f-d1da6fa33f44)
- 공사 정보 조회<br>
![image](https://github.com/roberniro/contractB2B/assets/113920417/6ce97dbb-354e-4d5d-b233-f1c44182f187)
- 계약 체결 이유 조회<br>
![image](https://github.com/roberniro/contractB2B/assets/113920417/7f933c8d-7830-4b6b-9223-35ff87421f19)
- 민원 채팅 상담<br>
  <img src="https://github.com/roberniro/contractB2B/assets/113920417/fe24c27d-d727-4acc-9e5b-ca98d4b5e30b" width=1000 height=500>


