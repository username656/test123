FROM openjdk:8-jdk-alpine

MAINTAINER ignacio.larranaga@aurea.com

RUN apk update
RUN apk add nodejs

WORKDIR /usr/src/kayako

# Prepare Frontend
COPY ui/.npmrc /usr/src/kayako/
COPY ui/.angular-cli.json /usr/src/kayako/
COPY ui/*.js /usr/src/kayako/
COPY ui/*.json /usr/src/kayako/
COPY ui/src /usr/src/kayako/src/
RUN npm install

# Prepare Backend
COPY service/sample-zbw-api/build/libs/sample-zbw-api.jar /usr/src/kayako/

# Default port to be exposed.
EXPOSE 4000

LABEL com.trilogy.company=aurea
LABEL com.trilogy.maintainer.email=ignacio.larranaga@aurea.com
LABEL com.trilogy.maintainer.skype=ignaciolarranaga
LABEL com.trilogy.product=kayako
LABEL com.trilogy.service=test
LABEL com.trilogy.stage=qa
LABEL com.trilogy.team=Easier

# Run in foreground.
CMD sh -c 'npm run start-test & java -jar sample-zbw-api.jar'
