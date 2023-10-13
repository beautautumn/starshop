FROM openjdk:8-alpine
COPY target/productcontext-command-0.0.1-SNAPSHOT.jar /productcontext-command.jar
COPY target/otel-agent.jar /otel-agent.jar
ENTRYPOINT ["java", "-jar","/productcontext-command.jar"]
