# Etapa de build
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copia o JAR gerado
COPY --from=builder /app/target/insurance-policy-service-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080
