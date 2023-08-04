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

@WebServlet(urlPatterns = "/currencies")
public class CurrenciesServlet extends HttpServlet {


    private final CurrencyDao currencyDao = new CurrencyDao();
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {

        Currency currency = new Currency(228, "RUB", "Russian Rouble", "R");

        String currencyJsonString = this.gson.toJson(currency);

        PrintWriter out = response.getWriter();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(currencyJsonString);
        out.flush();
    }
}
