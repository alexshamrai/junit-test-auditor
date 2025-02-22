# JUnit Test Auditor

JUnit Test Auditor is a Java-based application using Spring Boot and Maven. It is designed to audit JUnit tests, specifically focusing on identifying and reporting disabled tests within a project.

## Features

- **Scan Project Directory**: Scans a specified project directory for JUnit tests.
- **Identify Disabled Tests**: Identifies disabled tests and collects information about them.
- **Generate Reports**: Writes the collected information to a CSV file and generates an HTML report summarizing the test information.

## Project Structure

### `JunitTestAuditorApplication.java`
- The main entry point of the application.
- Implements `CommandLineRunner` to execute code after the Spring Boot application starts.
- Aggregates test information, writes it to CSV, and generates an HTML report.

### `TestInfoAggregator.java`
- Aggregates test information from the specified project path.
- Uses `TestsParser` to list method calls and identify disabled tests.

### `application.properties`
- Contains configuration properties such as the path to the project with tests and working directory.

## Configuration

The application is configured using the `application.properties` file:

```properties
# Local path to the project with tests
test.project.path=/Users/alexshamrai/super-tests

# Working directory for generated files
working.directory=target/

# Tags to exclude from the audit
excluded.tags=ACCEPTANCE,OWNER,DISABLED,SMOKE,COMMON
```

## Usage
Clone the repository:
```bash
git clone <repository-url>
cd junit-test-auditor
```
Update the `application.properties` file with the path to your project and other configurations. Build the project using Maven:
```sh
mvn clean install
```
Run the application:
```sh
java -jar target/junit-test-auditor-0.0.1-SNAPSHOT.jar
```
