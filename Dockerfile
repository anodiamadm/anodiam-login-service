# For Java 11
FROM adoptopenjdk/openjdk11:alpine-jre

# Copy jar
COPY /artifact/target/anodiam-login-service-*.jar /anodiam-login-service.jar

# Run
ENTRYPOINT ["java", "-jar","/anodiam-login-service.jar"]