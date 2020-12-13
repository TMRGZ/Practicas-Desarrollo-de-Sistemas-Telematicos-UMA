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
    <title>Blog</title></head>
<script type="text/javascript">
    function redirect(url) {
        window.location.href = url;
    }


</script>
<body>
<h1>Blog</h1>

<div align="right">
    <button type="submit" onclick="redirect('LoginPage.jsp')">Inicio</button>
    <button type="submit" onclick="redirect('RegisterPage.jsp')">Registro</button>
    <button type="submit" onclick="redirect('VerCrearEntradaServlet')">Crear entrada</button>

    <%=LoginManager.getLoginName(request)%>
</div>

<div align="left">
    <button type="submit" onclick="window.print()">Imprimir Página</button>
    <br>
    <button type="submit" onclick="alert(document.lastModified)">Última modificación</button>
</div>

<hr>

<%
    for (Entrada entrada : EntradaManager.leerEntradas(request)) {
        out.print("<h2> " + entrada.getTitulo() + "</h2>");
        out.print("<h3> " + entrada.getAutor() + " " + entrada.getFecha() + "</h3>");
        out.print("<p> " + entrada.getContenidoLimitado().replace("\n", "<br/>") + "</p>");
        out.print("<button type=\"submit\" onclick=\"redirect('VerComentariosServlet?noticia=" + entrada.getTitulo().hashCode() + "')\" > Leer Comentarios </button>");
    }

%>

</body>
</html>
