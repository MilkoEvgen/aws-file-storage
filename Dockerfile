
FROM amazoncorretto:17
COPY build/libs/aws-file-storage-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]


