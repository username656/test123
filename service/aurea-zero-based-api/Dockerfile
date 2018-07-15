FROM sgrio/java-oracle:jre_8

MAINTAINER alexander.yushchenko@aurea.com

RUN mkdir -p /opt

#Add yourkit agent
ADD docker/yourkit-linux-x86-64/libyjpagent.so /opt

# Launcher script.
ADD docker/start.sh /opt/start.sh
RUN chmod +x /opt/start.sh

# Logging.
ENV GRAYLOG_SWITCH_ON=on \
  APP_ENVIRONMENT=dev \
  FACILITY=zero

# Default port to be exposed.
EXPOSE 8080

# Import application.
ADD build/libs/aurea-zero-based-api.jar /opt/aurea-zero-based-api.jar

# Run in foreground.
CMD [ "/opt/start.sh" ]
