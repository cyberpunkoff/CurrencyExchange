package ru.cyberpunkoff.currencyexchange.utils;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class Renderer {
    public static void renderResponse(HttpServletResponse response, Object object) throws IOException {
        String jsonString = new Gson().toJson(object);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
    }
}
