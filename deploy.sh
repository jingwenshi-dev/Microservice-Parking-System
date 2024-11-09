#!/bin/bash


cd ./service/gate || exit
mvn clean package
docker build -t gate .

cd ../permit || exit
mvn clean package
docker build -t permit .
cd ../..
docker compose up --build