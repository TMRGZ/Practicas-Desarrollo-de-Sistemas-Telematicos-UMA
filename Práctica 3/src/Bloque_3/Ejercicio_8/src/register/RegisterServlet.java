package register;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "register.RegisterServlet", urlPatterns = "/register.RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usuario = request.getParameter("user");
        String pass = request.getParameter("password");
        String npass = request.getParameter("npassword");

        boolean existe = RegisterManager.existeUsuario(request, usuario);

        if (existe || !pass.equals(npass) || usuario.indexOf(' ') != -1 || pass.indexOf(' ') != -1) {
            Map<String, String> errors = new HashMap<>();

            if (existe) {
                errors.put("Nombre de usuario", "Ya existe");
            }

            if (!pass.equals(npass)) {
                errors.put("Contraseña", "No coinciden");
            }

            if (usuario.indexOf(' ') != -1 || pass.indexOf(' ') != -1) {
                errors.put("Formato de nombre o contraseña", "Espacios no admitidos");
            }

            request.getSession().setAttribute("errorMessage", errors);
            response.sendRedirect("ErrorPage.jsp?red=" + "RegisterPage.jsp");
        } else {
            RegisterManager.registarUsuario(request, usuario.trim(), pass.trim());
            response.sendRedirect("LoginPage.jsp");
        }
    }
}
