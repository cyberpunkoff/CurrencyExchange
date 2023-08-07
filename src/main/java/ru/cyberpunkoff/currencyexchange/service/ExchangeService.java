package ru.cyberpunkoff.currencyexchange.service;

import ru.cyberpunkoff.currencyexchange.dao.CurrencyDao;
import ru.cyberpunkoff.currencyexchange.dao.CurrencyDaoImpl;
import ru.cyberpunkoff.currencyexchange.dao.ExchangeRateDao;
import ru.cyberpunkoff.currencyexchange.dao.ExchangeRateDaoImpl;
import ru.cyberpunkoff.currencyexchange.dto.ExchangeRateDto;
import ru.cyberpunkoff.currencyexchange.dto.ExchangeRequestDto;
import ru.cyberpunkoff.currencyexchange.model.ExchangeRate;
import ru.cyberpunkoff.currencyexchange.model.ExchangeResponse;

import java.sql.SQLException;

public class ExchangeService {
    ExchangeRateDao exchangeRateDao = new ExchangeRateDaoImpl();
    CurrencyDao currencyDao = new CurrencyDaoImpl();

    public ExchangeResponse exchange(ExchangeRequestDto exchangeRequestDto) throws SQLException {
        ExchangeRate exchangeRate;

        // Case: requested exchange rate exists
        ExchangeRateDto exchangeRateDto = new ExchangeRateDto();
        exchangeRateDto.setBaseCurrencyCode(exchangeRequestDto.getBaseCurrencyCode());
        exchangeRateDto.setTargetCurrencyCode(exchangeRequestDto.getTargetCurrencyCode());
        exchangeRate = exchangeRateDao.findByCurrencyPair(exchangeRateDto);

        // Case: requested exchange rate does not exist, but inverted does
        if (exchangeRate == null) {
            exchangeRateDto.setBaseCurrencyCode(exchangeRequestDto.getTargetCurrencyCode());
            exchangeRateDto.setTargetCurrencyCode(exchangeRequestDto.getBaseCurrencyCode());
            exchangeRate = exchangeRateDao.findByCurrencyPair(exchangeRateDto);
            if (exchangeRate != null) {
                ExchangeRate tempExchangeRate = new ExchangeRate();
                tempExchangeRate.setRate(1 / exchangeRate.getRate());
                tempExchangeRate.setBaseCurrency(exchangeRate.getTargetCurrency());
                tempExchangeRate.setTargetCurrency(exchangeRate.getBaseCurrency());
                exchangeRate = tempExchangeRate;
            }
        }

        // Case: requested exchange rate does not exist but can covert through USD
        if (exchangeRate == null) {
            ExchangeRateDto exchangeRateToUsdDto = new ExchangeRateDto();
            exchangeRateToUsdDto.setBaseCurrencyCode(exchangeRequestDto.getBaseCurrencyCode());
            exchangeRateToUsdDto.setTargetCurrencyCode("USD");
            ExchangeRate exchangeRateToUsd = exchangeRateDao.findByCurrencyPair(exchangeRateToUsdDto);

            ExchangeRateDto exchangeRateFromUsdDto = new ExchangeRateDto();
            exchangeRateFromUsdDto.setBaseCurrencyCode("USD");
            exchangeRateFromUsdDto.setTargetCurrencyCode(exchangeRequestDto.getTargetCurrencyCode());
            ExchangeRate exchangeRateFromUsd = exchangeRateDao.findByCurrencyPair(exchangeRateFromUsdDto);

            if (exchangeRateFromUsd == null || exchangeRateToUsd == null) {
                return null; // Not possible to convert
            }

            exchangeRate = new ExchangeRate();
            exchangeRate.setBaseCurrency(currencyDao.findByCode(exchangeRequestDto.getBaseCurrencyCode()));
            exchangeRate.setTargetCurrency(currencyDao.findByCode(exchangeRequestDto.getTargetCurrencyCode()));
            exchangeRate.setRate(exchangeRateToUsd.getRate() * exchangeRateFromUsd.getRate());
        }

        ExchangeResponse exchangeResponse = new ExchangeResponse();
        exchangeResponse.setAmount(exchangeRequestDto.getAmount());
        exchangeResponse.setBaseCurrency(exchangeRate.getBaseCurrency());
        exchangeResponse.setTargetCurrency(exchangeRate.getTargetCurrency());
        exchangeResponse.setRate(exchangeRate.getRate());
        exchangeResponse.setConvertedAmount(exchangeRequestDto.getAmount() * exchangeRate.getRate());
        return exchangeResponse;
    }
}
