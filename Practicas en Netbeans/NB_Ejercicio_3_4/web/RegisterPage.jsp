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
    <script type="text/javascript">
        function validar() {
            var nombre = document.formularioRegistro.nombre.value;
            var apellido = document.formularioRegistro.apellido.value;
            var correo = document.formularioRegistro.correo.value;


            var user = document.formularioRegistro.user.value;
            var pass = document.formularioRegistro.password.value;
            var npass = document.formularioRegistro.npassword.value;


            var msg = "";
            var ok = true;

            if (user === "" || pass === "" || npass === "" || nombre === "" || apellido === "" || correo === "") {
                msg += "Rellene todos los campos";
                ok = false;
            } else if (pass !== npass) {
                msg += "Las contraseñas no coinciden";
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
<form name="formularioRegistro" action="register.RegisterServlet" onsubmit="return validar()">
    <fieldset>
        <legend>Nuevo usuario</legend>
        <br> Nombre:<label>
        <input type="text" name="nombre">
    </label>
        <br> Apellido:<label>
        <input type="text" name="apellido">
    </label>
        <br> Correo:<label>
        <input type="text" name="correo">
    </label>
        <br> Usuario:<label>
        <input type="text" name="user">
    </label>
        <br> Contraseña:<label>
        <input type="password" name="password">
    </label>
        <br> Repite Contraseña:<label>
        <input type="password" name="npassword">
    </label>


        <button type="submit" name="action" value="Registrarse">Registrarse</button>
    </fieldset>
</form>
</body>
</html>
