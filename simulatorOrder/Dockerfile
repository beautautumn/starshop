FROM openjdk:8-alpine
COPY target/simulator-0.0.1-SNAPSHOT.jar /simulateOrder.jar
COPY target/otel-agent.jar /otel-agent.jar
ENTRYPOINT ["java", "-jar","/simulateOrder.jar"]
