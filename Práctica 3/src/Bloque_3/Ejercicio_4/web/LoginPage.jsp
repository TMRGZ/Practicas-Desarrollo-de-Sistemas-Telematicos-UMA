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

    <script type="text/javascript">
        function validar() {
            var uname = document.formularioInicioSesion.usuario.value;
            var pass = document.formularioInicioSesion.password.value;
            var ok = true;
            var msg = "Campos vacíos: \n";

            if (uname === "") {
                msg += "Usuario vacio\n";
                ok = false;
            }
            if (pass === "") {
                msg += "Contraseña\n";
                ok = false;
            }

            if (ok === false) {
                alert(msg);
            }
            return ok;
        }
    </script>

</head>
<body>
<form name="formularioInicioSesion" action="login.LoginServlet" onsubmit="return validar()">
    Usuario <input type="text" name="usuario"/>
    <br> Contraseña <input type="password" name="password"/>
    <button type="submit" name="action" value="Iniciarsesion"> Iniciar sesión</button>
</form>

<a href="RegisterPage.jsp">Registrarse</a>
<a href="HomePage.jsp">Omitir inicio de sesion</a>
</body>
</html>
