FROM eclipse-temurin:21-jdk-alpine
ADD /backend_upgrade-4.0.0.jar //
RUN apk add --no-cache fontconfig ttf-dejavu
RUN apk add --no-cache curl
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /backend_upgrade-4.0.0.jar
