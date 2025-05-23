package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ecommerce_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "kani1625"; // Update this

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String nameFilter = request.getParameter("filterName");
        String categoryFilter = request.getParameter("filterCategory");
        String priceMinFilter = request.getParameter("priceMin");
        String priceMaxFilter = request.getParameter("priceMax");
        String stockMinFilter = request.getParameter("stockMin");
        String stockMaxFilter = request.getParameter("stockMax");

        HttpSession session = request.getSession();
        // Clear filters if 'clear' button clicked
        if (request.getParameter("clear") != null) {
            session.removeAttribute("nameFilter");
            session.removeAttribute("categoryFilter");
            session.removeAttribute("priceMinFilter");
            session.removeAttribute("priceMaxFilter");
            session.removeAttribute("stockMinFilter");
            session.removeAttribute("stockMaxFilter");
        } else {
            if (nameFilter != null)
                session.setAttribute("nameFilter", nameFilter);
            if (categoryFilter != null)
                session.setAttribute("categoryFilter", categoryFilter);
            if (priceMinFilter != null)
                session.setAttribute("priceMinFilter", priceMinFilter);
            if (priceMaxFilter != null)
                session.setAttribute("priceMaxFilter", priceMaxFilter);
            if (stockMinFilter != null)
                session.setAttribute("stockMinFilter", stockMinFilter);
            if (stockMaxFilter != null)
                session.setAttribute("stockMaxFilter", stockMaxFilter);
        }

        // Retrieve from session to maintain values across requests
        nameFilter = (String) session.getAttribute("nameFilter");
        categoryFilter = (String) session.getAttribute("categoryFilter");
        priceMinFilter = (String) session.getAttribute("priceMinFilter");
        priceMaxFilter = (String) session.getAttribute("priceMaxFilter");
        stockMinFilter = (String) session.getAttribute("stockMinFilter");
        stockMaxFilter = (String) session.getAttribute("stockMaxFilter");

        out.println("<html><head><title>Product Management</title><style>");
        out.println("body { font-family: Arial, sans-serif; background: #f0f2f5; margin: 0; padding: 0; }");
        out.println(
                ".container { max-width: 900px; margin: 50px auto; background: white; padding: 30px; box-shadow: 0 0 15px rgba(0,0,0,0.1); border-radius: 10px; }");
        out.println("h2 { color: #333; text-align: center; margin-bottom: 20px; }");

        out.println(
                "form { display: flex; flex-wrap: wrap; justify-content: center; gap: 15px; margin-bottom: 30px; }");
        out.println(
                "input[type=text], input[type=number] { padding: 10px; border: 1px solid #ccc; border-radius: 5px; width: 180px; }");
        out.println(
                "button { padding: 12px 25px; border: none; border-radius: 5px; background-color: #4CAF50; color: white; cursor: pointer; font-size: 16px; transition: background-color 0.3s ease; }");
        out.println("button:hover { background-color: #45a049; }");

        out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        out.println("th, td { padding: 12px; text-align: center; border-bottom: 1px solid #ddd; }");
        out.println("th { background-color: #f9fafb; color: #555; }");
        out.println("tr:hover { background-color: #f1f7fd; }");

        out.println(".message { color: green; font-weight: bold; margin-bottom: 25px; text-align: center; }");
        out.println(".filter-info { text-align: center; margin-bottom: 20px; font-style: italic; color: #666; }");

        out.println("@media (max-width: 600px) {");
        out.println("  form { flex-direction: column; align-items: center; }");
        out.println("  input[type=text], input[type=number] { width: 90%; }");
        out.println("  button { width: 90%; }");
        out.println("}");
        out.println("</style></head><body>");
        out.println("<div class='container'>");

        String message = request.getParameter("message");
        if (message != null) {
            out.println("<div class='message'>" + message + "</div>");
        }

        // Add Product Form
        out.println("<h2>Add Product</h2>");
        out.println("<form method='post' action='products'>");
        out.println("<input type='text' name='product_name' placeholder='Product Name' required>");
        out.println("<input type='text' name='category' placeholder='Category' required>");
        out.println("<input type='number' step='0.01' name='price' placeholder='Price' required min='0'>");
        out.println("<input type='number' name='stock' placeholder='Stock' required min='0'>");
        out.println("<button type='submit'>Add Product</button>");
        out.println("</form>");

        // Filter Form with min/max for price and stock
        out.println("<h2>Filter Products</h2>");
        out.println("<form method='get' action='products'>");
        out.println("<input type='text' name='filterName' placeholder='Filter by Name' value='"
                + (nameFilter != null ? nameFilter : "") + "'>");
        out.println("<input type='text' name='filterCategory' placeholder='Filter by Category' value='"
                + (categoryFilter != null ? categoryFilter : "") + "'>");
        out.println("<input type='number' step='0.01' min='0' name='priceMin' placeholder='Price Min' value='"
                + (priceMinFilter != null ? priceMinFilter : "") + "'>");
        out.println("<input type='number' step='0.01' min='0' name='priceMax' placeholder='Price Max' value='"
                + (priceMaxFilter != null ? priceMaxFilter : "") + "'>");
        out.println("<input type='number' min='0' name='stockMin' placeholder='Stock Min' value='"
                + (stockMinFilter != null ? stockMinFilter : "") + "'>");
        out.println("<input type='number' min='0' name='stockMax' placeholder='Stock Max' value='"
                + (stockMaxFilter != null ? stockMaxFilter : "") + "'>");
        out.println("<button type='submit'>Apply Filter</button>");
        out.println(
                "<button type='submit' name='clear' value='true' style='background:#f44336;'>Clear Filters</button>");
        out.println("</form>");

        // Show current filters info
        if (nameFilter != null || categoryFilter != null || priceMinFilter != null || priceMaxFilter != null
                || stockMinFilter != null || stockMaxFilter != null) {
            out.println("<div class='filter-info'>Current Filters: ");
            if (nameFilter != null && !nameFilter.isEmpty())
                out.print("Name = <strong>" + nameFilter + "</strong>; ");
            if (categoryFilter != null && !categoryFilter.isEmpty())
                out.print("Category = <strong>" + categoryFilter + "</strong>; ");
            if (priceMinFilter != null && !priceMinFilter.isEmpty())
                out.print("Price Min = <strong>" + priceMinFilter + "</strong>; ");
            if (priceMaxFilter != null && !priceMaxFilter.isEmpty())
                out.print("Price Max = <strong>" + priceMaxFilter + "</strong>; ");
            if (stockMinFilter != null && !stockMinFilter.isEmpty())
                out.print("Stock Min = <strong>" + stockMinFilter + "</strong>; ");
            if (stockMaxFilter != null && !stockMaxFilter.isEmpty())
                out.print("Stock Max = <strong>" + stockMaxFilter + "</strong>; ");
            out.println("</div>");
        }

        // Build SQL query with filters
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            String sql = "SELECT * FROM products";
            List<String> conditions = new ArrayList<>();
            if (nameFilter != null && !nameFilter.isEmpty())
                conditions.add("product_name LIKE ?");
            if (categoryFilter != null && !categoryFilter.isEmpty())
                conditions.add("category LIKE ?");
            if (priceMinFilter != null && !priceMinFilter.isEmpty())
                conditions.add("price >= ?");
            if (priceMaxFilter != null && !priceMaxFilter.isEmpty())
                conditions.add("price <= ?");
            if (stockMinFilter != null && !stockMinFilter.isEmpty())
                conditions.add("stock >= ?");
            if (stockMaxFilter != null && !stockMaxFilter.isEmpty())
                conditions.add("stock <= ?");

            if (!conditions.isEmpty()) {
                sql += " WHERE " + String.join(" AND ", conditions);
            }

            PreparedStatement ps = conn.prepareStatement(sql);

            int idx = 1;
            if (nameFilter != null && !nameFilter.isEmpty())
                ps.setString(idx++, "%" + nameFilter + "%");
            if (categoryFilter != null && !categoryFilter.isEmpty())
                ps.setString(idx++, "%" + categoryFilter + "%");
            if (priceMinFilter != null && !priceMinFilter.isEmpty())
                ps.setDouble(idx++, Double.parseDouble(priceMinFilter));
            if (priceMaxFilter != null && !priceMaxFilter.isEmpty())
                ps.setDouble(idx++, Double.parseDouble(priceMaxFilter));
            if (stockMinFilter != null && !stockMinFilter.isEmpty())
                ps.setInt(idx++, Integer.parseInt(stockMinFilter));
            if (stockMaxFilter != null && !stockMaxFilter.isEmpty())
                ps.setInt(idx++, Integer.parseInt(stockMaxFilter));

            ResultSet rs = ps.executeQuery();

            out.println("<h2>Products List</h2>");
            out.println("<table>");
            out.println(
                    "<thead><tr><th>ID</th><th>Name</th><th>Category</th><th>Price</th><th>Stock</th></tr></thead>");
            out.println("<tbody>");
            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getString("product_name") + "</td>");
                out.println("<td>" + rs.getString("category") + "</td>");
                out.println("<td>" + rs.getDouble("price") + "</td>");
                out.println("<td>" + rs.getInt("stock") + "</td>");
                out.println("</tr>");
            }
            if (!hasResults) {
                out.println("<tr><td colspan='5'>No products found.</td></tr>");
            }
            out.println("</tbody></table>");

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            out.println("<p style='color:red; text-align:center;'>Error: " + e.getMessage() + "</p>");
        }

        out.println("</div></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productName = request.getParameter("product_name");
        String category = request.getParameter("category");
        String priceStr = request.getParameter("price");
        String stockStr = request.getParameter("stock");

        String message = "";

        try {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            String sql = "INSERT INTO products (product_name, category, price, stock) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, productName);
            ps.setString(2, category);
            ps.setDouble(3, price);
            ps.setInt(4, stock);

            int inserted = ps.executeUpdate();
            if (inserted > 0) {
                message = "Product added successfully!";
            } else {
                message = "Failed to add product.";
            }

            ps.close();
            conn.close();

        } catch (Exception e) {
            message = "Error: " + e.getMessage();
        }

        // Redirect to GET with success/error message
        response.sendRedirect("products?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
    }
}