package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/student")
public class StudentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Inform user to use form submission (POST)
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h3>Please use the form to submit your Roll Number.</h3>");
        out.println("<a href=\"index.html\">Go to Form</a>");
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String rollno = request.getParameter("rollno");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String jdbcUrl = "jdbc:mysql://localhost:3306/student_db";
        String dbUser = "root";       // your DB username
        String dbPass = "";           // your DB password

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM students WHERE rollno = ?");
            stmt.setString(1, rollno);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                out.println("<h3>Student Information</h3>");
                out.println("Roll No: " + rs.getString("rollno") + "<br>");
                out.println("Name: " + rs.getString("name") + "<br>");
                out.println("Class: " + rs.getString("class") + "<br>");
            } else {
                out.println("<p>No record found for Roll Number: " + rollno + "</p>");
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }

        out.close();
    }
}
