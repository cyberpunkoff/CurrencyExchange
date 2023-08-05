package ru.cyberpunkoff.currencyexchange.dao;

import ru.cyberpunkoff.currencyexchange.model.ExchangeRate;

import java.sql.SQLException;
import java.util.List;

public interface ExchangeRateDao {
    List<ExchangeRate> findAll() throws SQLException;
}
