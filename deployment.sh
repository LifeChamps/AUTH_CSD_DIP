#!/bin/bash

export KAFKA_BROKER="kafka:9092"

if [ "$1" == "build-inf" ]; then
  docker-compose -f dockerbase/docker-compose-inf.yml build
elif [ "$1" == "build-code" ]; then
    docker-compose -f dockerbase/docker-compose-code.yml build
elif [ "$1" == "start-inf" ]; then
  docker-compose -f dockerbase/docker-compose-inf.yml up -d
elif [ "$1" == "stop-inf" ]; then
  docker-compose -f dockerbase/docker-compose-inf.yml down
elif [ "$1" == "start-code" ]; then
  docker-compose -f dockerbase/docker-compose-code.yml up -d
elif [ "$1" == "stop-code" ]; then
  docker-compose -f dockerbase/docker-compose-code.yml down
elif [ "$1" == "start-job" ]; then
  docker exec -it jobmanager flink run --detach -p 8 /job.jar
fi