cp -f /Users/samson/workspace/observ/opentelemetry/opentelemetry-java-instrumentation/javaagent/build/libs/opentelemetry-javaagent-1.29.0-SNAPSHOT.jar target/otel-agent.jar
docker buildx build --platform linux/amd64,linux/arm64,linux/arm/v7 -t ccr.ccs.tencentyun.com/default-ns/starshop-simuorder:$STARSHOP_VERSION . --push
rm -f target/otel-agent.jar