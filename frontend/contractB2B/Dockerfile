# 베이스 이미지 선택
FROM node:latest

# 작업 디렉토리 생성
WORKDIR /app

# 패키지 파일 복사
COPY package.json package-lock.json /app/

# 패키지 설치
RUN npm install

# 소스 코드 복사
COPY . /app

# 애플리케이션 빌드
RUN npm run build

# 애플리케이션 실행
CMD ["npm", "start"]

