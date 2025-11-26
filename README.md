# Java - Messenger Clone

## Requirements

- Java 25+
- The `lib/` folder included with the project

## How to Run

1. backend

```bash
1.  Requirements

-   JDK 25
-   PostgreSQL installed and running
-   Maven or Maven Wrapper

2.  Run the Database Script (IMPORTANT) Before starting the backend, you
    must execute SCRIPT.sql. Steps:

3.  Open pgAdmin or any PostgreSQL client.

4.  Connect to your PostgreSQL server.

5.  Run the SCRIPT.sql file to initialize required tables

6.  Create the Database Create a PostgreSQL database that matches the
    name configured in application.properties.

Example:
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=yourpassword

Update the values based on your local setup.

4.  Configure application.properties Ensure these fields match your
    PostgreSQL setup:

spring.datasource.url=jdbc:postgresql://localhost:5432/
spring.datasource.username=
spring.datasource.password=

5.  Run the Backend

Method 1 – Maven Wrapper Windows: mvnw spring-boot:run

Mac/Linux: ./mvnw spring-boot:run

6.  Verify Server Go to: http://localhost:8080
```

2. frontend

Run the app by double-click `build.bat` (Windows) or `build.sh` (Mac/Linux) or run:

```bash
# for Windows
./build.bat


# for Mac/Linux
chmod +x build.sh
./build.sh
```

This will:

1. Compile source files into /out
2. Copy assets into /out/assets
3. Run the application using `java -cp`
