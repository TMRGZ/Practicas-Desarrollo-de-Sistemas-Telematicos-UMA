package Bloque_1.Ejercicio_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class clienteTCP {
    private static final int PORT = 1234;

    public static void main(String[] args) {
        if ((args.length < 1) || args.length > 2)
            throw new IllegalArgumentException("Parametros incorrectos, servidor / puerto");
        int serverPort = args.length == 2 ? Integer.parseInt(args[1]) : PORT;

        try {
            InetAddress address = InetAddress.getByName(args[0]);
            Socket socket = new Socket(address, serverPort);
            // Opciones EJ3
            socket.setSendBufferSize(512);
            socket.setTcpNoDelay(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String message;

            System.out.println("Cliente preparado, puede comenzar a escribir:\n");
            while (true) {
                message = stdin.readLine();
                out.println(message);
                if (message.equals(".")) break;
                System.out.println("Mensaje enviado: " + message);

                message = in.readLine().trim();
                System.out.println("Respuesta recibida desde " + socket.getInetAddress().getCanonicalHostName() + ":" + socket.getPort() + ", mensaje: " + message);

            }

            System.out.println(in.readLine().trim());
            socket.close();
            in.close();
            out.close();
            stdin.close();
        } catch (IOException e) {
            System.out.println("Error de servidor");
        }
    }
}
