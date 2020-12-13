<%--
  Created by IntelliJ IDEA.
  User: miguelangelruiz
  Date: 2018-12-14
  Time: 23:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Inicio</title>
</head>
<body>
<form action="login.LoginServlet">
    Usuario <input type="text" name="usuario"/>
    <br> Contraseña <input type="password" name="password"/>
    <input type="submit" value="Iniciar sesion">
</form>

<a href="RegisterPage.jsp">Registrarse</a>
<a href="HomePage.jsp">Omitir inicio de sesion</a>
</body>
</html>
