package ru.cyberpunkoff.currencyexchange.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {

    public static final SQLiteConnection INSTANCE;

    static {
        try {
            INSTANCE = new SQLiteConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final Connection connection;

    private SQLiteConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:CurrencyExchange.db");
        // creating connection there...
    }

    public Connection getConnection() {
        return connection;
    }


}
