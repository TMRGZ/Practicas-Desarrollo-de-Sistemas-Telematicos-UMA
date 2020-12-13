<%--
  Created by IntelliJ IDEA.
  User: miguelangelruiz
  Date: 2018-12-14
  Time: 23:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Inicio</title>
    <script type="text/javascript" src="checkLoginAJAX.js"></script>
</head>
<body>
<form action="RegisterPage.jsp" name="login">
    Usuario <input id="usu" type="text" name="usuario" onchange='xmlhttpPost("")'/>
    <br>
    Contraseña <input id="pass" type="password" name="password" onchange='xmlhttpPost()'/>
    <input type="submit" value="Iniciar sesion" >
</form>
<div id="result"></div>

<a href="RegisterPage.jsp">Registrarse</a>
<a href="HomePage.jsp">Omitir inicio de sesion</a>
</body>
</html>
