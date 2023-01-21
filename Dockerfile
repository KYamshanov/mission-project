FROM openjdk:17
WORKDIR /app/
COPY /build/libs/mission-project-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 80
CMD ["java","-jar","/app/app.jar"]