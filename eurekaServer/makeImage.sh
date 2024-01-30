docker buildx build --platform linux/amd64,linux/arm64,linux/arm/v7 -t ccr.ccs.tencentyun.com/default-ns/starshop-eureka:$STARSHOP_VERSION . --push
