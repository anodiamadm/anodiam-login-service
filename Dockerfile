FROM openjdk:8-alpine
ADD target/*.jar microservice001-student-signup.jar
ENTRYPOINT ["java","-jar","microservice001-student-signup.jar"]