package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/calculate")
public class CalculatorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Display form for input
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h2>Simple Calculator</h2>");
        out.println("<form method='post' action='calculate'>");
        out.println("Number 1: <input type='text' name='num1' required/><br/>");
        out.println("Number 2: <input type='text' name='num2' required/><br/>");
        out.println("Operation: <select name='operation'>");
        out.println("<option value='add'>Add (+)</option>");
        out.println("<option value='subtract'>Subtract (-)</option>");
        out.println("<option value='multiply'>Multiply (*)</option>");
        out.println("<option value='divide'>Divide (/)</option>");
        out.println("</select><br/><br/>");
        out.println("<input type='submit' value='Calculate' />");
        out.println("</form>");
        out.println("</body></html>");

        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String num1Str = request.getParameter("num1");
        String num2Str = request.getParameter("num2");
        String operation = request.getParameter("operation");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            double num1 = Double.parseDouble(num1Str);
            double num2 = Double.parseDouble(num2Str);

            double result;
            String opSymbol;

            switch (operation) {
                case "add":
                    result = num1 + num2;
                    opSymbol = "+";
                    break;
                case "subtract":
                    result = num1 - num2;
                    opSymbol = "-";
                    break;
                case "multiply":
                    result = num1 * num2;
                    opSymbol = "*";
                    break;
                case "divide":
                    if (num2 == 0) {
                        out.println("<html><body>");
                        out.println("<p style='color:red;'>Error: Cannot divide by zero!</p>");
                        out.println("<a href='calculate'>Try Again</a>");
                        out.println("</body></html>");
                        out.close();
                        return;
                    }
                    result = num1 / num2;
                    opSymbol = "/";
                    break;
                default:
                    out.println("<html><body>");
                    out.println("<p style='color:red;'>Unknown operation.</p>");
                    out.println("<a href='calculate'>Try Again</a>");
                    out.println("</body></html>");
                    out.close();
                    return;
            }

            out.println("<html><body>");
            out.println("<h2>Result:</h2>");
            out.printf("<p>%.4f %s %.4f = %.4f</p>", num1, opSymbol, num2, result);
            out.println("<a href='calculate'>Perform Another Calculation</a>");
            out.println("</body></html>");

        } catch (NumberFormatException e) {
            out.println("<html><body>");
            out.println("<p style='color:red;'>Invalid input! Please enter valid numbers.</p>");
            out.println("<a href='calculate'>Try Again</a>");
            out.println("</body></html>");
        }

        out.close();
    }
}
