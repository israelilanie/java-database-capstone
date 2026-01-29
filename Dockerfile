FROM maven:3.9.10-eclipse-temurin-17 AS build

WORKDIR /workspace
COPY app/pom.xml app/pom.xml
RUN mvn -q -f app/pom.xml -DskipTests dependency:go-offline

COPY app app
RUN mvn -q -f app/pom.xml -DskipTests package

FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=build /workspace/app/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
