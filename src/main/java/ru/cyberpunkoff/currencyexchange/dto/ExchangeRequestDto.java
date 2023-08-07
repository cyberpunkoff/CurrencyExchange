package ru.cyberpunkoff.currencyexchange.dto;

public class ExchangeRequestDto {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private double amount;

    public ExchangeRequestDto() {
    }

    public ExchangeRequestDto(String baseCurrencyCode, String targetCurrencyCode, double amount) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.amount = amount;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public void setTargetCurrencyCode(String targetCurrencyCode) {
        this.targetCurrencyCode = targetCurrencyCode;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
