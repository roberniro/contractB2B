FROM openjdk:11-jdk

RUN apt-get update && apt-get install -y dos2unix
FROM openjdk:11

# 작업 디렉토리 설정
WORKDIR /app

# gradlew, build.gradle, settings.gradle 파일 복사
COPY gradlew build.gradle settings.gradle ./

# gradle 디렉토리 복사
COPY gradle ./gradle

# gradlew 파일에 실행 가능한 권한 부여
RUN chmod +x gradlew

# 필요한 의존 패키지 다운로드
RUN ./gradlew --no-daemon dependencies

# 소스 코드 복사
COPY src ./src

# 애플리케이션 빌드
RUN ./gradlew --no-daemon build -x test

# 빌드된 JAR 파일을 target 디렉토리로 복사
RUN mkdir -p target && (cd build/libs && cp contractB2B-0.0.1-SNAPSHOT.jar ../../target/contractB2B.jar)

# 애플리케이션 실행
CMD ["java", "-jar", "target/contractB2B.jar"]




