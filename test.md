# Test Environment

First build the code with:
* Have Docker deamon started
* Being connected to the VPN
* Backend: `./gradlew build`

In order to build the docker container:
`docker build -f test.Dockerfile -t registry2.swarm.devfactory.com/easier/kayako/test .`

In order to run the container locally:
`docker run -d --name aurea-kayakorw-XXX -p 4000:4000 registry2.swarm.devfactory.com/easier/kayako/test`
for example: `docker run -d --name aurea-kayakorw-156 -p 4000:4000 registry2.swarm.devfactory.com/easier/kayako/test`

In order to tag the container:
`docker tag docker tag registry2.swarm.devfactory.com/easier/kayako/test registry2.swarm.devfactory.com/easier/kayako/test:kayakorw-XXX`
for example: `docker tag registry2.swarm.devfactory.com/easier/kayako/test registry2.swarm.devfactory.com/easier/kayako/test:kayakorw-156`

In order to transfer the image to the registry:
`docker push registry2.swarm.devfactory.com/easier/kayako/test:kayakorw-XXX`
for example: `docker push registry2.swarm.devfactory.com/easier/kayako/test:kayakorw-156`

In order to run the docker container you have to run:
`docker -H dlb1.aureacentral.com run -d --name aurea-kayakorw-XXX ---l "com.trilogy.service=test-kayakorw-XXX" --cpu-period=50000 --cpu-quota=200000 -m 24GB -p YYYYY:4000 registry2.swarm.devfactory.com/easier/kayako/test/kayakorw-XXX`
for example: `docker -H dlb1.aureacentral.com run -d --name aurea-kayakorw-156 -l "com.trilogy.service=test-kayakorw-156" --cpu-period=50000 --cpu-quota=200000 -m 24GB -p 10202:4000 registry2.swarm.devfactory.com/easier/kayako/test:kayakorw-156`

where:
* XXX has to be replaced by the actual ticket number,
* YYYYY has to be replaced by the port number

Notes:
* It does takes some time for the container to start
* If you want to check the logs of the container execute: `docker -H dlb1.aureacentral.com logs -f aurea-kayakorw-XXX`
* To check the kayako tickets running on central you can execute: `docker -H dlb1.aureacentral.com ps | grep kayakorw-XXX`
* To stop/remove one existing instance from docker central please run:
    * Stop: `docker -H dlb1.aureacentral.com stop aurea-kayakorw-XXX`
    * Remove: `docker -H dlb1.aureacentral.com rm aurea-kayakorw-XXX`
