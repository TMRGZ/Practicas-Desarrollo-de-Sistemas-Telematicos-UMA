package login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "login.LoginServlet", urlPatterns = "/login.LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usuario = request.getParameter("usuario");
        String pass = request.getParameter("password");
        boolean userPassOk = LoginManager.userPassOK(request, usuario, pass);

        if (usuario == null || usuario.trim().length() == 0 || pass == null || pass.trim().length() == 0 || !userPassOk) {
            Map<String, String> errors = new HashMap<>();

            if (usuario == null || usuario.trim().length() == 0) {
                errors.put("Nombre de usuario", "Mandatory field");
            }

            if (pass == null || pass.trim().length() == 0) {
                errors.put("Contraseña", "Mandatory field");
            }

            if (!userPassOk) {
                errors.put("Inicio de sesion", "Usuario no existente o contraseña mala");
            }

            request.getSession().setAttribute("errorMessage", errors);
            response.sendRedirect("ErrorPage.jsp?red=" + "LoginPage.jsp");
        } else {
            LoginManager.login(request, usuario.trim(), pass.trim());
            response.sendRedirect("HomePage.jsp");
        }
    }
}
