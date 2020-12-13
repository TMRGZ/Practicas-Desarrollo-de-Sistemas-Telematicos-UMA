package comentario;

import entrada.Entrada;
import login.LoginManager;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ComentarioManager {
    public static final String EOP = "&";

    private ComentarioManager() {
    }

    public static void crearComentario(HttpServletRequest request, Entrada entrada) {
        String comentario = request.getParameter("comentario");
        String autor = LoginManager.getLoginName(request);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String fecha = dtf.format(now);

        // Para Idea
        //String path = request.getServletContext().getRealPath(File.separator);
        //String sFile = path + entrada.getComentarios();

        // Para Netbeans
        String path = request.getServletContext().getRealPath(request.getContextPath());
        String sFile = path + "\\" + entrada.getComentarios();


        File file = new File(sFile);

        try (PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
            printWriter.println(autor);
            printWriter.println(fecha);
            printWriter.println(comentario);
            printWriter.println("$");
            printWriter.println(EOP);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Comentario> leerComentarios(HttpServletRequest request, String archivo) {
        LinkedList<Comentario> list = new LinkedList<>();

        // Para Idea
        //String path = request.getServletContext().getRealPath(File.separator);
        //String sFile = path + archivo;
        // Para Netbeans
        String path = request.getServletContext().getRealPath(request.getContextPath());
        String sFile = path + "\\" + archivo;
        File file = new File(sFile);

        try (Scanner sc = new Scanner(new FileReader(file))) {
            while (sc.hasNextLine()) {
                String autor = sc.nextLine();
                String fecha = sc.nextLine();
                StringBuilder cuerpo = new StringBuilder(sc.nextLine());
                cuerpo.append("\n");

                String linea = sc.nextLine();
                while (!linea.equals("$")) {
                    cuerpo.append(linea).append("\n");
                    linea = sc.nextLine();
                }

                Comentario comentario = new Comentario(autor.trim(), fecha.trim(), cuerpo.toString().trim());

                if (!list.contains(comentario)) list.addLast(comentario);

                sc.nextLine();
            }
            list.sort(new ComentarioComparator());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}
