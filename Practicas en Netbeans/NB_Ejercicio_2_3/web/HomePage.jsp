<%@ page import="entrada.Entrada" %>
<%@ page import="entrada.EntradaManager" %>
<%@ page import="login.LoginManager" %>
<%@ page pageEncoding="UTF-8" %>
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
    <title>Blog</title></head>
<body>
<h1>Blog</h1>

<div align="right">
    <a href="LoginPage.jsp">Inicio</a>
    <a href="RegisterPage.jsp">Registro</a>
    <a href="VerCrearEntradaServlet">Crear entrada</a>
    <%=LoginManager.getLoginName(request)%>
</div>
<hr>

<%
    for (Entrada entrada : EntradaManager.leerEntradas(request)) {
        out.print("<h2> " + entrada.getTitulo() + "</h2>");
        out.print("<h3> " + entrada.getAutor() + " " + entrada.getFecha() + "</h3>");
        out.print("<p> " + entrada.getContenidoLimitado().replace("\n", "<br/>") + "</p>");
        out.print("<a href=VerComentariosServlet?noticia=" + entrada.getTitulo().hashCode() + " name=noticia >Leer Comentarios</a>");
    }

%>

</body>
</html>
