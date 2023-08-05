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

import static ru.cyberpunkoff.currencyexchange.servlet.ErrorMessageSender.*;

@WebServlet(urlPatterns = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyDao currencyDao = new CurrencyDaoImpl();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        System.out.println(pathInfo);
        if (pathInfo == null || !pathInfo.matches("^/[A-Z]{3}$")) {
            sendError(response, 400, "Requested url is incorrect. Should be /currency/***");
            return;
        }


        String[] parts = pathInfo.split("/");
        String currencyCode = parts[1];

        Currency currency = null;
        try {
            currency = currencyDao.findByCode(currencyCode);
        } catch (SQLException e) {
            // sending error there
            sendError(response, 500, "Database error");
            throw new RuntimeException(e);
        }

        if (currency == null) {
            sendError(response, 404, "Currency not found");
            return;
        }

        String jsonString = this.gson.toJson(currency);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
    }
}
