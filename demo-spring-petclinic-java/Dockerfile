FROM alpine:3.18.4 AS optima-agile-agent

ENV JAVA_HOME /usr/lib/jvm/default-jvm
ENV PATH $JAVA_HOME/bin:$PATH

ENV JAVA_TOOL_OPTIONS "-Dcom.sun.management.jmxremote \
-Dcom.sun.management.jmxremote.authenticate=false \
-Dcom.sun.management.jmxremote.ssl=false \
-Dcom.sun.management.jmxremote.local.only=false \
-Dcom.sun.management.jmxremote.port=5005 \
-Dcom.sun.management.jmxremote.rmi.port=5005 \
-Djava.rmi.server.hostname=127.0.0.1"

RUN apk update && \
    apk add --no-cache openjdk17-jre-headless && \
    apk add --no-cache ca-certificates openssl

WORKDIR /app

COPY target/spring-petclinic-rest-3.4.2.jar /app/app.jar

EXPOSE 8080

CMD java -Xmx6144m -Dserver.port=8080 -jar app.jar
