package ru.cyberpunkoff.currencyexchange.servlet;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import ru.cyberpunkoff.currencyexchange.model.Message;

import java.io.IOException;
import java.io.PrintWriter;

public class ErrorMessageSender {

    private Message message;
    private int statusCode;
    private HttpServletResponse response;

    public ErrorMessageSender(Message message, int statusCode, HttpServletResponse response) {
        this.message = message;
        this.statusCode = statusCode;
        this.response = response;
    }

    public static void sendError(HttpServletResponse response, int errorCode, String message) throws IOException {
        String jsonString = new Gson().toJson(new Message(message));
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode);
        out.print(jsonString);
        out.flush();
    }


}
