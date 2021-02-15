FROM openjdk:11-slim
COPY target/ebi-person-service-*.*.*-SNAPSHOT.jar /app.jar
EXPOSE 8080/tcp
ENTRYPOINT ["java", "-jar", "/app.jar"]