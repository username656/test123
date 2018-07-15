FROM openjdk:8-jdk-alpine

MAINTAINER alexander.yushchenko@aurea.com

RUN mkdir -p /opt

#Add yourkit agent
ADD service/docker/yourkit-linux-x86-64/libyjpagent.so /opt

# Launcher script.
ADD service/docker/start.sh /opt/start.sh
RUN chmod +x /opt/start.sh

# Logging.
ENV GRAYLOG_SWITCH_ON=on \
  APP_ENVIRONMENT=dev \
  FACILITY=zero

# Default port to be exposed.
EXPOSE 8080

# Build application.
WORKDIR /usr/src/aurea-zero-based
COPY service/ /usr/src/aurea-zero-based/service
COPY ./gradlew /usr/src/aurea-zero-based
COPY /gradle /usr/src/aurea-zero-based/gradle
COPY *gradle* /usr/src/aurea-zero-based/
RUN /usr/src/aurea-zero-based/gradlew build

# Run in foreground.
CMD [ "/opt/start.sh" ]
