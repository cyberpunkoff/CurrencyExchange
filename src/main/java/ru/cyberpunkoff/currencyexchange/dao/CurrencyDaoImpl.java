package ru.cyberpunkoff.currencyexchange.dao;

import ru.cyberpunkoff.currencyexchange.model.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDaoImpl implements CurrencyDao {

    /*private final SQLiteConnection sqLiteConnection;

    public CurrencyDaoImpl() {
        this.sqLiteConnection = SQLiteConnection.INSTANCE;
    }*/

    public List<Currency> findAll() throws SQLException {

        String sql = "SELECT id, code, full_name, sign from currency";
        List<Currency> currencies = new ArrayList<>();

        Connection connection = DataSourceSQLite.getDataSource().getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            currencies.add(parseCurrencyFromResult(resultSet));
        }

        return currencies;
    }

    @Override
    public long insertCurrency(Currency currency) throws SQLException {
        return 0;
    }

    @Override
    public Currency findById(int id) throws SQLException {
        return null;
    }

    @Override
    public Currency findByCode(String code) throws SQLException {


        String sql = "SELECT * FROM currency WHERE code = ?";
        Connection connection = DataSourceSQLite.getDataSource().getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, code);
        ResultSet resultSet = ps.executeQuery();
        if (!resultSet.next()) {
            return null;
        }
        return parseCurrencyFromResult(resultSet);
    }

    @Override
    public void update(Currency currency) throws SQLException {

    }

    private Currency parseCurrencyFromResult(ResultSet resultSet) throws SQLException {

        Currency currency = new Currency();
        currency.setId(resultSet.getInt("id"));
        currency.setCode(resultSet.getString("code"));
        currency.setName(resultSet.getString("full_name"));
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
