FROM openjdk:8-alpine
COPY target/authcontext-0.0.1-SNAPSHOT.jar /authcontext.jar
COPY target/otel-agent.jar /otel-agent.jar
ENTRYPOINT ["java", "-jar","/authcontext.jar"]
