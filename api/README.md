# Introduction

This project is a mock created for the ZBW project. It does mainly contains the API's with some trivial mock code.

This project was seed with https://github.com/trilogy-group/easier-template-java

## Deployment

To application is packaged as an executable jar so it can be started as follows:
`java -jar ./target/api-1.0.0-SNAPSHOT.jar`

This project uses Actuator for Management, you can use: `curl http://localhost:8080/health` to check the system status
(check https://spring.io/guides/gs/actuator-service/ for more info).

## Development

### Start the Service

To start the service run `./mvnw spring-boot:run` and open `http://localhost:8080/`.

### Authentication

The application uses Spring Security and JWT for authentication.
The users/passwords are retrieved from the DB attached (users/roles table)

For authenticate your request you should execute a call like the follwing:
```
curl -X POST \
  http://localhost:8080/auth/login \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -d '{
	"username": "user",
	"password": "secret"
}'
```

And you will receive the token in the *Authorization* header.
After that you can proceed with the received token as follows:
```
curl -X GET \
  http://localhost:8080/data/users \
  -H 'Authorization: Bearer Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiQXV0aG9yaXRpZXMiOiJVU0VSIiwiZXhwIjoxNTIxNTU5NjUwfQ.h2w8RzTcUOfN5DbTjracYuvfwpf6O0iw8N29QqDhCVLPdOkp-JKPTZ6pq1b7Ek_hVCxMerI-KUHvaIi1-H0Snw' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json'
```

### Docker

In order to build the docker image you need to run `./mvnw install dockerfile:build`.
In order to build the docker image you need to run `./mvnw install dockerfile:build dockerfile:push`.
In order to run it you have to execute `docker run --name zbw-mock-api -p 8080:8080 -t registry2.swarm.devfactory.com/easier/zbw/api:latest`
In order to publish you must run (UNDER REVIEW):
```
docker run -d --name dev_kayako_api-mocks -l "com.trilogy.company=aurea" \
-l "com.trilogy.team=Easier" \
-l "com.trilogy.product=zbw" \
-l "com.trilogy.service=api-mocks" \
-l "com.trilogy.stage=dev" \
-l "com.trilogy.maintainer.skype=ignaciolarranaga" \
-l "com.trilogy.maintainer.email=ignacio.larranaga@aurea.com" \
--cpu-period=50000 --cpu-quota=200000 -m 24GB \
-p XXXXXX:8080
```
