#!/usr/bin/env bash
TAG=$1
BUILDDIR=$2
NAME=$3

docker build -t $NAME:$TAG $BUILDDIR
