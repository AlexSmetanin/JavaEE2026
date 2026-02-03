<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
    <h1><%= "Hello World!" %></h1>
    <br/>
    <%@ page import="java.util.Date,java.text.*"%>
    <%
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss SSS");
    %>
    <h2>Current Time:</h2>
    <%=df.format(now)%>
    <h3><%="Виконав студент Сметанін О.Г."%></h3>

    <a href="hello-servlet">Hello Servlet</a>
</body>
</html>