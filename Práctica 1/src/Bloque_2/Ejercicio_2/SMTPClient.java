package Bloque_2.Ejercicio_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class SMTPClient {
    private static final int port = 25;
    private static BufferedReader stdin;
    private static BufferedReader in;
    private static Socket socket;
    private static PrintWriter out;
    private static String finEnvio = "\r\n";
    private static String finCuerpo = finEnvio + ".";

    // Constantes
    private static final String DATA = "DATA";
    private static final String mailFrom = "MAIL FROM:<";
    private static final String rcpTo = "RCPT TO:<";
    private static final String subject = "Subject: ";
    private static final String from = "From: ";
    private static final String to = "To: ";
    private static final String ID = "HELO practica";
    private static final String QUIT = "QUIT";
    private static final String IP = "127.0.0.1";

    public static void configuracionInicial() throws IOException {
        stdin = new BufferedReader(new InputStreamReader(System.in));
        InetAddress address = InetAddress.getByName(IP);
        socket = new Socket(address, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        stdin = new BufferedReader(new InputStreamReader(System.in));
        receive();
        send(ID);
        receive();
    }

    public static void receive() throws IOException {
        String rec = in.readLine().trim();

        System.out.println(rec);
        String[] msg = rec.split(" ");

        if (!msg[0].equals("220") && !msg[0].equals("250") && !msg[0].equals("354") && !msg[0].equals("221")) {
            throw new RuntimeException("Error recibido del servidor: " + msg[0]);
        }
    }

    public static void send(String msg) {
        out.print(msg + finEnvio);
        out.flush();
    }

    private static void leerCuerpo() throws IOException {
        while (true) {
            String linea = stdin.readLine();

            if (linea.equalsIgnoreCase(".")) break;

            send(linea);
        }
        send(finCuerpo);
    }

    public static void main(String[] args) throws IOException {
        configuracionInicial();
        // Dar origen
        System.out.print("Inserte su email: ");
        String correoOrigen = stdin.readLine();
        send(mailFrom + correoOrigen + ">");
        receive();
        // Dar destinatario
        System.out.print("Inserte destinatario: ");
        String correoEnvio = stdin.readLine();
        send(rcpTo + correoEnvio + ">");
        receive();
        // Comienzo de datos
        send(DATA);
        receive();
        // Capturar asunto
        System.out.println("Inserte asunto");
        String asunto = stdin.readLine();
        // Formato
        send(subject + asunto);
        send(from + correoOrigen);
        send(to + correoEnvio);
        // Cuerpo del mensaje
        System.out.println("Cuerpo del mensaje, termine insertando un .");
        leerCuerpo();
        receive();
        // Fin del proceso
        send(QUIT);
        receive();
        socket.close();
    }


}
