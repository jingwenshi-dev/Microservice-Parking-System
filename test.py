import pika

# Define the RabbitMQ server connection parameters with credentials
connection_params = pika.ConnectionParameters(
    'localhost',
    5672,  # default RabbitMQ port
    '/',
    pika.PlainCredentials('root', 'root')  # replace with actual username and password
)

# Establish a connection to RabbitMQ server
connection = pika.BlockingConnection(connection_params)
channel = connection.channel()

# Declare the queue with the durable property set to True
queue_name = 'transponder.queue'
channel.queue_declare(queue=queue_name, durable=True)

# Define the message headers
headers = {
    'header_key_1': 'header_value_1',
    'header_key_2': 'header_value_2'
}

# Send the message to the queue with headers
message = '1'
properties = pika.BasicProperties(headers=headers)
channel.basic_publish(exchange='', routing_key=queue_name, body=message, properties=properties)
print(f" [x] Sent '{message}' to queue '{queue_name}' with headers {headers}")

# Close the connection
connection.close()