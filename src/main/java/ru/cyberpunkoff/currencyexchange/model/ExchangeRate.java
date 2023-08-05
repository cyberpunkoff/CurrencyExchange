package ru.cyberpunkoff.currencyexchange.model;

public class ExchangeRate {
    private Integer id;
    private Integer base_currency_id;
    private Integer target_currency_id;
    private Double rate;

    public ExchangeRate() {
    }

    public ExchangeRate(Integer id, Integer base_currency_id, Integer target_currency_id, Double rate) {
        this.id = id;
        this.base_currency_id = base_currency_id;
        this.target_currency_id = target_currency_id;
        this.rate = rate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBase_currency_id() {
        return base_currency_id;
    }

    public void setBase_currency_id(Integer base_currency_id) {
        this.base_currency_id = base_currency_id;
    }

    public Integer getTarget_currency_id() {
        return target_currency_id;
    }

    public void setTarget_currency_id(Integer target_currency_id) {
        this.target_currency_id = target_currency_id;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
