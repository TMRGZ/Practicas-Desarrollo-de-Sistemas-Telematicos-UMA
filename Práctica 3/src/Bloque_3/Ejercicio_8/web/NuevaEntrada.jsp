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
</head>
<body>
<h1 align="center">Nueva Entrada</h1>
<hr>
<div align="center">
    <form action="EntradaServlet">
        <p>
            Titulo <input type="text" name="titulo" required/>
        </p>
        <p>
            <textarea name="contenido" rows=3 cols=20 placeholder="Contenido..." style="width:400px; height:200px;" required></textarea>
        </p>
        <p>
            <input type="submit" value="Enviar">
        </p>
    </form>
</div>

</body>
</html>
