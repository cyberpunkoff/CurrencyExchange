package ru.cyberpunkoff.currencyexchange.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import ru.cyberpunkoff.currencyexchange.model.Currency;
import ru.cyberpunkoff.currencyexchange.model.CurrencyPair;

import java.io.IOException;
import java.text.ParseException;

@WebServlet(urlPatterns = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

    private CurrencyPair parseCurrenciesFromUrl(String url) throws IOException {
        if (!url.matches("^/[A-Z]{6}$")) {
            throw new IllegalArgumentException();
        }

        CurrencyPair currencyPair = new CurrencyPair();
        String currencies = url.split("/")[1];

        currencyPair.setBaseCurrencyCode(currencies.substring(0, 2));
        currencyPair.setTargetCurrencyCode(currencies.substring(3));

        return currencyPair;
    }
}
