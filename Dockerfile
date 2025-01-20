FROM amazoncorretto:17
COPY target/task-management-system-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]