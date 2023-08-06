package ru.cyberpunkoff.currencyexchange.dao;

import org.apache.commons.lang3.tuple.Pair;
import ru.cyberpunkoff.currencyexchange.model.Currency;
import ru.cyberpunkoff.currencyexchange.model.ExchangeRate;

import java.sql.SQLException;
import java.util.List;

public interface ExchangeRateDao {
    List<ExchangeRate> findAll() throws SQLException;

    ExchangeRate findByCurrencyPair(Pair<Currency, Currency> currencyPair);



}
