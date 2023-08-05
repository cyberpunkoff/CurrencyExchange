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
import ru.cyberpunkoff.currencyexchange.model.Currency;
import ru.cyberpunkoff.currencyexchange.model.ExchangeRate;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

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
}
