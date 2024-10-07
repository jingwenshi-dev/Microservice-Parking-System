# Download rabbitmqadmin from the RabbitMQ Management Plugin
if [ ! -f rabbitmqadmin ]; then
    wget http://localhost:15672/cli/rabbitmqadmin
    chmod +x rabbitmqadmin
fi

echo "Transponder 1 is the only valid transponder in database"

echo "Sending transponder with ID 1"

# Connect to the RabbitMQ server and publish a message
./rabbitmqadmin --username=root --password=root publish routing_key=transponder.queue payload="1"

# Give some time for the message to be processed
sleep 1

result=$(./rabbitmqadmin --username=root --password=root get queue=gate.ctrl ackmode=ack_requeue_false)
echo "$result"