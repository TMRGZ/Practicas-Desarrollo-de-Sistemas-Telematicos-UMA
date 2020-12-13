package entrada;

import login.LoginManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "EntradaServlet", urlPatterns = "/EntradaServlet")
public class EntradaServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String titulo = request.getParameter("titulo");
        String contenido = request.getParameter("contenido");

        if (!LoginManager.isLogged(request) || titulo.trim().length() == 0 || contenido.trim().length() == 0) {
            Map<String, String> errors = new HashMap<>();

            if (!LoginManager.isLogged(request)) {
                errors.put("Usuario", "Tienes que estar logeado");
            }

            if (titulo.trim().length() == 0) {
                errors.put("Titulo", "Titulo vacio");
            }

            if (contenido.trim().length() == 0) {
                errors.put("Contenido", "No hay contenido");
            }

            request.getSession().setAttribute("errorMessage", errors);
            response.sendRedirect("ErrorPage.jsp?red=" + "NuevaEntrada.jsp");

        } else {
            EntradaManager.crearEntrada(request);
            response.sendRedirect("HomePage.jsp");
        }
    }
}
