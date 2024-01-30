docker buildx build --platform linux/amd64,linux/arm64,linux/arm/v7 -t ccr.ccs.tencentyun.com/default-ns/demo-load-generator:$STARSHOP_VERSION . --push
