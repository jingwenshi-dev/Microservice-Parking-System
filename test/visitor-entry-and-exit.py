import json
import threading
import time
from datetime import datetime
import pika

GREEN = '\033[0;32m'
RED = '\033[0;31m'
NC = '\033[0m'

RABBITMQ_USER = 'root'
RABBITMQ_PASS = 'root'
RABBITMQ_HOST = 'localhost'
RABBITMQ_PORT = 5672

GATE_EXCHANGE = "gateOpenRequestExchange"
GATE_ROUTING_KEY_PATTERN = "gate.%s.command"
QUEUE_NAME = "gateCommandQueue"

def receive_gate_control_message(gate_id):
    routing_key = GATE_ROUTING_KEY_PATTERN % gate_id

    credentials = pika.PlainCredentials(RABBITMQ_USER, RABBITMQ_PASS)
    connection = pika.BlockingConnection(pika.ConnectionParameters(
        host=RABBITMQ_HOST,
        port=RABBITMQ_PORT,
        credentials=credentials
    ))
    channel = connection.channel()

    channel.exchange_declare(exchange=GATE_EXCHANGE, exchange_type='direct', durable=True)

    channel.queue_declare(queue=QUEUE_NAME, durable=True)

    channel.queue_bind(exchange=GATE_EXCHANGE, queue=QUEUE_NAME, routing_key=routing_key)

    def callback(ch, method, properties, body):
        try:
            decoded_body = body.decode('utf-8')

            try:
                message = json.loads(decoded_body)
                if isinstance(message, dict) and "isValid" in message:
                    print(f"{RED}GATE OPEN {RED}")
                    print(f"{GREEN}Gate Control Message: {message}{NC}")
                else:
                    print(f"{RED}Unexpected JSON Format: {decoded_body}{NC}")
            except json.JSONDecodeError:
                if decoded_body.lower() in ["true", "false"]:
                    gate_ctrl = {"isValid": decoded_body.lower() == "true"}
                    print(f"{GREEN}Gate Control Message: {gate_ctrl}{NC}")
                else:
                    print(f"{RED}Unexpected Message Format: {decoded_body}{NC}")

        except Exception as e:
            print(f"{RED}Error processing message: {e}{NC}")
        finally:
            ch.basic_ack(delivery_tag=method.delivery_tag)

    channel.basic_consume(queue=QUEUE_NAME, on_message_callback=callback)
    channel.start_consuming()


def build_transponder_dto(transponder_id, license_plate, gate_id, lot_id, is_entry):
    transponder_dto = json.dumps({
        "transponderId": transponder_id,
        "licensePlate": license_plate,
        "gateId": gate_id,
        "lotId": lot_id,
        "entry": is_entry,
        "timestamp": datetime.now().strftime("%Y-%m-%dT%H:%M:%S")
    })
    return transponder_dto


def send_transponder_data(transponder_dto, queue_name, is_entry):
    action = "ENTRY" if is_entry else "EXIT"
    print(f"{RED}{action} REQUEST OPEN GATE{RED}")
    print(f"{GREEN}Gate input Message: {transponder_dto}{NC}")
    credentials = pika.PlainCredentials(RABBITMQ_USER, RABBITMQ_PASS)
    connection = pika.BlockingConnection(pika.ConnectionParameters(
        host=RABBITMQ_HOST,
        port=RABBITMQ_PORT,
        credentials=credentials
    ))
    channel = connection.channel()

    channel.queue_declare(queue=queue_name, durable=True)

    channel.basic_publish(
        exchange='',
        routing_key=queue_name,
        body=transponder_dto,
        properties=pika.BasicProperties(
            content_type='application/json',
            delivery_mode=2,
        )
    )
    connection.close()


def start_consumer_thread(gate_id):
    consumer_thread = threading.Thread(target=receive_gate_control_message, args=(gate_id,))
    consumer_thread.daemon = True
    consumer_thread.start()


def main():
    start_consumer_thread("Gate1")
    time.sleep(1)

    # Send ENTRY message
    transponder_data = build_transponder_dto(
        transponder_id="",
        license_plate="AAAAAA",
        gate_id="Gate1",
        lot_id=1,
        is_entry=True
    )
    send_transponder_data(transponder_data, "transponder.queue", is_entry=True)

    time.sleep(2)

    # Send EXIT message
    transponder_data = build_transponder_dto(
        transponder_id="",
        license_plate="AAAAAA",
        gate_id="Gate1",
        lot_id=1,
        is_entry=False
    )
    send_transponder_data(transponder_data, "transponder.queue", is_entry=False)

    time.sleep(5)


if __name__ == "__main__":
    main()
