package ru.cyberpunkoff.currencyexchange.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.cyberpunkoff.currencyexchange.dao.CurrencyDao;
import ru.cyberpunkoff.currencyexchange.model.Currency;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "currenciesServlet", value = "/currencies")
public class CurrenciesServlet extends HttpServlet {


    private final CurrencyDao currencyDao = new CurrencyDao();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {

        Currency currency1 = new Currency(228, "RUB", "Russian Rouble", "R");
        Currency currency2 = new Currency(1337, "USD", "American Dollar", "S");
        Currency currency3 = new Currency(6969, "EUR", "Euro", "E");

        //List<Currency> currencyList = List.of(currency1, currency2, currency3);

        List<Currency> currencyList = null;
        try {
            currencyList = currencyDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String currencyJsonString = this.gson.toJson(currencyList);

        PrintWriter out = response.getWriter();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(currencyJsonString);
        out.flush();
    }
}
