FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests --batch-mode --errors -X

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/webtech-0.0.1-SNAPSHOT.jar webtech.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "webtech.jar"]