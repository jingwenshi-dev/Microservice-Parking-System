#!/bin/bash

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

download_rabbitmqadmin() {
    if [ ! -f rabbitmqadmin ]; then
        wget http://localhost:15672/cli/rabbitmqadmin
        chmod +x rabbitmqadmin
    fi
}

print_note() {
    echo ""
    echo "Note: Transponder 1 is the only valid transponder in database."
    echo -e "The returned msg should either be ${GREEN}true${NC} or ${RED}false${NC}, which indicating the gate will be open or close."
    echo ""
}

send_transponder() {
    local id=$1
    echo "Sending transponder with ID $id"
    ./rabbitmqadmin --username=root --password=root publish routing_key=transponder.queue payload="$id"
    sleep 1
    result=$(./rabbitmqadmin --username=root --password=root get queue=gate.ctrl ackmode=ack_requeue_false --format=raw_json)
    payload=$(echo "$result" | jq -r '.[0].payload')
    if [ "$payload" = "true" ]; then
        echo -e "${GREEN}$payload${NC}"
    else
        echo -e "${RED}$payload${NC}"
    fi
    echo ""
}

main() {
    download_rabbitmqadmin
    print_note
    send_transponder 1
    send_transponder 2
}

main