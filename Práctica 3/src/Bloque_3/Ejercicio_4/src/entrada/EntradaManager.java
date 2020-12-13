package entrada;

import login.LoginManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EntradaManager {
    public static final String EOP = "&";
    public static final String INICIO_COMENT_FILE = "FicheroComentarios_";
    public static final String ARCHIVO_POSTS = "posts";

    private EntradaManager() {

    }

    public static void crearEntrada(HttpServletRequest request) {
        String titulo = request.getParameter("titulo");
        String autor = LoginManager.getLoginName(request);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String fecha = dtf.format(now);
        String contenido = request.getParameter("contenido");
        String comentFile = INICIO_COMENT_FILE + titulo.hashCode() + ".txt";

        // Para Idea
        String path = request.getServletContext().getRealPath(File.separator);
        String sFile = path + ARCHIVO_POSTS;
        //Para Netbeans
        //String path = request.getServletContext().getRealPath(request.getContextPath());
        //String sFile = path + "\\" + ARCHIVO_POSTS;


        File file = new File(sFile);

        try (PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
            printWriter.println(titulo);
            printWriter.println(autor);
            printWriter.println(fecha);
            printWriter.println(contenido);
            printWriter.println("$");
            printWriter.println(comentFile);
            printWriter.println(EOP);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Entrada> leerEntradas(HttpServletRequest request) {
        LinkedList<Entrada> list = new LinkedList<>();

        // Para Idea
        String path = request.getServletContext().getRealPath(File.separator);
        String sFile = path + ARCHIVO_POSTS;
        // Para Netbeans
        //String path = request.getServletContext().getRealPath(request.getContextPath());
        //String sFile = path + "\\" + ARCHIVO_POSTS;


        File file = new File(sFile);

        try (Scanner sc = new Scanner(new FileReader(file))) {
            while (sc.hasNextLine()) {
                String titulo = sc.nextLine();
                String autor = sc.nextLine();
                String fecha = sc.nextLine();
                StringBuilder cuerpo = new StringBuilder(sc.nextLine());
                cuerpo.append("\n");

                String contenido = sc.nextLine();
                while (!contenido.equals("$")) {
                    cuerpo.append(contenido).append("\n");
                    contenido = sc.nextLine();
                }

                String comentarios = sc.nextLine();

                Entrada entrada = new Entrada(titulo.trim(), autor.trim(), fecha.trim(), cuerpo.toString().trim(), comentarios.trim());

                if (!list.contains(entrada)) list.addFirst(entrada);

                sc.nextLine();
            }
            list.sort(new EntradaComparator());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}
