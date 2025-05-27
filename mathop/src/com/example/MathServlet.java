package com.example;
import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

public class MathServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        double num1 = Double.parseDouble(request.getParameter("num1"));
        double num2 = Double.parseDouble(request.getParameter("num2"));
        String operation = request.getParameter("operation");

        double result = 0;
        String opSymbol = "";

        switch (operation) {
            case "add":
                result = num1 + num2;
                opSymbol = "+";
                break;
            case "sub":
                result = num1 - num2;
                opSymbol = "-";
                break;
            case "mul":
                result = num1 * num2;
                opSymbol = "*";
                break;
            case "div":
                if (num2 != 0) {
                    result = num1 / num2;
                    opSymbol = "/";
                } else {
                    out.println("<h3>Error: Cannot divide by zero.</h3>");
                    return;
                }
                break;
        }

        out.println("<h2>Result</h2>");
        out.println("<p>" + num1 + " " + opSymbol + " " + num2 + " = " + result + "</p>");
        out.println("<a href='index.html'>Back</a>");
    }
}