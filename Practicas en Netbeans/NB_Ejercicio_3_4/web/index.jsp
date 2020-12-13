<%--
  Created by IntelliJ IDEA.
  User: miguelangelruiz
  Date: 2018-12-29
  Time: 16:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Enlace</title>
    <script>
        function cambia(opcion) {
            if (opcion === 1) {
                img.src = "nook.png";
            } else if (opcion === 2) {
                img.src = "ok.png";
            }
        }
    </script>
</head>
<body>
<h1>Entrada al blog</h1>
<a href="HomePageServlet">Entrar</a>

<div align="center">
    <img src="nook.png" id="img" width="250px" height="200px">
    <br>
    <button type="submit" onclick="cambia(2)">Mirar</button>
    <button type="submit" onclick="cambia(1)">No mirar</button>
</div>
</body>
</html>
