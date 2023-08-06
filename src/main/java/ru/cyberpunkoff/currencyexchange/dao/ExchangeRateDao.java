package ru.cyberpunkoff.currencyexchange.dao;

import ru.cyberpunkoff.currencyexchange.dto.ExchangeRateDto;
import ru.cyberpunkoff.currencyexchange.model.ExchangeRate;

import java.sql.SQLException;
import java.util.List;

public interface ExchangeRateDao {
    List<ExchangeRate> findAll() throws SQLException;

    ExchangeRate findByCurrencyPair(ExchangeRateDto exchangeRateDto) throws SQLException;

    ExchangeRate findById(int id) throws SQLException;

    int insertExchangeRate(ExchangeRateDto exchangeRate) throws SQLException;


}
