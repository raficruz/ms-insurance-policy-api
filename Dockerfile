# Etapa de build
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Permite passar opções extras para a JVM (ex: debug)
ENV JAVA_OPTS=""

# Copia o JAR gerado (nome corrigido)
COPY --from=builder /app/target/ms-insurance-policy-service-api-1.0.0.jar app.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

EXPOSE 8080
EXPOSE 5005