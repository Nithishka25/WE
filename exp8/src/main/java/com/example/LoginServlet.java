package com.example;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;

public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<style>");
        out.println("  body {");
        out.println("    font-family: Arial, sans-serif;");
        out.println("    display: flex;");
        out.println("    justify-content: center;");
        out.println("    align-items: center;");
        out.println("    height: 100vh;");
        out.println("    background-color: #f0f0f0;");
        out.println("    margin: 0;");
        out.println("  }");
        out.println("  .message-box {");
        out.println("    padding: 30px 50px;");
        out.println("    border-radius: 10px;");
        out.println("    box-shadow: 0 4px 8px rgba(0,0,0,0.1);");
        out.println("    text-align: center;");
        out.println("  }");
        out.println("  .success {");
        out.println("    background-color: #d4edda;");
        out.println("    color: #155724;");
        out.println("    border: 1px solid #c3e6cb;");
        out.println("  }");
        out.println("  .error {");
        out.println("    background-color: #f8d7da;");
        out.println("    color: #721c24;");
        out.println("    border: 1px solid #f5c6cb;");
        out.println("  }");
        out.println("  .btn {");
        out.println("    margin-top: 20px;");
        out.println("    padding: 10px 20px;");
        out.println("    font-size: 16px;");
        out.println("    border: none;");
        out.println("    border-radius: 5px;");
        out.println("    background-color: #007bff;");
        out.println("    color: white;");
        out.println("    cursor: pointer;");
        out.println("    transition: background-color 0.3s ease;");
        out.println("  }");
        out.println("  .btn:hover {");
        out.println("    background-color: #0056b3;");
        out.println("  }");
        // New styles for success message text
        out.println("  .success-title {");
        out.println("    font-size: 36px;");
        out.println("    font-weight: bold;");
        out.println("    margin-bottom: 10px;");
        out.println("  }");
        out.println("  .success-subtitle {");
        out.println("    font-size: 18px;");
        out.println("    font-weight: normal;");
        out.println("    color:rgb(32, 33, 31);");
        out.println("  }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        if ("Nithishka".equals(username) && "user1@gmail.com".equals(email) && "pass123".equals(password)) {
            out.println("<div class='message-box success'>");
            out.println("<div class='success-title'>Congratulations " + username + "!</div>");
            out.println("<div class='success-subtitle'>You have successfully logged in.</div>");
        } else {
            out.println("<div class='message-box error'>");
            out.println("<h2>Invalid credentials.</h2>");
        }

        out.println("<button class='btn' onclick=\"window.location.href='login.html'\">Go to Login Page</button>");
        out.println("</div>");

        out.println("</body>");
        out.println("</html>");

        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendRedirect("login.html");
    }
}
