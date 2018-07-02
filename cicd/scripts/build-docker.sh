#!/usr/bin/env bash
TAG=$1
BUILDDIR=$2
NAME=$3

#dlb1 to build, dl6 to run
DOCKER_HOST='tcp://dlb1.aureacentral.com:2375'

echo "TAG=${TAG} NAME=${NAME} DOCKER_HOST=${DOCKER_HOST} BUILDDIR=${BUILDDIR} NG_BUILD_ARGS=${NG_BUILD_ARGS}"
docker build -t $NAME:$TAG $BUILDDIR
