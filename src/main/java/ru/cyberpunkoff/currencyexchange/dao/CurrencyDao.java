package ru.cyberpunkoff.currencyexchange.dao;

import ru.cyberpunkoff.currencyexchange.model.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDao {

    private final SQLiteConnection sqLiteConnection;

    public CurrencyDao() {
        this.sqLiteConnection = SQLiteConnection.INSTANCE;
    }

    public List<Currency> findAll() throws SQLException {

        List<Currency> currencies = new ArrayList<>();
        Connection connection = sqLiteConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQLTask.GET_ALL_CURRENCIES.QUERY);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            currencies.add(parseCurrencyFromResult(resultSet));
        }

        return currencies;
    }

    private Currency parseCurrencyFromResult(ResultSet resultSet) throws SQLException {

        Currency currency = new Currency();
        currency.setId(resultSet.getInt("id"));
        currency.setCode(resultSet.getString("code"));
        currency.setFullName(resultSet.getString("full_name"));
        currency.setSign(resultSet.getString("sign"));

        return currency;
    }

    enum SQLTask {
        //INSERT_GOAL("INSERT INTO goals (name, description) VALUES ((?), (?))"),
        //GET_GOAL_BY_ID("SELECT  goals.id, goals.name , goals.description from goals where id = (?)"),
        //DELETE_GOAL_BY_ID("DELETE from goals where id=(?)"),
        //UPDATE_GOAL_BY_ID("UPDATE goals set name=(?), description=(?) where id=(?)"),
        GET_ALL_CURRENCIES("SELECT id, code, full_name, sign from currency");

        final String QUERY;

        SQLTask(String QUERY) {
            this.QUERY = QUERY;
        }
    }

}
