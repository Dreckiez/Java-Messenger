# Messenger Clone ![Java](https://forthebadge.com/images/badges/made-with-java.svg)

## Requirements

- Java 25+
- The `lib/` folder included with the project
- PostgreSQL installed and running
- Maven or Maven Wrapper

## How to Run

### Backend
Run the Database Script (**IMPORTANT**) Before starting the backend, you must execute `SCRIPT.sql`. Steps:
1. Open pgAdmin or any PostgreSQL client.
2. Connect to your PostgreSQL server.
3. Run the SCRIPT.sql file to initialize required tables
4. Create the Database Create a PostgreSQL database that matches the name configured in application.properties.

**Example:**
```sql
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=yourpassword
```

Update the values based on your local setup. <br>
<br>
5. Configure application.properties Ensure these fields match your PostgreSQL setup:

```sql
spring.datasource.url=jdbc:postgresql://localhost:5432/
spring.datasource.username=
spring.datasource.password=
```

6.  Run the Backend <br>
Maven Wrapper Windows: `mvnw spring-boot:run` <br>
Mac/Linux: `./mvnw spring-boot:run` <br>
7.  Verify Server Go to: `http://localhost:8080`

### Frontend
1. Move into `frontend/`
2. Run the app by double-click `build.bat` (Windows) or `build.sh` (Mac/Linux) or run:
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
