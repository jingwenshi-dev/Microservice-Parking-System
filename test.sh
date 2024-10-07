GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

# Download rabbitmqadmin from the RabbitMQ Management Plugin
if [ ! -f rabbitmqadmin ]; then
    wget http://localhost:15672/cli/rabbitmqadmin
    chmod +x rabbitmqadmin
fi

echo -e "\nNote: Transponder 1 is the only valid transponder in database."
echo -e "The returned msg should either be ${GREEN}true${NC} or ${RED}false${NC}, which indicating the gate will be open or close.\n"

echo "Sending transponder with ID 1"
# Connect to the RabbitMQ server and publish a message
./rabbitmqadmin --username=root --password=root publish routing_key=transponder.queue payload="1"
# Give some time for the message to be processed
sleep 1
result=$(./rabbitmqadmin --username=root --password=root get queue=gate.ctrl ackmode=ack_requeue_false --format=raw_json)
payload=$(echo "$result" | jq -r '.[0].payload')
echo -e "${GREEN}$payload${NC} \n"

echo "Sending transponder with ID 2"
# Connect to the RabbitMQ server and publish a message
./rabbitmqadmin --username=root --password=root publish routing_key=transponder.queue payload="2"
# Give some time for the message to be processed
sleep 1
result=$(./rabbitmqadmin --username=root --password=root get queue=gate.ctrl ackmode=ack_requeue_false --format=raw_json)
payload=$(echo "$result" | jq -r '.[0].payload')
echo -e "${RED}$payload${NC} \n"