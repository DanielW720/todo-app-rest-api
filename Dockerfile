# Use a base image with [Java 17](https://www.google.com/search?q=Java%2017)
FROM openjdk:17-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled Spring application JAR file to the container
COPY target/todoapp-0.0.1.jar /app/todoapp.jar

# Expose the port that your Spring application listens on
EXPOSE 8080

# Set the command to run your Spring application
CMD ["java", "-jar", "todoapp.jar"]
