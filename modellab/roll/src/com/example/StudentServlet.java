package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import jakarta.servlet.ServletContext;

import org.w3c.dom.*;
import javax.xml.parsers.*;

import java.io.*;

@WebServlet("/student")
public class StudentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String rollInput = request.getParameter("roll");
        ServletContext context = getServletContext();
        String xmlPath = context.getRealPath("/students.xml");

        String name = "", age = "", dept = "";
        boolean found = false;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            File xmlFile = new File(xmlPath);
            if (!xmlFile.exists()) {
                out.println("<html><body><h3>students.xml not found!</h3></body></html>");
                return;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList studentList = doc.getElementsByTagName("student");

            for (int i = 0; i < studentList.getLength(); i++) {
                Node node = studentList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String roll = element.getElementsByTagName("roll").item(0).getTextContent();

                    if (roll.equals(rollInput)) {
                        name = element.getElementsByTagName("name").item(0).getTextContent();
                        age = element.getElementsByTagName("age").item(0).getTextContent();
                        dept = element.getElementsByTagName("department").item(0).getTextContent();
                        found = true;
                        break;
                    }
                }
            }

            out.println("<html><body>");
            if (found) {
                out.println("<h2>Student Details</h2>");
                out.println("<p>Roll No: " + rollInput + "</p>");
                out.println("<p>Name: " + name + "</p>");
                out.println("<p>Age: " + age + "</p>");
                out.println("<p>Department: " + dept + "</p>");
            } else {
                out.println("<h3 style='color:red;'>No student found with Roll No: " + rollInput + "</h3>");
            }
            out.println("<br><a href='index.html'>Search Again</a>");
            out.println("</body></html>");

        } catch (Exception e) {
            e.printStackTrace(out);
        } finally {
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response); // handle GET the same as POST
    }
}
