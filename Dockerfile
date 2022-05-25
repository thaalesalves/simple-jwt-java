FROM docker.io/openjdk:17-alpine

WORKDIR /app
RUN apk add -u git maven && \
    git clone https://github.com/thaalesalves/simple-jwt-java.git && \
    mvn clean install -f /app/simple-jwt-java/pom.xml

FROM docker.io/openjdk:17-alpine
WORKDIR /opt
COPY --from=0 /app/simple-jwt-java/target/interview-0.0.1-SNAPSHOT.jar ./
EXPOSE 8080
ENTRYPOINT "java -jar ./interview-0.0.1-SNAPSHOT.jar"
