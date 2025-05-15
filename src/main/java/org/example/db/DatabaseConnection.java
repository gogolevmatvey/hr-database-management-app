package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String connectionUrl = "jdbc:sqlserver://localhost;databaseName=hrdepartment;user=admin;password=admin;encrypt=true;trustServerCertificate=true;";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName(JDBC_DRIVER);
                connection = DriverManager.getConnection(connectionUrl);
            } catch (ClassNotFoundException e) {
                throw new SQLException("SQL Server JDBC Driver not found", e);
            }
        }
        return connection;
    }
}
