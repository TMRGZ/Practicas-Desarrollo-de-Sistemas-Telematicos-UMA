<%--
  Created by IntelliJ IDEA.
  User: miguelangelruiz
  Date: 2018-12-31
  Time: 15:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Nueva Entrada</title>

    <script type="text/javascript">
        function validar() {
            var entrada = document.entrada.contenido.value;
            var titulo = document.entrada.titulo.value;
            var ok = true;
            var msg = "";


            if (titulo === "" || entrada === "") {
                msg += "Los dos campos son obligatorios";
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
<h1 align="center">Nueva Entrada</h1>
<hr>
<div align="center">
    <form name="entrada" action="" onsubmit="return validar()">
        <p>
            Titulo <input type="text" name="titulo"/>
        </p>
        <p>
            <textarea name="contenido" rows=3 cols=20 placeholder="Contenido..." style="width:400px; height:200px;"
                      ></textarea>
        </p>
        <p>
            <button type="submit" value="Enviar">Enviar</button>
        </p>
    </form>
</div>

</body>
</html>
