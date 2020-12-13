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
    <link rel="stylesheet" type="text/css" href="ErrorPage.css">
<body>

<div class="errorBox">
    <h1>Error</h1>
    <h3><%=session.getAttribute("errorMessage")%></h3>
    <a href=<%=request.getParameter("red")%>> Ok </a>

</div>

</body>
</html>
