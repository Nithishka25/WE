package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class XMLServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            InputStream xmlInput = getServletContext().getResourceAsStream("/data.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlInput);

            NodeList products = doc.getElementsByTagName("product");

            out.println("<html><head><title>XML Product List</title>");
            out.println("<style>");
            out.println("body { font-family: Arial; background: #f4f4f4; padding: 20px; }");
            out.println("table { border-collapse: collapse; width: 60%; margin: auto; background: #fff; }");
            out.println("th, td { padding: 10px; border: 1px solid #ccc; text-align: center; }");
            out.println("th { background: #0077cc; color: white; }");
            out.println("</style></head><body>");
            out.println("<h2 style='text-align:center;'>Products from XML</h2>");
            out.println("<table><tr><th>ID</th><th>Name</th><th>Price (₹)</th></tr>");

            for (int i = 0; i < products.getLength(); i++) {
                Element product = (Element) products.item(i);
                String id = product.getElementsByTagName("id").item(0).getTextContent();
                String name = product.getElementsByTagName("name").item(0).getTextContent();
                String price = product.getElementsByTagName("price").item(0).getTextContent();

                out.println("<tr><td>" + id + "</td><td>" + name + "</td><td>" + price + "</td></tr>");
            }

            out.println("</table></body></html>");

        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}
