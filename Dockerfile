# For Java 11
FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /app

# Copy jar
COPY target/anodiam-login-service*.jar /app/anodiam-login-service.jar

# Run
ENTRYPOINT ["java", "-jar","/app/anodiam-login-service.jar"]