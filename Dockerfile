FROM openjdk:17-alpine3.14
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY build/libs/sched-0.0.1-SNAPSHOT.jar sched.jar
EXPOSE 3000
ENTRYPOINT exec java $JAVA_OPTS -jar sched.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar sched.jar
