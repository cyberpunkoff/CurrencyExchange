package ru.cyberpunkoff.currencyexchange.dao;

import ru.cyberpunkoff.currencyexchange.model.Currency;
import ru.cyberpunkoff.currencyexchange.model.ExchangeRate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDaoImpl implements ExchangeRateDao {

    @Override
    public List<ExchangeRate> findAll() throws SQLException {

        List<ExchangeRate> exchangeRates = new ArrayList<>();

        String sql = "select exchange_rate.id, exchange_rate.rate, bc.id bc_id, bc.full_name bc_name, bc.code bc_code, bc.sign bc_sign, " +
                "tc.id tc_id, tc.full_name tc_name, tc.code tc_code, tc.sign tc_sign from exchange_rate inner join  " +
                "currency bc on base_currency_id = bc.id inner join currency tc on target_currency_id = tc.id";
        Connection connection = DataSourceSQLite.getDataSource().getConnection();
        ResultSet rs = connection.createStatement().executeQuery(sql);

        while (rs.next()) {
            exchangeRates.add(parseExchangeRateFromResult(rs));
        }

        return exchangeRates;
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
        exchangeRate.setId(rs.getInt(rs.getInt("id")));

        return exchangeRate;
    }
}
