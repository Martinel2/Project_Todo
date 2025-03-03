# 1. OpenJDK 17을 베이스 이미지로 사용
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. JAR 파일 복사
COPY ./build/libs/*.jar app.jar

# 4. 포트 8080 열기
EXPOSE 8080

# 5. 애플리케이션 실행 명령어
CMD ["java", "-jar", "app.jar"]

