FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the JAR file
COPY api/api/target/api-0.0.1-SNAPSHOT.jar api.0.0.1-SNAPSHOT.jar

ENV DB_URL=jdbc:postgresql://db:5432/notapro
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=postgres
ENV DB_MAX_POOL_SIZE=10
ENV DB_MIN_IDLE=5
ENV MAIL_USERNAME=mail@example.com
ENV MAIL_PASSWORD=password
ENV SERVER_PORT=8056
ENV CORS_ALLOWED_ORIGINS=https://notapro.romeu.dev.br

EXPOSE 8056

ENTRYPOINT ["java", "-jar", "/app/api.0.0.1-SNAPSHOT.jar"]