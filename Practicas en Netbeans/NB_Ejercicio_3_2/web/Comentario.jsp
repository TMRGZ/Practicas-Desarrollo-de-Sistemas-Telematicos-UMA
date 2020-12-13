<%@ page import="entrada.Entrada" %>
<%@ page import="entrada.EntradaManager" %>
<%@ page import="comentario.ComentarioManager" %>
<%@ page import="comentario.Comentario" %>
<%@ page import="login.LoginManager" %><%--
  Created by IntelliJ IDEA.
  User: miguelangelruiz
  Date: 2018-12-31
  Time: 17:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Comentarios</title>
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
    int noticia = Integer.parseInt(request.getParameter("noticia"));
    Entrada noti = null;

    for (Entrada entrada : EntradaManager.leerEntradas(request)) {
        if (entrada.getTitulo().hashCode() == noticia) {
            noti = entrada;
            break;
        }
    }

    out.print("<div class=\"noticiasBox\">");
    out.print("<h2> " + noti.getTitulo() + "</h2>");
    out.print("<h3> " + noti.getAutor() + " " + noti.getFecha() + "</h3>");
    out.print("<p> " + noti.getContenido().replace("\n", "<br/>") + "</p>");
    out.print("<hr>");
    out.print("</div>");

    for (Comentario comentario : ComentarioManager.leerComentarios(request, noti.getComentarios())) {
        out.print("<div class=\"comentariosBox\">");
        out.print("<h4> " + comentario.getAutor() + " " + comentario.getFecha() + "</h4>");
        out.print("<p> " + comentario.getContenido().replace("\n", "<br/>") + "</p>");
        out.print("</div>");
    }
    session.setAttribute("entrada", noti);

%>


<div class="areaBox">
    <h2>
        Escribe un comentario
    </h2>

    <form action="ComentarioServlet">
        <p>
        <textarea name="comentario" rows=3 cols=20 placeholder="Comentario..."
                  required></textarea>
        </p>
        <p>
            <input type="submit" value="Enviar">
        </p>
    </form>

</div>

<div class="pie">
    <hr>
    <p> Miguel Ángel Ruiz Gómez (Universidad de Málaga)
        Copyright©2019 Todos los derechos reservados. </p>
</div>



</body>
</html>
