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

@WebServlet(urlPatterns = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyDao currencyDao = new CurrencyDaoImpl();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        System.out.println(pathInfo);
        if (!pathInfo.matches("^/[A-Z]{3}$")) {
            response.sendError(400);
        }


        String[] parts = pathInfo.split("/");
        String currencyCode = parts[1];

        Currency currency = null;
        try {
            currency = currencyDao.findByCode(currencyCode);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (currency == null) {
            response.sendError(404);
        }

        String jsonString = this.gson.toJson(currency);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
    }
}
