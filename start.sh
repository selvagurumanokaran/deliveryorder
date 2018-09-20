#!/bin/bash

docker rm --force deliveryordermysqlimage deliveryorderwebimage
docker-compose up -d
printf 'waiting for application .'
until $(curl --output /dev/null --silent --head --fail http://localhost:8080/orders); do
    printf '.'
    sleep 5
done
printf 'Application started successfully!'
exit 0