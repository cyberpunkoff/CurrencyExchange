package ru.cyberpunkoff.currencyexchange.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.cyberpunkoff.currencyexchange.dao.CurrencyDao;
import ru.cyberpunkoff.currencyexchange.dao.CurrencyDaoImpl;
import ru.cyberpunkoff.currencyexchange.model.Currency;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.cyberpunkoff.currencyexchange.utils.ErrorMessageSender.sendError;
import static ru.cyberpunkoff.currencyexchange.utils.Renderer.renderResponse;

@WebServlet(name = "currenciesServlet", value = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        CurrencyDao currencyDao = new CurrencyDaoImpl();
        List<Currency> currencyList = new ArrayList<>();
        try {
            currencyList = currencyDao.findAll();
        } catch (SQLException e) {
            response.sendError(500, "Database error");
        }
        renderResponse(response, currencyList);
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
                resp.sendError( 409, "Currency exists");
                return;
            }
            int id = currencyDao.insertCurrency(currency);
            currency.setId(id);
        } catch (SQLException e) {
            resp.sendError(500, "Database error");
            return;
        }
        renderResponse(resp, currency);
    }
}
