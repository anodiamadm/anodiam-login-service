# https://hub.docker.com/_/maven
FROM maven:3.8.6-openjdk-11-slim as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml ./
COPY src ./src/

# Build a release artifact.
RUN mvn clean package -DskipTests

# For Java 11
FROM adoptopenjdk/openjdk11:alpine-jre

# Copy jar
COPY --from=builder /app/target/anodiam-login-service-*.jar /anodiam-login-service.jar

# Run
ENTRYPOINT ["java", "-jar","/anodiam-login-service.jar"]