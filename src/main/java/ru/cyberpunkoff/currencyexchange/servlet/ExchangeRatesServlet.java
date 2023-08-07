package ru.cyberpunkoff.currencyexchange.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.cyberpunkoff.currencyexchange.dao.CurrencyDao;
import ru.cyberpunkoff.currencyexchange.dao.CurrencyDaoImpl;
import ru.cyberpunkoff.currencyexchange.dao.ExchangeRateDao;
import ru.cyberpunkoff.currencyexchange.dao.ExchangeRateDaoImpl;
import ru.cyberpunkoff.currencyexchange.dto.ExchangeRateDto;
import ru.cyberpunkoff.currencyexchange.model.ExchangeRate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static ru.cyberpunkoff.currencyexchange.utils.ErrorMessageSender.sendError;
import static ru.cyberpunkoff.currencyexchange.utils.Renderer.renderResponse;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ExchangeRate> exchangeRates;
        ExchangeRateDao exchangeRate = new ExchangeRateDaoImpl();
        try {
            exchangeRates = exchangeRate.findAll();
        } catch (SQLException e) {
            resp.sendError(500, "Database error");
            return;
        }
        renderResponse(resp, exchangeRates);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRateDao exchangeRateDao = new ExchangeRateDaoImpl();
        CurrencyDao currencyDao = new CurrencyDaoImpl();

        try {
            String baseCurrencyCode = req.getParameter("baseCurrencyCode");
            String targetCurrencyCode = req.getParameter("targetCurrencyCode");
            String rate = req.getParameter("rate");

            if (baseCurrencyCode == null) {
                resp.sendError(400, "Base currency code field missing");
                return;
            }
            if (targetCurrencyCode == null) {
                resp.sendError(400, "Target currency code field missing");
                return;
            }
            if (rate == null) {
                resp.sendError(400, "Base currency code field missing");
                return;
            }

            ExchangeRateDto exchangeRateDto = new ExchangeRateDto();
            exchangeRateDto.setBaseCurrencyCode(baseCurrencyCode);
            exchangeRateDto.setTargetCurrencyCode(targetCurrencyCode);
            exchangeRateDto.setRate(Double.parseDouble(rate));

            if (currencyDao.findByCode(baseCurrencyCode) == null) {
                resp.sendError(409, "Base currency code does not exist");
                return;
            }
            if (currencyDao.findByCode(targetCurrencyCode) == null) {
                resp.sendError(409, "Target currency code does not exist");
                return;
            }

            ExchangeRate exchangeRate = exchangeRateDao.findByCurrencyPair(exchangeRateDto);
            if (exchangeRate != null) {
                sendError(resp, 409, "Currency pair exists");
                return;
            }

            int insertedId = exchangeRateDao.insertExchangeRate(exchangeRateDto);
            ExchangeRate insertedExchangeRate = exchangeRateDao.findById(insertedId);
            renderResponse(resp, insertedExchangeRate);
        } catch (SQLException e) {
            sendError(resp, 500, "Database error");
        }
    }
}
