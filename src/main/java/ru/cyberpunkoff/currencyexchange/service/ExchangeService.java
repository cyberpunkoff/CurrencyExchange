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
    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDaoImpl();
    private final CurrencyDao currencyDao = new CurrencyDaoImpl();

    public ExchangeResponse exchange(ExchangeRequestDto exchangeRequestDto) throws SQLException {
        ExchangeRateDto exchangeRateDto = new ExchangeRateDto();
        exchangeRateDto.setBaseCurrencyCode(exchangeRequestDto.getBaseCurrencyCode());
        exchangeRateDto.setTargetCurrencyCode(exchangeRequestDto.getTargetCurrencyCode());
        ExchangeRate exchangeRate = findExchangeRate(exchangeRateDto);
        if (exchangeRate == null) {
            return null;
        }
        ExchangeResponse exchangeResponse = new ExchangeResponse();
        exchangeResponse.setAmount(exchangeRequestDto.getAmount());
        exchangeResponse.setBaseCurrency(exchangeRate.getBaseCurrency());
        exchangeResponse.setTargetCurrency(exchangeRate.getTargetCurrency());
        exchangeResponse.setRate(exchangeRate.getRate());
        exchangeResponse.setConvertedAmount(exchangeRequestDto.getAmount() * exchangeRate.getRate());
        return exchangeResponse;
    }

    public ExchangeRate findExchangeRate(ExchangeRateDto exchangeRateDto) throws SQLException {
        ExchangeRate exchangeRate;

        // Case: requested exchange rate exists
        System.out.println("Trying direct convert...");
        exchangeRate = exchangeRateDao.findByCurrencyPair(exchangeRateDto);
        if (exchangeRate != null) {
            return exchangeRate;
        }

        // Case: requested exchange rate does not exist, but inverted does
        System.out.println("Trying reverse convert...");
        exchangeRate = exchangeRateDao.findByCurrencyPair(new ExchangeRateDto
                (
                        exchangeRateDto.getTargetCurrencyCode(),
                        exchangeRateDto.getBaseCurrencyCode()
                ));
        if (exchangeRate != null) {
            return new ExchangeRate(
                    exchangeRate.getTargetCurrency(),
                    exchangeRate.getBaseCurrency(),
                    1 / exchangeRate.getRate()
            );
        }

        if (exchangeRateDto.getBaseCurrencyCode().equals("USD") ||
                exchangeRateDto.getTargetCurrencyCode().equals("USD")) {
            return null;
        }

        // Case: requested exchange rate does not exist but can covert through USD
        System.out.println("Trying USD convert...");
        ExchangeRate exchangeRateToUsd = findExchangeRate(
                new ExchangeRateDto(exchangeRateDto.getBaseCurrencyCode(), "USD")
        );
        ExchangeRate exchangeRateFromUsd = findExchangeRate(
                new ExchangeRateDto("USD", exchangeRateDto.getTargetCurrencyCode())
        );

        if (exchangeRateFromUsd == null || exchangeRateToUsd == null) {
            return null; // Not possible to convert
        }
        exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCurrency(currencyDao.findByCode(exchangeRateDto.getBaseCurrencyCode()));
        exchangeRate.setTargetCurrency(currencyDao.findByCode(exchangeRateDto.getTargetCurrencyCode()));
        exchangeRate.setRate(exchangeRateToUsd.getRate() * exchangeRateFromUsd.getRate());
        return exchangeRate;
    }
}
