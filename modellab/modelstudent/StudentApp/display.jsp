<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
  <head>
    <title>Student Details</title>
  </head>
  <body>
    <% boolean found = (Boolean) request.getAttribute("found"); %> <% if (found)
    { %>
    <h2>Student Details</h2>
    <table border="1">
      <tr>
        <th>Roll Number</th>
        <td><%= request.getAttribute("roll") %></td>
      </tr>
      <tr>
        <th>Name</th>
        <td><%= request.getAttribute("name") %></td>
      </tr>
      <tr>
        <th>Age</th>
        <td><%= request.getAttribute("age") %></td>
      </tr>
      <tr>
        <th>Department</th>
        <td><%= request.getAttribute("dept") %></td>
      </tr>
    </table>
    <% } else { %>
    <h3>No student found with that roll number.</h3>
    <% } %>

    <br /><a href="index.html">Search Again</a>
  </body>
</html>
