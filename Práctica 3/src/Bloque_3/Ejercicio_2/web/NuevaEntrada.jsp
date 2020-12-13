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
    <link rel="stylesheet" type="text/css" href="NuevaEntrada.css">
    <link rel="stylesheet" type="text/css" href="Autor.css">
</head>
<body>




<div class="entradaBox">
    <h1>Nueva Entrada</h1>
    <form action="EntradaServlet">
        <p>Título</p>
        <input type="text" name="titulo" required/>
        <p>Cuerpo de la Noticia</p>
        <textarea name="contenido" placeholder="Contenido..." required></textarea>
        <input type="submit" value="Enviar">
    </form>

</div>

<div class="pie">
    <hr>
    <p> Miguel Ángel Ruiz Gómez (Universidad de Málaga)
        Copyright©2019 Todos los derechos reservados. </p>
</div>

</body>
</html>
