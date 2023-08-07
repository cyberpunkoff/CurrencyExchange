package ru.cyberpunkoff.currencyexchange.servlet;

import com.google.common.base.Splitter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.cyberpunkoff.currencyexchange.dao.ExchangeRateDao;
import ru.cyberpunkoff.currencyexchange.dao.ExchangeRateDaoImpl;
import ru.cyberpunkoff.currencyexchange.dto.ExchangeRateDto;
import ru.cyberpunkoff.currencyexchange.model.ExchangeRate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Map;

import static ru.cyberpunkoff.currencyexchange.utils.Renderer.renderResponse;

@WebServlet(urlPatterns = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            super.service(req, resp);
            return;
        }
        this.doPatch(req, resp);
    }

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
            resp.sendError(500, "Database error");
            return;
        }

        if (exchangeRate == null) {
            resp.sendError(404, "Exchange rate not found");
            return;
        }

        renderResponse(resp, exchangeRate);
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRateDao exchangeRateDao = new ExchangeRateDaoImpl();
        try {
            String rate = null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()))) {
                String data = br.readLine();
                Map<String, String> dataMap = Splitter.on('&')
                        .trimResults()
                        .withKeyValueSeparator(
                                Splitter.on('=')
                                        .limit(2)
                                        .trimResults())
                        .split(data);
                rate = dataMap.get("rate");
            } catch (IOException ex) {
                rate = null;
            }

            if (rate == null) {
                resp.sendError(400, "Rate field missing");
                return;
            }

            String urlPath = req.getPathInfo();
            ExchangeRateDto exchangeRateDto;
            try {
                exchangeRateDto = parseCurrenciesFromUrl(urlPath);
                exchangeRateDto.setRate(Double.parseDouble(rate));
            } catch (IllegalArgumentException exception) {
                resp.sendError(400, "Currency codes not found");
                return;
            }

            ExchangeRate exchangeRate = exchangeRateDao.updateExchangeRate(exchangeRateDto);
            if (exchangeRate == null) {
                resp.sendError(404, "Currency pair not found");
                return;
            }
            renderResponse(resp, exchangeRate);
        } catch (SQLException exception) {
            resp.sendError(500, "Database error");
        }
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
