FROM openjdk:21

#prima fai mvn clean package cosi si crea il file .jar
COPY target/task-0.0.1-SNAPSHOT.jar ethscanner.jar

EXPOSE 9090

ENTRYPOINT ["java","-jar","/ethscanner.jar"]