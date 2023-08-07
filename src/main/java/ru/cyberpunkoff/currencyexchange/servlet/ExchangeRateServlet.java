package ru.cyberpunkoff.currencyexchange.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.cyberpunkoff.currencyexchange.dao.ExchangeRateDao;
import ru.cyberpunkoff.currencyexchange.dao.ExchangeRateDaoImpl;
import ru.cyberpunkoff.currencyexchange.dto.ExchangeRateDto;
import ru.cyberpunkoff.currencyexchange.model.ExchangeRate;

import java.io.IOException;
import java.sql.SQLException;

import static ru.cyberpunkoff.currencyexchange.utils.Renderer.renderResponse;

@WebServlet(urlPatterns = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRateDao exchangeRateDao = new ExchangeRateDaoImpl();
        String urlPath = req.getPathInfo();
        ExchangeRateDto exchangeRateDto;
        try {
            exchangeRateDto = parseCurrenciesFromUrl(urlPath);
        } catch (IllegalArgumentException exception) {
            resp.sendError(400, "Currency codes not found");
            return;
        }

        ExchangeRate exchangeRate;
        try {
             exchangeRate = exchangeRateDao.findByCurrencyPair(exchangeRateDto);
        } catch (SQLException exception) {
            resp.sendError( 500, "Database error");
            return;
        }

        if (exchangeRate == null) {
            resp.sendError(404, "Exchange rate not found");
            return;
        }

        renderResponse(resp, exchangeRate);
    }

    private ExchangeRateDto parseCurrenciesFromUrl(String url) throws IOException {
        if (url == null || !url.matches("^/[A-Z]{6}$")) {
            throw new IllegalArgumentException();
        }
        ExchangeRateDto exchangeRateDto = new ExchangeRateDto();
        String currencies = url.split("/")[1];
        exchangeRateDto.setBaseCurrencyCode(currencies.substring(0, 3));
        exchangeRateDto.setTargetCurrencyCode(currencies.substring(3));
        System.out.println(exchangeRateDto);
        return exchangeRateDto;
    }
}
