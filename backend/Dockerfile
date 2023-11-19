FROM maven:3.9.5-eclipse-temurin-21-alpine

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

CMD mvn spring-boot:run