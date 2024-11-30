import json
import requests
import time

GREEN = '\033[0;32m'
RED = '\033[0;31m'
NC = '\033[0m'

# RabbitMQ 服务器信息
RABBITMQ_USER = 'root'
RABBITMQ_PASS = 'root'
RABBITMQ_HOST = 'localhost'
RABBITMQ_PORT = 15672

# 打印提示信息
def print_note():
    print("\nNote: Sending visitor entry requests to the visitor-entry-request-exchange.")
    print(f"The returned message should either be {GREEN}success{NC} or {RED}failure{NC}, indicating the gate will be open or closed.\n")

# 发送访客进入请求
# 使用 REST API 将请求发布到指定的交换机，并获取响应
def send_visitor_entry_request(visitor_request, exchange_name):
    print(f"Sending visitor entry request with data: {visitor_request} to exchange: {exchange_name}")
    url_publish = f'http://{RABBITMQ_HOST}:{RABBITMQ_PORT}/api/exchanges/%2f/{exchange_name}/publish'
    payload = {
        "properties": {},
        "routing_key": "visitorEntryQueue",
        "payload": visitor_request,
        "payload_encoding": "string"
    }
    try:
        response = requests.post(url_publish, json=payload, auth=(RABBITMQ_USER, RABBITMQ_PASS))
        response.raise_for_status()
        print("Response from publish command:", response.text)
    except requests.RequestException as e:
        print(f"{RED}Failed to publish message to RabbitMQ. Error: {e}{NC}")
        return

    # 等待 1 秒以确保消息已被处理
    time.sleep(1)

    # 从响应队列中获取结果
    url_get = f'http://{RABBITMQ_HOST}:{RABBITMQ_PORT}/api/queues/%2f/gate.ctrl/get'
    payload_get = {
        "count": 1,
        "ackmode": "ack_requeue_false",
        "encoding": "auto"
    }
    try:
        result = requests.post(url_get, json=payload_get, auth=(RABBITMQ_USER, RABBITMQ_PASS))
        result.raise_for_status()
        messages = result.json()
        if not messages:
            print(f"{RED}No messages found in the queue.{NC}")
            return
        message_payload = messages[0].get('payload')
        if message_payload:
            if message_payload == "success":
                print(f"{GREEN}{message_payload}{NC}")
            else:
                print(f"{RED}{message_payload}{NC}")
        else:
            print(f"{RED}No payload found in the message.{NC}")
    except requests.RequestException as e:
        print(f"{RED}Failed to get message from RabbitMQ. Error: {e}{NC}")
        return

# 发送访客进入请求的新版本，使用不同的参数结构
def send_visitor_entry_request_v2(license_plate, is_valid, gate_id, exchange_name):
    visitor_request = json.dumps({
        "licensePlate": license_plate,
        "valid": is_valid,
        "gateId": gate_id
    })
    print(f"Sending visitor entry request with data: {visitor_request} to exchange: {exchange_name}")
    url_publish = f'http://{RABBITMQ_HOST}:{RABBITMQ_PORT}/api/exchanges/%2f/{exchange_name}/publish'
    payload = {
        "properties": {},
        "routing_key": "visitorEntryQueue",
        "payload": visitor_request,
        "payload_encoding": "string"
    }
    try:
        response = requests.post(url_publish, json=payload, auth=(RABBITMQ_USER, RABBITMQ_PASS))
        response.raise_for_status()
        print("Response from publish command:", response.text)
    except requests.RequestException as e:
        print(f"{RED}Failed to publish message to RabbitMQ. Error: {e}{NC}")
        return

    # 等待 1 秒以确保消息已被处理
    time.sleep(1)

    # 从响应队列中获取结果
    url_get = f'http://{RABBITMQ_HOST}:{RABBITMQ_PORT}/api/queues/%2f/gate.ctrl/get'
    payload_get = {
        "count": 1,
        "ackmode": "ack_requeue_false",
        "encoding": "auto"
    }
    try:
        result = requests.post(url_get, json=payload_get, auth=(RABBITMQ_USER, RABBITMQ_PASS))
        result.raise_for_status()
        messages = result.json()
        if not messages:
            print(f"{RED}No messages found in the queue.{NC}")
            return
        message_payload = messages[0].get('payload')
        if message_payload:
            if message_payload == "success":
                print(f"{GREEN}{message_payload}{NC}")
            else:
                print(f"{RED}{message_payload}{NC}")
        else:
            print(f"{RED}No payload found in the message.{NC}")
    except requests.RequestException as e:
        print(f"{RED}Failed to get message from RabbitMQ. Error: {e}{NC}")
        return

# 主函数，按顺序执行脚本中的功能
def main():
    # 打印提示信息
    print_note()
    # 发送访客进入请求和离开请求，模拟不同的场景
    # send_visitor_entry_request(json.dumps({"licensePlate": "ABC123", "entry": True}), "visitorEntryRequestExchange")
    # send_visitor_entry_request(json.dumps({"licensePlate": "XYZ789", "entry": False}), "visitorExitRequestExchange")
    send_visitor_entry_request_v2("DEF4561111", "true", "Gate1", "visitorToGateEntryResponseExchange")
    # send_visitor_entry_request_v2("GHI012", False, "Gate2", "visitorToGateExitRequestExchange")

# 运行主函数
if __name__ == "__main__":
    main()
