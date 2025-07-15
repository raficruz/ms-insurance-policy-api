# Etapa de build
FROM gradle:8.7-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle clean bootJar --no-daemon

# Etapa de runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Permite passar opções extras para a JVM (ex: debug)
ENV JAVA_OPTS=""

# Copia o JAR gerado (nome corrigido)
COPY --from=builder /app/build/libs/ms-insurance-policy-service-api-1.0.0.jar app.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

EXPOSE 8080
EXPOSE 5005