FROM openjdk:8-alpine
COPY target/ordercontext-command-0.0.1-SNAPSHOT.jar /ordercontext-command.jar
COPY target/otel-agent.jar /otel-agent.jar
ENTRYPOINT ["java", "-jar","/ordercontext-command.jar"]
