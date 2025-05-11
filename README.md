# Loan API

This document provides instructions on how to build, run, and interact with the Loan API.

---

### 🛠️ Build the Project

To build the project, make sure you have Java 21 installed.

1. Clone the repository:
    ```bash
    git clone https://github.com/mertkeski/bank-loan-api.git
    cd bank-loan-api
    ```

2. Build the project:
    ```bash
    ./mvnw clean package
    ```

This will download the necessary dependencies and compile the project.

---

### 🚀 Run the Project

You can run the project using the Spring Boot plugin. You have two options to run the project:

#### Option 1: Running via Maven

```bash
./mvnw spring-boot:run
```

This will start the server on `http://localhost:8080`.

#### Option 2: Running the JAR file (after building)

After building the project, you can run the JAR file directly.

```bash
java -jar target/bank-loan-api-0.0.1-SNAPSHOT.jar
```

This will also start the server on `http://localhost:8080`.

---

### Accessing the H2 Database

The project uses an in-memory H2 database. It will be created on the fly when the application is started. The data is reset every time the application restarts.
If you'd like to view the H2 database directly, you can access the H2 console:

1. Open your browser and go to:
    ```
    http://localhost:8080/h2-console
    ```

2. Enter the following settings:
    - **JDBC URL**: `jdbc:h2:mem:loandb`
    - **User Name**: `mert`
    - **Password**: `pass`

3. Click **Connect**.

The H2 console provides an interface to view and interact with the in-memory database.

---

### API Usage Examples

To see a set of example API calls using **cURL**, refer to the [**curl-examples.md**](./curl-examples.md) file. This document includes common API request and response examples to help you interact with the Loan API endpoints.




