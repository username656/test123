#!/usr/bin/env bash
BRANCH=$1
SHA1=$2
NAME=$3
LOGIN_USERNAME=$4
LOGIN_PASSWD=$5

VERSION=$BRANCH-$SHA1
PROJECT=aurea-zero-based

# Authenticate against our Docker registry
docker login -u $LOGIN_USERNAME -p $LOGIN_PASSWD registry2.swarm.devfactory.com

docker tag $NAME:$VERSION registry2.swarm.devfactory.com/$PROJECT/$NAME:$VERSION
docker push registry2.swarm.devfactory.com/$PROJECT/$NAME:$VERSION
