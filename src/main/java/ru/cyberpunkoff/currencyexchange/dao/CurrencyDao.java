package ru.cyberpunkoff.currencyexchange.dao;

import ru.cyberpunkoff.currencyexchange.model.Currency;

import java.sql.SQLException;
import java.util.List;

public interface CurrencyDao {
    List<Currency> findAll() throws SQLException;

    long insertCurrency(Currency currency) throws SQLException;

    Currency findById(int id) throws SQLException;

    Currency findByCode(String code) throws SQLException;

    void update(Currency currency) throws SQLException;


}
