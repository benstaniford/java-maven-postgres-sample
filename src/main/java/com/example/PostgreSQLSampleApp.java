package com.example;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLSampleApp {
    
    private static final Logger logger = LoggerFactory.getLogger(PostgreSQLSampleApp.class);
    
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/sampledb";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "password";
    
    public static void main(String[] args) {
        PostgreSQLSampleApp app = new PostgreSQLSampleApp();
        
        try {
            logger.info("Starting PostgreSQL Sample Application");
            
            app.createDatabase();
            app.createTable();
            app.insertSampleData();
            app.retrieveAndDisplayData();
            
            logger.info("Application completed successfully");
            
        } catch (Exception e) {
            logger.error("Application failed: " + e.getMessage(), e);
            System.exit(1);
        }
    }
    
    private void createDatabase() throws SQLException {
        String adminUrl = "jdbc:postgresql://localhost:5432/postgres";
        
        try (Connection conn = DriverManager.getConnection(adminUrl, DB_USER, DB_PASSWORD)) {
            logger.info("Connected to PostgreSQL server");
            
            String checkDbQuery = "SELECT 1 FROM pg_database WHERE datname = 'sampledb'";
            try (PreparedStatement stmt = conn.prepareStatement(checkDbQuery);
                 ResultSet rs = stmt.executeQuery()) {
                
                if (!rs.next()) {
                    try (Statement createDbStmt = conn.createStatement()) {
                        createDbStmt.execute("CREATE DATABASE sampledb");
                        logger.info("Database 'sampledb' created successfully");
                    }
                } else {
                    logger.info("Database 'sampledb' already exists");
                }
            }
        }
    }
    
    private void createTable() throws SQLException {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                email VARCHAR(150) UNIQUE NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(createTableSQL);
            logger.info("Table 'users' created or already exists");
        }
    }
    
    private void insertSampleData() throws SQLException {
        String insertSQL = "INSERT INTO users (name, email) VALUES (?, ?) ON CONFLICT (email) DO NOTHING";
        
        String[][] sampleUsers = {
            {"John Doe", "john.doe@example.com"},
            {"Jane Smith", "jane.smith@example.com"},
            {"Bob Johnson", "bob.johnson@example.com"},
            {"Alice Williams", "alice.williams@example.com"}
        };
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
            
            int insertedCount = 0;
            for (String[] user : sampleUsers) {
                stmt.setString(1, user[0]);
                stmt.setString(2, user[1]);
                insertedCount += stmt.executeUpdate();
            }
            
            logger.info("Inserted {} new user records", insertedCount);
        }
    }
    
    private void retrieveAndDisplayData() throws SQLException {
        String selectSQL = "SELECT id, name, email, created_at FROM users ORDER BY id";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {
            
            List<User> users = new ArrayList<>();
            
            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getTimestamp("created_at")
                );
                users.add(user);
            }
            
            logger.info("Retrieved {} users from database", users.size());
            
            System.out.println("\n=== USERS IN DATABASE ===");
            System.out.println(StringUtils.repeat("=", 60));
            
            for (User user : users) {
                System.out.printf("ID: %-3d | Name: %-20s | Email: %-25s | Created: %s%n",
                    user.getId(),
                    StringUtils.abbreviate(user.getName(), 20),
                    user.getEmail(),
                    user.getCreatedAt());
            }
            
            System.out.println(StringUtils.repeat("=", 60));
            System.out.println("Total users: " + users.size());
        }
    }
    
    private static class User {
        private final int id;
        private final String name;
        private final String email;
        private final Timestamp createdAt;
        
        public User(int id, String name, String email, Timestamp createdAt) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.createdAt = createdAt;
        }
        
        public int getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public Timestamp getCreatedAt() { return createdAt; }
    }
}