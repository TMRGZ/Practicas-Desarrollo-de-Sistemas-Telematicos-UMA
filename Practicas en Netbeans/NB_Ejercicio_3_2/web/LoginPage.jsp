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
    <link rel="stylesheet" type="text/css" href="Login.css">
    <link rel="stylesheet" type="text/css" href="Autor.css">
</head>
<body>

<div class="loginbox">
    <img src="pepa.png" class="avatar" alt="Aquí había una Pepa">
    <h1>Inicio sesión</h1>
    <form action="login.LoginServlet">
        <p>Usuario</p>
        <input type="text" name="usuario" placeholder="Aquí el usuario"/>
        <p>Contraseña</p>
        <input type="password" name="password" placeholder="Aquí la contraseña"/>
        <input type="submit" value="Iniciar sesion">
        <a href="RegisterPage.jsp">Registrarse</a>
        <a href="HomePage.jsp">Omitir inicio de sesion</a>
    </form>
</div>


<div class="pie">
    <hr>
    <p> Miguel Ángel Ruiz Gómez (Universidad de Málaga)
        Copyright©2019 Todos los derechos reservados. </p>
</div>

</body>
</html>
