package ru.cyberpunkoff.currencyexchange.dao;

import ru.cyberpunkoff.currencyexchange.model.Currency;
import ru.cyberpunkoff.currencyexchange.dto.ExchangeRateDto;
import ru.cyberpunkoff.currencyexchange.model.ExchangeRate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDaoImpl implements ExchangeRateDao {

    @Override
    public List<ExchangeRate> findAll() throws SQLException {
        String sql = "SELECT exchange_rate.id, exchange_rate.rate, bc.id bc_id, bc.full_name bc_name, bc.code bc_code, bc.sign bc_sign, " +
                "tc.id tc_id, tc.full_name tc_name, tc.code tc_code, tc.sign tc_sign from exchange_rate INNER JOIN  " +
                "currency bc ON base_currency_id = bc.id INNER JOIN currency tc ON target_currency_id = tc.id";

        try (Connection connection = DataSourceSqlite.getDataSource().getConnection();
             ResultSet rs = connection.createStatement().executeQuery(sql)) {
            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (rs.next()) {
                exchangeRates.add(parseExchangeRateFromResult(rs));
            }
            return exchangeRates;
        }
    }

    @Override
    public ExchangeRate findByCurrencyPair(ExchangeRateDto exchangeRateDto) throws SQLException {
        String sql = "SELECT exchange_rate.id, exchange_rate.rate, bc.id bc_id, bc.full_name bc_name, bc.code bc_code, bc.sign bc_sign, " +
                "tc.id tc_id, tc.full_name tc_name, tc.code tc_code, tc.sign tc_sign from exchange_rate INNER JOIN  " +
                "currency bc ON base_currency_id = bc.id INNER JOIN currency tc ON target_currency_id = tc.id WHERE " +
                "bc_code = ? AND tc_code = ?";

        try (Connection connection = DataSourceSqlite.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, exchangeRateDto.getBaseCurrencyCode());
            ps.setString(2, exchangeRateDto.getTargetCurrencyCode());
            ResultSet resultSet = ps.executeQuery(); // close result set as well?
            if (resultSet.next()) {
                return parseExchangeRateFromResult(resultSet);
            }
            return null;
        }
    }

    @Override
    public ExchangeRate findById(int id) throws SQLException {
        String sql = "SELECT exchange_rate.id, exchange_rate.rate, bc.id bc_id, bc.full_name bc_name, bc.code bc_code, bc.sign bc_sign, " +
                "tc.id tc_id, tc.full_name tc_name, tc.code tc_code, tc.sign tc_sign from exchange_rate INNER JOIN  " +
                "currency bc ON base_currency_id = bc.id INNER JOIN currency tc ON target_currency_id = tc.id WHERE " +
                "exchange_rate.id = ?";

        try (Connection connection = DataSourceSqlite.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery(); // maybe I should close result set as well
            if (resultSet.next()) {
                return parseExchangeRateFromResult(resultSet);
            }
            return null;
        }
    }

    @Override
    public int insertExchangeRate(ExchangeRateDto exchangeRateDto) throws SQLException {
        String sql = "insert into exchange_rate (base_currency_id, target_currency_id, rate) values " +
                "((select id from currency where code = ?), (select id from currency where code = ?), ?)";

        try (Connection connection = DataSourceSqlite.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, exchangeRateDto.getBaseCurrencyCode());
            ps.setString(2, exchangeRateDto.getTargetCurrencyCode());
            ps.setDouble(3, exchangeRateDto.getRate());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys(); // Maybe I should close result set there as well (try-with-resource)
            return rs.getInt(1);
        }
    }

    private ExchangeRate parseExchangeRateFromResult(ResultSet rs) throws SQLException {
        Currency baseCurrency = new Currency();
        baseCurrency.setId(rs.getInt("bc_id"));
        baseCurrency.setCode(rs.getString("bc_code"));
        baseCurrency.setName(rs.getString("bc_name"));
        baseCurrency.setSign(rs.getString("bc_sign"));

        Currency targetCurrency = new Currency();
        targetCurrency.setId(rs.getInt("tc_id"));
        targetCurrency.setCode(rs.getString("tc_code"));
        targetCurrency.setName(rs.getString("tc_name"));
        targetCurrency.setSign(rs.getString("tc_sign"));

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCurrency(baseCurrency);
        exchangeRate.setTargetCurrency(targetCurrency);
        exchangeRate.setRate(rs.getDouble("rate"));
        exchangeRate.setId(rs.getInt("id"));

        return exchangeRate;
    }
}
