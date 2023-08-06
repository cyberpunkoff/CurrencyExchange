package ru.cyberpunkoff.currencyexchange.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.cyberpunkoff.currencyexchange.dto.ExchangeRequestDto;
import ru.cyberpunkoff.currencyexchange.model.ExchangeResponse;
import ru.cyberpunkoff.currencyexchange.service.ExchangeService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String amount = req.getParameter("amount");

        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto();
        exchangeRequestDto.setAmount(Double.parseDouble(amount));
        exchangeRequestDto.setTargetCurrencyCode(targetCurrencyCode);
        exchangeRequestDto.setBaseCurrencyCode(baseCurrencyCode);


        try {
            ExchangeResponse exchangeResponse = new ExchangeService().exchange(exchangeRequestDto);
            // TODO: a lot of code is copied may be static method for sending response?
            String jsonString = new Gson().toJson(exchangeResponse);
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(jsonString);
            out.flush();
        } catch (SQLException e) {
            // TODO: data validation (not null parametrs)
            throw new RuntimeException(e);
        }
    }
}
