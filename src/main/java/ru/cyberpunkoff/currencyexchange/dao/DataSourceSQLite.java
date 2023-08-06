package ru.cyberpunkoff.currencyexchange.dao;

import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

public class DataSourceSQLite {
    final static String URL = "jdbc:sqlite::resource:CurrencyExchange.db";

    private static final SQLiteDataSource dataSource;

    static {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl(URL);
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}
