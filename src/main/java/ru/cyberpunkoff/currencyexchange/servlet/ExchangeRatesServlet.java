package ru.cyberpunkoff.currencyexchange.servlet;

import com.google.gson.Gson;
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
import ru.cyberpunkoff.currencyexchange.model.Currency;
import ru.cyberpunkoff.currencyexchange.model.ExchangeRate;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import static ru.cyberpunkoff.currencyexchange.servlet.ErrorMessageSender.sendError;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private final ExchangeRateDao exchangeRate = new ExchangeRateDaoImpl();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ExchangeRate> exchangeRates = null;
        try {
            exchangeRates = exchangeRate.findAll();
        } catch (SQLException e) {
            //throw new RuntimeException(e);
            response.setStatus(500);
        }

        String jsonString = this.gson.toJson(exchangeRates);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRateDao exchangeRateDao = new ExchangeRateDaoImpl();

        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        if (baseCurrencyCode == null) {
            sendError(resp, 400, "Base currency code field missing");
            return;
        }
        if (targetCurrencyCode == null) {
            sendError(resp, 400, "Target currency code field missing");
            return;
        }
        if (rate == null) {
            sendError(resp, 400, "Base currency code field missing");
            return;
        }

        //TODO: may be add check when currency code doesnt exists

        ExchangeRateDto exchangeRateDto = new ExchangeRateDto();
        exchangeRateDto.setBaseCurrencyCode(baseCurrencyCode);
        exchangeRateDto.setTargetCurrencyCode(targetCurrencyCode);
        exchangeRateDto.setRate(Double.parseDouble(rate));

        try {
            ExchangeRate exchangeRate = exchangeRateDao.findByCurrencyPair(exchangeRateDto);
            if (exchangeRate != null) {
                sendError(resp, 409, "Currency pair exists");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendError(resp, 500, "Database error");
            return;
        }

        ExchangeRate insertedExchangeRate;

        try {
            int insertedId = exchangeRateDao.insertExchangeRate(exchangeRateDto);
            insertedExchangeRate = exchangeRateDao.findById(insertedId);
            //System.out.println(insertedExchangeRate);
        } catch (SQLException e) {
            e.printStackTrace();
            sendError(resp, 500, "Database error");
            return;
        }

        String jsonString = new Gson().toJson(insertedExchangeRate);
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
    }
}
