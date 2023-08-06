package ru.cyberpunkoff.currencyexchange.dao;

import ru.cyberpunkoff.currencyexchange.model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDaoImpl implements CurrencyDao {

    public List<Currency> findAll() throws SQLException {
        String sql = "SELECT id, code, full_name, sign from currency";

        try (Connection connection = DataSourceSqlite.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet resultSet = ps.executeQuery()) {
            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                currencies.add(parseCurrencyFromResult(resultSet));
            }
            return currencies;
        }
    }

    @Override
    public int insertCurrency(Currency currency) throws SQLException {
        String sql = "INSERT INTO currency (code, full_name, sign) VALUES (?, ?, ?)";

        try (Connection connection = DataSourceSqlite.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, currency.getCode());
            ps.setString(2, currency.getName());
            ps.setString(3, currency.getSign());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                // maybe I should close result set as well
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    @Override
    public Currency findById(int id) throws SQLException {
        return null;
    }

    @Override
    public Currency findByCode(String code) throws SQLException {
        String sql = "SELECT * FROM currency WHERE code = ?";

        try (Connection connection = DataSourceSqlite.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            ResultSet resultSet = ps.executeQuery(); // maybe I should close result set there
            if (resultSet.next()) {
                return parseCurrencyFromResult(resultSet);
            }
            return null;
        }
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
}
