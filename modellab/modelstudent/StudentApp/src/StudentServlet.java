import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class StudentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String rollInput = request.getParameter("roll");
        ServletContext context = getServletContext();
        String path = context.getRealPath("/students.xml");

        String name = "", age = "", dept = "";
        boolean found = false;

        try {
            File xmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("student");

            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);

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

            request.setAttribute("roll", rollInput);
            request.setAttribute("name", name);
            request.setAttribute("age", age);
            request.setAttribute("dept", dept);
            request.setAttribute("found", found);

            RequestDispatcher dispatcher = request.getRequestDispatcher("display.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
