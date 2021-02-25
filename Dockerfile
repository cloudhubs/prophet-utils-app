FROM openjdk:14
COPY /target/prophet-app-utils-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
