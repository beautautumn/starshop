FROM openjdk:8-alpine
COPY target/platintegrationcontext-0.0.1-SNAPSHOT.jar /platintegration.jar
COPY target/otel-agent.jar /otel-agent.jar
ENTRYPOINT ["java", "-jar","/platintegration.jar"]
