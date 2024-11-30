#!/bin/bash


cd ./service/gate || exit
mvn clean package
docker build -t gate .

cd ../permit || exit
mvn clean package
docker build -t permit .

cd ../visitor_access || exit
mvn clean package
docker build -t visitor_access .

cd ../..
docker compose up --build