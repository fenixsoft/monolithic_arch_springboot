#!/bin/bash
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
docker build -t bookstore:monolithic .
docker images
docker tag bookstore:monolithic $DOCKER_USERNAME/bookstore:monolithic
docker push $DOCKER_USERNAME/bookstore:monolithic
