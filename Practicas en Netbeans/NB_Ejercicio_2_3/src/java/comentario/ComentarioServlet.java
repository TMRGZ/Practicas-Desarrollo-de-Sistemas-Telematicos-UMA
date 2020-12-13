package comentario;

import entrada.Entrada;
import login.LoginManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ComentarioServlet", urlPatterns = "/ComentarioServlet")
public class ComentarioServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Entrada entrada = (Entrada) request.getSession(true).getAttribute("entrada");
        String comentario = request.getParameter("comentario");


        if (!LoginManager.isLogged(request) || comentario.trim().length() == 0) {
            Map<String, String> errors = new HashMap<>();

            if (!LoginManager.isLogged(request)) {
                errors.put("Usuario", "Tienes que estar logeado");
            }

            if (comentario.trim().length() == 0) {
                errors.put("Comenario", "Comentario vacio");
            }

            request.setAttribute("errors", errors);
            response.sendRedirect("Comentario.jsp?noticia=" + entrada.getTitulo());

        } else {
            ComentarioManager.crearComentario(request, entrada);
            response.sendRedirect("Comentario.jsp?noticia=" + entrada.getTitulo().hashCode());
        }
    }
}
