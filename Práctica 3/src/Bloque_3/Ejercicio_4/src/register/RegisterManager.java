package register;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Scanner;

public final class RegisterManager {
    private RegisterManager() {
    }

    public static boolean existeUsuario(HttpServletRequest request, String usuario) {
        // Para Idea
        String path = request.getServletContext().getRealPath(File.separator);
        String sFile = path + "userpass";
        // Para Netbeans
        //String path = request.getServletContext().getRealPath(request.getContextPath());
        //String sFile = path + "\\" + "userpass";

        File f = new File(sFile);

        if (!f.exists()) {
            try {
                f.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        try (Scanner scLinea = new Scanner(new FileReader(f))) {
            while (scLinea.hasNextLine()) {
                String linea = scLinea.nextLine();

                try (Scanner sc = new Scanner(linea)) {
                    String user = sc.next();
                    String pass = sc.next();

                    if (user.equalsIgnoreCase(usuario)) {
                        scLinea.close();
                        sc.close();
                        return true;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void registarUsuario(HttpServletRequest request, String usuario, String password) {
        String lineaEscribir = usuario + " " + password;

        if (!existeUsuario(request, usuario)) {
            // Para Idea
            String path = request.getServletContext().getRealPath(File.separator);
            String sFile = path + "userpass";
            // Para Netbeans
            //String path = request.getServletContext().getRealPath(request.getContextPath());
            //String sFile = path + "\\" + "userpass";

            File file = new File(sFile);

            try (PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
                printWriter.println(lineaEscribir);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
