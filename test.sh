#!/bin/bash

###########################################################################################################
# This shell script helps in the creation and publication of the environment for an specific JIRA ticket. #
###########################################################################################################

if [[ $# -ne 3 ]] ; then
    echo "ERROR: Not all the required parameters were supplied."
    echo "$0 {TICKET} {PORT} {VERSION}"
    echo "WHERE:"
    echo "- TICKET: Is the ticket you are providing the environment for (e.g. 156 - from KAYAKORW-156)"
    echo "- PORT: Is the port that you are going to use from the pool (e.g. 10210 - checkout KAYAKORW-156)"
    echo "- VERSION: Is the version of your test (e.g. 1 at start but if you need to change you should increase the version)"
    exit -1
fi

if [ "$2" -lt 10210 -o "$2" -gt 10219 ] ; then
  echo "ERROR: The port selected ($2) is not on the range of the reserved ones."
  exit -1
fi

REPOSITORY=registry2.swarm.devfactory.com/easier/kayako/test
TAG=kayakorw-$1-v$3

echo "Building the API:"
./gradlew build

echo "Preparing the docker container:"
docker build -f test.Dockerfile -t $REPOSITORY .

echo "Tagging the docker image for the specific ticket ($TAG)"
docker tag $REPOSITORY $REPOSITORY:$TAG

echo "Pushing the docker tag to the registry:"
docker push $REPOSITORY:$TAG

echo "Running the image on central docker:"
docker -H dlb1.aureacentral.com run -d --name aurea-kayakorw-$1 -l "com.trilogy.service=test-kayakorw-$1" --cpu-period=50000 --cpu-quota=200000 -m 24GB -p $2:4000 $REPOSITORY:$TAG
