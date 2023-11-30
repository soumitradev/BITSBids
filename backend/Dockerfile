FROM maven:3.9.5-eclipse-temurin-21-alpine

WORKDIR /app
COPY pom.xml .
RUN mvn clean dependency:go-offline -Dskip-tests
COPY src ./src

CMD mvn spring-boot:run
