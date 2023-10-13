FROM openjdk:8-alpine
COPY target/customercontext-0.0.1-SNAPSHOT.jar /customercontext.jar
COPY target/otel-agent.jar /otel-agent.jar
ENTRYPOINT ["java", "-jar","/customercontext.jar"]
