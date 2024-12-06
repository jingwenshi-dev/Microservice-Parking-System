import json
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


    debug_log(f"Declaring exchange: {GATE_EXCHANGE}")
    channel.exchange_declare(exchange=GATE_EXCHANGE, exchange_type='direct', durable=True)

    debug_log(f"Declaring queue: {QUEUE_NAME}")
    channel.queue_declare(queue=QUEUE_NAME, durable=True)

    debug_log(f"Binding queue: {QUEUE_NAME} to exchange: {GATE_EXCHANGE} with routingKey: {routing_key}")
    channel.queue_bind(exchange=GATE_EXCHANGE, queue=QUEUE_NAME, routing_key=routing_key)

    debug_log(f"Listening for messages on queue: {QUEUE_NAME} with routingKey: {routing_key}")

    def callback(ch, method, properties, body):

        try:
            debug_log(f"Received raw message: {body}")
            decoded_body = body.decode('utf-8')
            debug_log(f"Decoded message: {decoded_body}")


            try:
                message = json.loads(decoded_body)
                if isinstance(message, dict) and "isValid" in message:
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


def send_transponder_data(transponder_dto, queue_name):
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


def main():

    transponder_data = build_transponder_dto(
        transponder_id="97ae1bb0-b42a-41db-8cfc-fd2ba3bbf008",
        license_plate="AAAAAA",
        gate_id="Gate1",
        lot_id=1,
        is_entry=True
    )
    send_transponder_data(transponder_data, "transponder.queue")



    transponder_data = build_transponder_dto(
        transponder_id="97ae1bb0-b42a-41db-8cfc-fd2ba3bbf008",
        license_plate="AAAAAA",
        gate_id="Gate1",
        lot_id=1,
        is_entry=False
    )
    send_transponder_data(transponder_data, "transponder.queue")
    receive_gate_control_message("Gate1")


if __name__ == "__main__":
    main()
