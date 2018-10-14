#!/bin/bash -aux

# Import new root SSL certificates.
update-ca-certificates

# Set mandatory options, which are not overridden by JAVA_OPTS setting
MANDATORY_JAVA_OPTS=${MANDATORY_JAVA_OPTS:-"-agentpath:/opt/libyjpagent.so=disablestacktelemetry,disableexceptiontelemetry,delay=10000 -XX:+PrintGC -Djava.security.egd=file:/dev/./urandom -XshowSettings "}

# to deceive jvm about number of cores and memory in host machine
_JAVA_OPTIONS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxMetaspaceSize=512M -XX:MaxRAMFraction=1 "
LD_PRELOAD="/opt/libnumcpus.so"
_NUM_CPUS="${NUM_CPUS}"
# If optional options are not set, use empty string
JAVA_OPTS=${JAVA_OPTS:-""}


echo "Final java options are \"${MANDATORY_JAVA_OPTS} ${JAVA_OPTS} _JAVA_OPTIONS: ${_JAVA_OPTIONS}\""

# Don't insert line breaks, otherwise exec fails
java -version
exec java ${MANDATORY_JAVA_OPTS} ${JAVA_OPTS} -Dorg.eclipse.jetty.server.Request.maxFormContentSize=5000000 -jar /opt/aurea-zero-based-api.jar
