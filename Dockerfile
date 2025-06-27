FROM  maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

ENV FILE_PROCESSING_PATH="/app/file-processing"
ENV FILE_RESULT_PATH="/app/file-processing"
ENV REPORT_HTML_RESULT_PATH="/app/file-processing"
ENV FILE_PROCESSING_NAME="lote.simple.json"

COPY pom.xml .
# Descarga dependencias primero para aprovechar la caché de Docker
COPY processing-app ./processing-app
COPY src ./src
RUN mvn clean package


FROM  azul/zulu-openjdk:21
ENV JAVA_OPTS=" "
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar


# Ejecutar la aplicación Spring Boot correctamente
ENTRYPOINT ["java", "-jar", "app.jar"]