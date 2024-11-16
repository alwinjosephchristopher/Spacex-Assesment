
# SpaceX Application

This project is developed to process and analyze SpaceX launch data and provide insights about launches grouped by year and launch site. The task was completed using Spring Boot with a reactive programming approach for performance optimization. Below is the detailed documentation.

---

## Why This Approach?

While there are multiple ways to solve this problem, I chose the **Spring Boot Reactive** approach to leverage non-blocking asynchronous API calls and efficient data processing pipelines. This ensures scalability and high performance, especially when dealing with external API calls and large datasets. Additionally, reactive streams combined with Java's Stream API provide clean and maintainable code.

---

## How to Run the Project

### Prerequisites
1. **JDK 11+**
2. **Maven 3.8+**
3. **Lombok Plugin**:
   - Ensure Lombok annotations are enabled in your IDE.
   - For IntelliJ IDEA, enable "Enable annotation processing" in the settings under `Build, Execution, Deployment > Compiler > Annotation Processors`.

### Steps to Run
1. Clone this repository:
   ```bash
   git clone <repository_url>
   cd spacex-application
   ```
2. Build the project using Maven:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
   Alternatively, you can run the generated JAR:
   ```bash
   java -jar target/spacex-0.0.1-SNAPSHOT.jar
   ```

### Default Configuration
- **Application Name**: `SpacexApplication`
- **Port**: `8080`
- Base URI: `http://localhost:8080`

---

## API Documentation

### Base URI
`http://localhost:8080`

### API Endpoints
#### 1. Get Rocket Launches by Year
- **Endpoint**: `/task/rocket/launches-by-year`
- **Method**: `GET`
- **Response**: A nested map containing rocket names/type as keys, with each mapped to another map of years and the corresponding launch counts.

#### Sample Response:
```json
{
  "Falcon 9": {
    "2020": 26,
    "2021": 31
  },
  "Falcon Heavy": {
    "2018": 3,
    "2022": 1
  }
}
```

---

#### 2. Get Rocket Launches by Launch Site
- **Endpoint**: `/task/rocket/launches-by-site`
- **Method**: `GET`
- **Response**: A nested map containing rocket names/type as keys, with each mapped to another map of launch sites and the corresponding launch counts.

#### Sample Response:
```json
{
  "Falcon 9": {
    "CCAFS SLC 40": 45,
    "KSC LC 39A": 52
  },
  "Falcon Heavy": {
    "KSC LC 39A": 3
  }
}
```

---

## Application Workflow

1. **Fetch Data**: The application hits the [SpaceX API](https://github.com/r-spacex/SpaceX-API) to fetch launch data, rocket and launchpad details.
2. **Process Data**: Using reactive programming and Java Streams, the data is grouped and aggregated:
   - Group by rocket name and year to calculate yearly launch counts.
   - Group by rocket name and launch site to calculate site-based launch counts.
3. **Expose APIs**: The processed data is made available via the above endpoints.

---

## Key Features

- **Reactive Programming**: Uses Spring WebFlux for non-blocking and efficient API calls.
- **Stream API**: Processes and groups data cleanly and efficiently.
- **Unit Tests**: A sanity pack of unit tests is included to validate the core functionalities.

---

## Running Unit Tests

To run the unit tests:
```bash
mvn test
```

---

## Sample Usage

### Using `curl`
```bash
curl -X GET http://localhost:8080/task/rocket/launches-by-year
curl -X GET http://localhost:8080/task/rocket/launches-by-site
```
---

## Additional Notes

- **API Rate Limiting**: The SpaceX API may have request rate limits. During development, a local JSON server was used for testing to avoid hitting the actual API multiple times.
- **Port Configuration**: If port `8080` is unavailable, modify the `application.properties` file to set a custom port:
  ```properties
  server.port=9090
  ```
- **Extensibility**: The application is designed to be easily extendable for other analytics tasks using the same reactive approach.

---

Thank you for evaluating my submission. I look forward to discussing the implementation details during the interview.
