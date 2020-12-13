<%@ page import="entrada.Entrada" %>
<%@ page import="entrada.EntradaManager" %>
<%@ page import="login.LoginManager" %>
<%--
  Created by IntelliJ IDEA.
  User: miguelangelruiz
  Date: 2018-12-28
  Time: 13:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Blog</title>

</head>
    <link rel="stylesheet" type="text/css" href="Home.css">
<link rel="stylesheet" type="text/css" href="Autor.css">
<body>


<div class="opcionesBox">
    <h1>Blog</h1>

    <h2><%=LoginManager.getLoginName(request)%></h2>

    <form action="LoginPage.jsp">
        <input type="submit" value="Inicio">
    </form>

    <form action="RegisterPage.jsp">
        <input type="submit" value="Registrarse">
    </form>

    <form action="VerCrearEntradaServlet">
        <input type="submit" value="Crear Entrada">
    </form>
    <%--
    <a href="LoginPage.jsp">Inicio</a>
    <a href="RegisterPage.jsp">Registro</a>
    <a href="VerCrearEntradaServlet">Crear entrada</a>
    <%=LoginManager.getLoginName(request)%>
    --%>
</div>


    <%
        for (Entrada entrada : EntradaManager.leerEntradas(request)) {
            out.print("<div class=\"noticiasBox\">");

            out.print("<h2> " + entrada.getTitulo() + "</h2>");
            out.print("<h3> " + entrada.getAutor() + " " + entrada.getFecha() + "</h3>");
            out.print("<p> " + entrada.getContenidoLimitado().replace("\n", "<br/>") + "</p>");
            out.print("<a href=VerComentariosServlet?noticia=" + entrada.getTitulo().hashCode() + " name=noticia >Leer Comentarios</a>");

            out.print("</div>");
        }
    %>


<div class="pie">
    <hr>
    <p> Miguel Ángel Ruiz Gómez (Universidad de Málaga)
        Copyright©2019 Todos los derechos reservados. </p>
</div>


</body>
</html>
