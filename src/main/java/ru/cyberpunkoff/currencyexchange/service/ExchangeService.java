package ru.cyberpunkoff.currencyexchange.service;

import ru.cyberpunkoff.currencyexchange.dao.ExchangeRateDao;
import ru.cyberpunkoff.currencyexchange.dao.ExchangeRateDaoImpl;
import ru.cyberpunkoff.currencyexchange.dto.ExchangeRateDto;
import ru.cyberpunkoff.currencyexchange.dto.ExchangeRequestDto;
import ru.cyberpunkoff.currencyexchange.model.ExchangeRate;
import ru.cyberpunkoff.currencyexchange.model.ExchangeResponse;

import java.sql.SQLException;

public class ExchangeService {

    ExchangeRateDao exchangeRateDao = new ExchangeRateDaoImpl();
    public ExchangeResponse exchange(ExchangeRequestDto exchangeRequestDto) throws SQLException {

        // Case: requested exchange rate exists
        ExchangeRateDto exchangeRateDto = new ExchangeRateDto();
        exchangeRateDto.setBaseCurrencyCode(exchangeRequestDto.getBaseCurrencyCode());
        exchangeRateDto.setTargetCurrencyCode(exchangeRequestDto.getTargetCurrencyCode());
        ExchangeRate exchangeRate = exchangeRateDao.findByCurrencyPair(exchangeRateDto);

        ExchangeResponse exchangeResponse = new ExchangeResponse();
        exchangeResponse.setAmount(exchangeRequestDto.getAmount());
        exchangeResponse.setBaseCurrency(exchangeRate.getBaseCurrency());
        exchangeResponse.setTargetCurrency(exchangeRate.getTargetCurrency());
        exchangeResponse.setRate(exchangeRate.getRate());
        exchangeResponse.setConvertedAmount(exchangeRequestDto.getAmount() * exchangeRate.getRate());
        return exchangeResponse;


        // Case: requested exchange rate does not exist, but inverted does
        // TODO: implement case

        // Case: requested exchange rate does not exist but can covert through USD
        // TODO: implement case
    }
}
