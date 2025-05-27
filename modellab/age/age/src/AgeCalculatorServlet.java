import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Period;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/age")
public class AgeCalculatorServlet extends HttpServlet {

    // Handle GET request: show a simple HTML form
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h2>Enter Your Date of Birth</h2>");
        out.println("<form method='post' action='age'>");
        out.println("Date of Birth: <input type='date' name='dob' required>");
        out.println("<br><br><input type='submit' value='Calculate Age'>");
        out.println("</form>");
        out.println("</body></html>");

        out.close();
    }

    // Handle POST request: process date and calculate age
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String dobString = request.getParameter("dob");

        try {
            LocalDate dob = LocalDate.parse(dobString);
            LocalDate today = LocalDate.now();
            Period age = Period.between(dob, today);

            out.println("<html><body>");
            out.println("<h2>Your Age:</h2>");
            out.println("<p>" + age.getYears() + " years, " 
                               + age.getMonths() + " months, "
                               + age.getDays() + " days</p>");
            out.println("</body></html>");

        } catch (Exception e) {
            out.println("<html><body>");
            out.println("<p>Error: Invalid date format.</p>");
            out.println("</body></html>");
        }

        out.close();
    }
}
