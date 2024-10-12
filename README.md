## How to Run the Project

You can deploy this project by running the `deploy.sh` script.

### Deploying the Project

To build and deploy the project, execute the `deploy.sh` script. 

### Environment Requirements

- **Java 23** (Project developed with Java 23, but should work with Java 17)
- **Maven** (`mvn clean package` to build the project)
- **Docker** (`docker compose up` to start services)
- **Python 12** (Used during development for testing, but recent Python versions should work)

### Testing

To run tests, execute `test.sh`. The script performs two simple test cases:

- Valid input: `"1"` (a valid transponder id)
- Invalid input: any other number (an invalid transponder id)

#### Requirements for Running Tests

- Python is required for `test.sh` as it uses `rabbitmqadmin`, a Python script to publish messages to the RabbitMQ
  server.
- Ensure the system is fully prepared before running tests to avoid potential connection errors.
