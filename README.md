# Java PostgreSQL Sample Project

A simple Java Maven project that demonstrates connecting to PostgreSQL database, creating tables, inserting data, and retrieving information.

## Features

- Maven-based project structure
- PostgreSQL database connectivity using JDBC
- Automatic database and table creation
- Sample data insertion and retrieval
- Logging with SLF4J
- Uses Apache Commons Lang3 for string utilities

## Requirements

- Java 21 or higher
- Maven 3.6+
- PostgreSQL server running on localhost:5432
- PostgreSQL user `postgres` with password `password`

## Setup PostgreSQL

1. Install PostgreSQL if not already installed
2. Start PostgreSQL service
3. Create a user (if needed):
   ```sql
   CREATE USER postgres WITH PASSWORD 'password';
   ALTER USER postgres CREATEDB;
   ```

## Build and Run

1. **Compile the project:**
   ```bash
   mvn clean compile
   ```

2. **Run the application:**
   ```bash
   mvn exec:java
   ```

   Or alternatively:
   ```bash
   mvn exec:java -Dexec.mainClass="com.example.PostgreSQLSampleApp"
   ```

## What the Application Does

1. **Database Setup**: Creates a database named `sampledb` if it doesn't exist
2. **Table Creation**: Creates a `users` table with columns: id, name, email, created_at
3. **Data Insertion**: Inserts sample user records (avoids duplicates using ON CONFLICT)
4. **Data Retrieval**: Retrieves and displays all users from the database

## Database Configuration

The application connects to PostgreSQL using these default settings:
- **URL**: `jdbc:postgresql://localhost:5432/sampledb`
- **Username**: `postgres`
- **Password**: `password`

To change these settings, modify the constants in `PostgreSQLSampleApp.java`:
```java
private static final String DB_URL = "jdbc:postgresql://localhost:5432/sampledb";
private static final String DB_USER = "postgres";
private static final String DB_PASSWORD = "password";
```

## Dependencies

- **PostgreSQL JDBC Driver** (42.7.2): For database connectivity
- **SLF4J Simple** (2.0.12): For logging
- **Apache Commons Lang3** (3.14.0): For string utility functions

## Project Structure

```
java-maven-postgres-sample/
├── pom.xml                          # Maven configuration
├── README.md                        # Project documentation
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── example/
        │           └── PostgreSQLSampleApp.java  # Main application
        └── resources/               # Resources directory (empty)
```

## Sample Output

When you run the application successfully, you should see output similar to:

```
=== USERS IN DATABASE ===
============================================================
ID: 1   | Name: John Doe           | Email: john.doe@example.com      | Created: 2024-01-01 10:30:45.123
ID: 2   | Name: Jane Smith         | Email: jane.smith@example.com    | Created: 2024-01-01 10:30:45.124
ID: 3   | Name: Bob Johnson        | Email: bob.johnson@example.com   | Created: 2024-01-01 10:30:45.125
ID: 4   | Name: Alice Williams     | Email: alice.williams@example.com | Created: 2024-01-01 10:30:45.126
============================================================
Total users: 4
```

## Troubleshooting

### Common Issues:

1. **Connection refused**: Make sure PostgreSQL is running
2. **Authentication failed**: Check username/password
3. **Database does not exist**: The app will create it automatically
4. **Permission denied**: Make sure the postgres user has CREATE DATABASE privileges

### Testing Database Connection:

You can test your PostgreSQL connection manually:
```bash
psql -h localhost -p 5432 -U postgres -d postgres
```