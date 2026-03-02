#!/bin/bash

if [ $# -ne 1 ]; then
    echo "usage: ./publish.sh docker-usr"
    exit 1
fi

REPO=$1

docker build -t $REPO/ufund-api:latest -f Dockerfile.spring .
docker build -t $REPO/ufund-ui:latest -f Dockerfile.ng .

docker push $REPO/ufund-api:latest
docker push $REPO/ufund-ui:latest
