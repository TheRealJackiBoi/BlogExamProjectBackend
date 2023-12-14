# Base Java image
FROM eclipse-temurin:17-alpine

# .jar file copied from the target folder
COPY target/app.jar /app.jar

# This is the port that your javalin application will listen on
EXPOSE 7070

# This is the command that will be run when the container starts
ENTRYPOINT ["java", "-jar", "/app.jar"]
