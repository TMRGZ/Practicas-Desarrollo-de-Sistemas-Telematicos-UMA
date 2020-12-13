<%--
  Created by IntelliJ IDEA.
  User: miguelangelruiz
  Date: 2018-12-28
  Time: 13:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<html>
<head>
    <title>Registro</title>
</head>
<body>
<form action="register.RegisterServlet">
    <fieldset>
        <legend>Nuevo usuario</legend>
        <br> Usuario:<label>
        <input type="text" name="user">
    </label>
        <br> Contraseña:<label>
        <input type="password" name="password">
    </label>
        <br> Repite Contraseña:<label>
        <input type="password" name="npassword">
    </label>
        <input type="submit" value="Registrarse">
    </fieldset>
</form>
</body>
</html>
