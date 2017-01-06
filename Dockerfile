FROM frolvlad/alpine-oraclejdk8:slim
RUN apk update
RUN apk add bash
RUN apk add ca-certificates
RUN update-ca-certificates
RUN apk add openssl
RUN wget https://services.gradle.org/distributions/gradle-3.3-bin.zip
RUN unzip gradle-3.3-bin.zip && rm gradle-3.3-bin.zip
RUN mv gradle-3.3 /usr/local
RUN ln -s /usr/local/gradle-3.3/bin/gradle bin/gradle
RUN apk update && apk add libstdc++ && rm -rf /var/cache/apk/*
COPY src/ /opt/boot-app/src
COPY build.gradle /opt/boot-app
WORKDIR /opt/boot-app
RUN gradle build
RUN cp build/libs/spring-boot-app-0.1.0.jar /app.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar --debug" ]