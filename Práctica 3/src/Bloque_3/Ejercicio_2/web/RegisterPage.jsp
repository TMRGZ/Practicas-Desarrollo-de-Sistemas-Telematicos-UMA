<%--
  Created by IntelliJ IDEA.
  User: miguelangelruiz
  Date: 2018-12-28
  Time: 13:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Registro</title>
    <link rel="stylesheet" type="text/css" href="Register.css">
    <link rel="stylesheet" type="text/css" href="Autor.css">
</head>
<body>

<div class="registerBox">
<img src="pepa.png" class="avatar" alt="Aquí había una Pepa">
    <h1>Registro</h1>
    <form action="RegisterPage.jsp">
        <p>Nombre de usuario</p>
        <input type="text" name="user" placeholder="Aquí tu nuevo usuario">
        <p>Contraseña</p>
        <input type="password" name="password" placeholder="Pon tu contraseña">
        <p>Repite la contraseña</p>
        <input type="password" name="npassword" placeholder="Repitela">
        <input type="submit" value="Registrarse">
    </form>


</div>

<div class="pie">
    <hr>
    <p> Miguel Ángel Ruiz Gómez (Universidad de Málaga)
        Copyright©2019 Todos los derechos reservados. </p>
</div>

<%--
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
--%>
</body>
</html>
