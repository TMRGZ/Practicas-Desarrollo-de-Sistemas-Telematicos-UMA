package comentario;

import login.LoginManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "VerComentariosServlet", urlPatterns = "/VerComentariosServlet")
public class VerComentariosServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!LoginManager.isLogged(request)) {
            Map<String, String> errors = new HashMap<>();
            errors.put("Usuario", "Tienes que estar logeado");
            request.getSession().setAttribute("errorMessage", errors);
            response.sendRedirect("ErrorPage.jsp?red=" + "HomePage.jsp");
        } else {
            request.getRequestDispatcher("Comentario.jsp").forward(request, response);
        }
    }
}
