FROM openjdk:8-alpine
COPY target/starshop-edge-0.0.1-SNAPSHOT.jar /gatewayServer.jar
COPY target/otel-agent.jar /otel-agent.jar
ENTRYPOINT ["java", "-jar","/gatewayServer.jar"]
