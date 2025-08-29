# Webhook SQL Solver

A Spring Boot application that automatically solves SQL problems and submits solutions via webhook for the HealthRx hiring challenge.

## Features

- ✅ Automatically generates a webhook on startup
- ✅ Determines the SQL problem based on registration number (odd/even)
- ✅ Solves the SQL problem with proper implementations
- ✅ Submits the solution using JWT authentication
- ✅ Uses RestTemplate with proper configuration
- ✅ Runs automatically on startup without requiring controller endpoints
- ✅ Configurable through application properties

## Task Overview

This application implements the following workflow:

1. **On startup**: Sends a POST request to generate a webhook
2. **Problem determination**: Based on the response, determines which SQL problem to solve (odd/even registration number)
3. **Solution generation**: Solves the SQL problem and stores the result
4. **Submission**: Sends the solution (final SQL query) to the returned webhook URL using a JWT token

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Internet connection (to access webhook endpoints)

## Configuration

Edit `src/main/resources/application.properties` to configure the application:

```properties
# Student details - UPDATE THESE WITH YOUR INFORMATION
student.name=John Doe
student.regNo=REG12347
student.email=john@example.com

# Webhook endpoints (usually no need to change these)
webhook.generate.url=https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA
webhook.submit.url=https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA

# Timeout settings (in milliseconds)
rest.connection.timeout=5000
rest.read.timeout=5000

# Logging
logging.level.root=INFO
logging.level.com.healthrx=DEBUG
```

## Building the Application

1. Clone the repository
2. Navigate to the project root directory
3. Run the following command to build the application:

```bash
mvn clean package
```

The executable JAR file will be created in the `target` directory as `webhook-sql-solver-1.0.0.jar`.

## Running the Application

After building, run the application using:

```bash
java -jar target/webhook-sql-solver-1.0.0.jar
```

The application will automatically:
1. Generate a webhook with your details
2. Determine the SQL problem based on your registration number (odd/even)
3. Solve the problem using the implemented SQL solutions
4. Submit the solution to the provided webhook URL

## SQL Solutions Implemented

### Question 1 (Odd Registration Number)
Finds the highest salary that was credited to an employee, but only for transactions that were not made on the 1st day of any month. Returns employee details along with the salary.

### Question 2 (Even Registration Number)
Calculates the number of employees who are younger than each employee, grouped by their respective departments.

## Project Structure

```
src/main/java/com/healthrx/webhooksqlsolver/
├── WebhookSqlSolverApplication.java    # Main application class
├── config/
│   └── RestTemplateConfig.java         # RestTemplate configuration
├── model/
│   └── WebhookResponse.java            # Response model for webhook
└── service/
    ├── WebhookService.java             # Main service orchestrating the process
    └── SqlSolutionService.java         # SQL problem solutions
```

## Key Components

- **WebhookService**: Orchestrates the entire process from webhook generation to solution submission
- **SqlSolutionService**: Contains the actual SQL solutions for both questions
- **RestTemplateConfig**: Configures HTTP client with proper timeouts
- **WebhookResponse**: Model for parsing the webhook response

## Logging

Logs are written to the console. You can adjust the log level in `application.properties`:

```properties
logging.level.com.healthrx=DEBUG
```

## Troubleshooting

- If you encounter connection timeouts, increase the timeout values in `application.properties`
- Check the logs for detailed error messages
- Ensure you have a stable internet connection to access the webhook endpoints
- Verify your student details are correctly configured

## Submission Requirements

For submission, ensure you have:
- ✅ Public GitHub repository with code
- ✅ Final JAR output (`webhook-sql-solver-1.0.0.jar`)
- ✅ RAW downloadable GitHub link to the JAR
- ✅ Public JAR file link (downloadable)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
