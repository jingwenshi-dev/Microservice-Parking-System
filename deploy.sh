cd ./service/gate || exit
mvn clean package

cd ../permit || exit
mvn clean package

docker compose up