<%--
  Created by IntelliJ IDEA.
  User: miguelangelruiz
  Date: 2018-12-28
  Time: 13:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title></head>
<body>
<%=session.getAttribute("errorMessage")%>

<a href=<%=request.getParameter("red")%>> Ok </a>
</body>
</html>
