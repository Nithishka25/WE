package com.example;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class ProductServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ecommerce_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "kani1625"; // change to your DB password

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("product_name");
        String category = request.getParameter("category");
        double price = Double.parseDouble(request.getParameter("price"));
        int stock = Integer.parseInt(request.getParameter("stock"));

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            // Insert into DB
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO products (product_name, category, price, stock) VALUES (?, ?, ?, ?)"
            );
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.executeUpdate();

            // Fetch all products
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products");

            // Output HTML
            out.println("<html><head><title>Product Added</title>");
            out.println("<style>");
            out.println("body { font-family: Arial; text-align: center; background: #f0f0f0; }");
            out.println(".container { margin: 50px auto; width: 80%; }");
            out.println(".message { color: green; font-size: 20px; margin: 20px 0; }");
            out.println("table { margin: 0 auto; border-collapse: collapse; width: 100%; background: #fff; }");
            out.println("th, td { border: 1px solid #ccc; padding: 10px; }");
            out.println("th { background-color: #e0e0e0; }");
            out.println(".back-link { margin-top: 20px; display: inline-block; text-decoration: none; color: #007bff; }");
            out.println("</style></head><body>");

            out.println("<div class='container'>");
            out.println("<div class='message'>✅ Product added successfully!</div>");
            out.println("<h3>Product List</h3>");
            out.println("<table>");
            out.println("<tr><th>ID</th><th>Name</th><th>Category</th><th>Price</th><th>Stock</th></tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getString("product_name") + "</td>");
                out.println("<td>" + rs.getString("category") + "</td>");
                out.println("<td>" + rs.getDouble("price") + "</td>");
                out.println("<td>" + rs.getInt("stock") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("<a class='back-link' href='index.html'>➕ Add Another Product</a>");
            out.println("</div></body></html>");

            conn.close();

        } catch (Exception e) {
            out.println("<h3 style='color:red;'>❌ Error: " + e.getMessage() + "</h3>");
        }
    }
}
