#!/usr/bin/env bash
TAG=$1
WORKDIR=$2
NAME=$3

echo "TAG=${TAG} NAME=${NAME} DOCKER_HOST=${DOCKER_HOST} WORKDIR=${WORKDIR}"
docker build -t $NAME:$TAG $WORKDIR
