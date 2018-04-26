#!/usr/bin/env bash
TAG=$1
NAME=$2

PROJECT=zbw

docker tag $NAME:$TAG registry2.swarm.devfactory.com/$PROJECT/$NAME:$TAG
docker push registry2.swarm.devfactory.com/$PROJECT/$NAME:$TAG
