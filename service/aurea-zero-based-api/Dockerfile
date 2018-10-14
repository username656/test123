FROM sgrio/java-oracle:jre_8

RUN mkdir -p /opt

LABEL com.trilogy.company=aurea \
  com.trilogy.maintainer=alexander.yushchenko@aurea.com \
  com.trilogy.maintainer=alexander.yushchenko \
  com.trilogy.product=kayako \
  com.trilogy.service=api \
  com.trilogy.stage=dev \
  com.trilogy.team=Easier.BackendRewrite

# Logging.
ENV GRAYLOG_SWITCH_ON on \
  APP_ENVIRONMENT dev \
  FACILITY zero

# Service API
EXPOSE 8080
# management port "/mng/*" urls
EXPOSE 8282
# Below port is needed for connecting to yourkit agent
EXPOSE 10001

# Remote debug
EXPOSE 5005

COPY docker/ build/libs/aurea-zero-based-api.jar /opt/

RUN chmod +x /opt/start.sh;

WORKDIR /opt

# Run in foreground.
CMD [ "/opt/start.sh" ]
