package login;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public final class LoginManager extends HttpServlet {
    private final static String LOGIN_NAME_ATTRIBUTE = "loginName";
    private final static String PASSWORD_ATTRIBUTE = "password";

    private LoginManager() {
    }

    public static void login(HttpServletRequest request, String loginName, String password) {
        HttpSession session = request.getSession(true);
        session.setAttribute(LOGIN_NAME_ATTRIBUTE, loginName);
        session.setAttribute(PASSWORD_ATTRIBUTE, password);
    }

    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
    }

    public static String getLoginName(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return !isLogged(request) ? "anonimo" : (String) session.getAttribute(LOGIN_NAME_ATTRIBUTE);
    }

    public static boolean isLogged(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(LOGIN_NAME_ATTRIBUTE) != null;
    }

    public static boolean userPassOK(HttpServletRequest request, String user, String pass) {
        String path = request.getServletContext().getRealPath(request.getContextPath());
        String sFile = path + "\\" + "userpass";
        File file = new File(sFile);

        try (Scanner scLinea = new Scanner(new FileReader(file))) {
            while (scLinea.hasNextLine()) {
                String linea = scLinea.nextLine();

                try (Scanner sc = new Scanner(linea)) {
                    String usuario = sc.next();
                    String password = sc.next();

                    if (user.equalsIgnoreCase(usuario) && pass.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}