## How to Run the Project

You can deploy this project by running the `deploy.sh` script.

### Deploying the Project

To build the project and docker images, execute the `build.sh` script. 

To deploy services in docker containers, execute the `docker.sh` script.

`depoly.sh` calls `build.sh` and `docker.sh` in sequence.

### Environment Requirements

- **Java 23** (Project developed with Java 23, but should work with Java 17)
- **Maven** (`mvn clean package` and `mvn clean install` to build the project)
- **Docker** (`docker compose up` to start services)
- **Python 12** (Used during development for testing, but recent Python versions should work)

### Testing 

All unit tests will be triggered when building the project with `build.sh`.

#### Python Testing Script

The test folder contains Python test scripts for different functionalities:
- `student-entry-and-exit.py`: Tests the scenario when a car with a transponder enters the gate.
```bash
python3 student-entry-and-exit.py
```

- `violation-visitor-entry-and-exit.py`: Tests the scenario when a car with a violation exits the gate.
```bash 
python3 violation-visitor-entry-and-exit.py
```
- `visitor-entry-and-exit.py`: Tests the scenario when a visitor enters the gate.
```bash
python3 visitor-entry-and-exit.py
- ``` 
- `voucher-visitor-entry-and-exit.py`: Tests the exit of a visitor with a voucher applied.
```bash
python3 voucher-visitor-entry-and-exit.py
```



#### Testing Functionality with Postman

The following functionalities can be tested using Postman. Below are the corresponding curl commands for each test case:

##### Permit: 
-issue permit 
```bash
- curl --location 'http://localhost/permit/api/apply' \
  --header 'Content-Type: application/json' \
  --data '{
  "validFrom": "2024-12-01T00:00:00",
  "validUntil": "2024-12-31T00:00:00",
  "userId": 1,
  "licensePlate": "BBC123",
  "lotId": 10,
  "paymentMethod": "creditCard"
  }'
  ```
- renew permit 
```bash
- curl --location --request PUT 'http://localhost/permit/api/renew' \
  --header 'Content-Type: application/json' \
  --data '{
  "validFrom": "2024-12-01T00:00:00",
  "validUntil": "2024-12-31T00:00:00",
  "permitId": 1,
  "paymentMethod": "creditCard"
  }'
  ```


- Get valid permit count
```bash
curl --location 'http://localhost/permit/api/valid-permits' \
  --data ''
 ```
##### Violation:

- issue a ticket
```bash
curl --location 'http://localhost/violation/tickets/issue' \
--header 'Content-Type: application/json' \
--data '{
"licensePlate": "ABC123",
"fineAmount": 50.00,
"officerId": 12345,
"lotId": 67890
}'
```
- list existing tickets
```bash
curl --location 'http://localhost/violation/tickets/lookup?licensePlate=ABC123'
```
##### Gate:
- Check for parking lot statistics
```bash
curl --location 'http://localhost/gate/monitor/lookup?lotId=1' \
  --data ''
```

##### Payment:
- Apply a voucher 
```bash
curl --location --request POST 'http://localhost/payment/voucher/create' \
--header 'Content-Type: application/json' \
--data '{
    "voucher": "DISCOUNT2024",
    "validFrom": "2024-12-01T00:00:00",
    "validUntil": "2024-12-31T23:59:59"
}'
```