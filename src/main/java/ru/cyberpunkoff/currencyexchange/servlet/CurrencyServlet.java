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

import static ru.cyberpunkoff.currencyexchange.utils.ErrorMessageSender.*;
import static ru.cyberpunkoff.currencyexchange.utils.Renderer.renderResponse;

@WebServlet(urlPatterns = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CurrencyDao currencyDao = new CurrencyDaoImpl();
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || !pathInfo.matches("^/[A-Z]{3}$")) {
            response.sendError(400, "Requested url is incorrect. Should be /currency/***");
            return;
        }

        String[] parts = pathInfo.split("/");
        String currencyCode = parts[1];

        Currency currency;
        try {
            currency = currencyDao.findByCode(currencyCode);
        } catch (SQLException e) {
            // sending error there
            response.sendError(500, "Database error");
            return;
        }

        if (currency == null) {
            response.sendError(404, "Currency not found");
        }

        renderResponse(response, currency);
    }
}
