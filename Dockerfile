# For Java 11
FROM adoptopenjdk/openjdk11:alpine-jre

RUN ls -lrt

COPY . /tmp/build/

RUN ls -lrt /tmp/build/

RUN ls -lrt /tmp/build/target

# Copy jar
COPY tmp/build/target/anodiam-login-service-*.jar /anodiam-login-service.jar

# Run
ENTRYPOINT ["java", "-jar","/anodiam-login-service.jar"]