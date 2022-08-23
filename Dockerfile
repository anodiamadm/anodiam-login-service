# For Java 11
FROM adoptopenjdk/openjdk11:alpine-jre

# Copy jar
COPY target/anodiam-login-service-0.0.1-SNAPSHOT.jar /anodiam-login-service.jar

# Run
ENTRYPOINT ["java", "-jar","/anodiam-login-service.jar"]