# For Java 11
FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /app

# Copy jar
COPY target/anodiam-login-service-0.0.1-SNAPSHOT.jar /app/anodiam-login-service.jar

RUN ls -lrt /app

# Run
ENTRYPOINT ["java", "-jar","/app/anodiam-login-service.jar"]