<%@ page import="entrada.Entrada" %>
<%@ page import="entrada.EntradaManager" %>
<%@ page import="comentario.ComentarioManager" %>
<%@ page import="comentario.Comentario" %><%--
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
<body>

<%
    int noticia = Integer.parseInt(request.getParameter("noticia"));
    Entrada noti = null;

    for (Entrada entrada : EntradaManager.leerEntradas(request)) {
        if (entrada.getTitulo().hashCode() == noticia) {
            noti = entrada;
            break;
        }
    }

    out.print("<h2> " + noti.getTitulo() + "</h2>");
    out.print("<h3> " + noti.getAutor() + " " + noti.getFecha() + "</h3>");
    out.print("<p> " + noti.getContenido().replace("\n", "<br/>") + "</p>");
    out.print("<hr>");

    for (Comentario comentario : ComentarioManager.leerComentarios(request, noti.getComentarios())) {
        out.print("<h4> " + comentario.getAutor() + " " + comentario.getFecha() + "</h4>");
        out.print("<p> " + comentario.getContenido().replace("\n", "<br/>") + "</p>");
    }
    session.setAttribute("entrada", noti);

%>


<h2>
    Escribe un comentario
</h2>

<form action="ComentarioServlet">
    <p>
        <textarea name="comentario" rows=3 cols=20 placeholder="Comentario..." style="width:400px; height:200px;"
                  required></textarea>
    </p>
    <p>
        <input type="submit" value="Enviar">
    </p>
</form>

</body>
</html>
