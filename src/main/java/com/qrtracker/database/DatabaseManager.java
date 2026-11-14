
package com.qrtracker.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class DatabaseManager {

    private static final String URL =
            "jdbc:sqlite:data/payment_tracker.db";

    public static Connection getConnection() throws SQLException {

        try {
            Files.createDirectories(Path.of("data"));
        } catch (IOException e) {
            throw new SQLException(
                    "Unable to create database folder", e);
        }

        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {

        try (Connection connection = getConnection()) {

            connection.setAutoCommit(false);

            try {
                if (!tableExists(connection, "payments")) {

                    createPaymentsTable(connection);

                } else if (!columnExists(connection, "fixed_amount")) {

                    if (tableExists(connection, "payments_legacy")) {
                        throw new SQLException(
                                "Backup table already exists. Migration stopped.");
                    }

                    try (Statement statement =
                            connection.createStatement()) {

                        statement.execute(
                                "ALTER TABLE payments RENAME TO payments_legacy");
                    }

                    createPaymentsTable(connection);

                    String migrationSQL = """
                            INSERT INTO payments
                            (id, fixed_amount, received_amount,
                             transaction_id, status, created_at)
                            SELECT id, '250', CAST(amount AS TEXT),
                                   transaction_id,
                                   CASE status
                                       WHEN 'VERIFIED' THEN 'SUCCESS'
                                       WHEN 'REJECTED' THEN 'FAILED'
                                       ELSE 'PENDING'
                                   END,
                                   created_at
                            FROM payments_legacy
                            """;

                    try (Statement statement =
                            connection.createStatement()) {

                        statement.executeUpdate(migrationSQL);
                    }

                    System.out.println(
                            "Previous payment records migrated successfully!");
                    System.out.println(
                            "Original records preserved in payments_legacy.");
                }

                // Add account holder name to existing databases
                if (!columnExists(connection, "account_holder_name")) {

                    try (Statement statement =
                            connection.createStatement()) {

                        statement.execute(
                                "ALTER TABLE payments ADD COLUMN account_holder_name TEXT");
                    }

                    System.out.println(
                            "Account holder name column added successfully!");
                }

                connection.commit();

                System.out.println(
                        "Payments table initialized successfully!");

            } catch (SQLException e) {

                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {

            System.out.println("Database initialization failed!");
            e.printStackTrace();
        }
    }

    private static void createPaymentsTable(
            Connection connection) throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS payments (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    fixed_amount TEXT NOT NULL,
                    received_amount TEXT,
                    transaction_id TEXT NOT NULL UNIQUE,
                    status TEXT NOT NULL,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    account_holder_name TEXT
                )
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private static boolean tableExists(
            Connection connection, String tableName)
            throws SQLException {

        String sql = """
                SELECT name FROM sqlite_master
                WHERE type = 'table' AND name = ?
                """;

        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setString(1, tableName);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    private static boolean columnExists(
            Connection connection, String columnName)
            throws SQLException {

        String sql = "PRAGMA table_info(payments)";

        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {

                if (columnName.equals(result.getString("name"))) {
                    return true;
                }
            }
        }

        return false;
    }
}
