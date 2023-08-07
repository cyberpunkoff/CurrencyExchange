<%@ page import="com.google.gson.Gson"%>
<%@ page import="ru.cyberpunkoff.currencyexchange.model.Message" %>
<%@ page contentType="application/json;charset=UTF-8" language="java" %>
<% String message = (String) request.getAttribute("jakarta.servlet.error.message");
    String jsonString = new Gson().toJson(new Message(message));
    out.print(jsonString);
%>
