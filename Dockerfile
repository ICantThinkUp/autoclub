FROM adoptopenjdk/openjdk11:alpine-jre
VOLUME /etc/letsencrypt/live/autoclub156.website/keystore.p12
ARG JAR_FILE=target/demo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
