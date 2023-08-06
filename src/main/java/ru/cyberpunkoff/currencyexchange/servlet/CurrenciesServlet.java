package ru.cyberpunkoff.currencyexchange.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.cyberpunkoff.currencyexchange.dao.CurrencyDao;
import ru.cyberpunkoff.currencyexchange.dao.CurrencyDaoImpl;
import ru.cyberpunkoff.currencyexchange.model.Currency;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import static ru.cyberpunkoff.currencyexchange.servlet.ErrorMessageSender.sendError;

@WebServlet(name = "currenciesServlet", value = "/currencies")
public class CurrenciesServlet extends HttpServlet {




    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {

        //Currency currency1 = new Currency(228, "RUB", "Russian Rouble", "R");
        //Currency currency2 = new Currency(1337, "USD", "American Dollar", "S");
        //Currency currency3 = new Currency(6969, "EUR", "Euro", "E");
        //List<Currency> currencyList = List.of(currency1, currency2, currency3);

        CurrencyDao currencyDao = new CurrencyDaoImpl();

        List<Currency> currencyList = null;
        try {
            currencyList = currencyDao.findAll();
        } catch (SQLException e) {
            //throw new RuntimeException(e);
            response.setStatus(500);
        }

        String jsonString = new Gson().toJson(currencyList);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CurrencyDao currencyDao = new CurrencyDaoImpl();

        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign  = req.getParameter("sign");

        if (name == null) {
            sendError(resp, 400, "Name field missing");
            return;
        }
        if (code == null) {
            sendError(resp, 400, "Code field missing");
            return;
        }
        if (sign == null) {
            sendError(resp, 400, "Sign field missing");
            return;
        }

        Currency currency = new Currency();
        currency.setName(name);
        currency.setCode(code);
        currency.setSign(sign);

        try {
            if (currencyDao.findByCode(code) != null) {
                sendError(resp, 409, "Currency exists");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendError(resp, 400, "Database error");
            return;
        }

        try {
            int id = currencyDao.insertCurrency(currency);
            currency.setId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            sendError(resp, 400, "Database error");
            return;
        }

        String jsonString = new Gson().toJson(currency);
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();


    }
}
