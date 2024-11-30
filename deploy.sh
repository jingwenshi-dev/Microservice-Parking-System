#!/bin/bash


cd ./service/gate || exit
mvn clean package -DskipTests
docker build -t gate .

cd ../permit || exit
mvn clean package -DskipTests
docker build -t permit .

cd ../payment || exit
mvn clean package -DskipTests
docker build -t payment .

cd ../violation || exit
mvn clean package -DskipTests
docker build -t violation .

cd ../visitor_access || exit
mvn clean package -DskipTests
docker build -t visitor_access .

cd ../..
docker compose up --build