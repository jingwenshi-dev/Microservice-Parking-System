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
