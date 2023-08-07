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

import static ru.cyberpunkoff.currencyexchange.utils.Renderer.renderResponse;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String amount = req.getParameter("amount");

        if (baseCurrencyCode == null) {
            resp.sendError(400, "From parameter missing");
            return;
        }
        if (targetCurrencyCode == null) {
            resp.sendError(400, "To parameter missing");
            return;
        }
        if (amount == null) {
            resp.sendError(400, "Amount parameter missing");
            return;
        }

        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto();
        exchangeRequestDto.setAmount(Double.parseDouble(amount));
        exchangeRequestDto.setTargetCurrencyCode(targetCurrencyCode);
        exchangeRequestDto.setBaseCurrencyCode(baseCurrencyCode);

        try {
            ExchangeResponse exchangeResponse = new ExchangeService().exchange(exchangeRequestDto);
            if (exchangeResponse == null) {
                resp.sendError(404, "No such exchange rate");
                return;
            }
            renderResponse(resp, exchangeResponse);
        } catch (SQLException e) {
            resp.sendError(500, "Database error");
        }
    }
}
