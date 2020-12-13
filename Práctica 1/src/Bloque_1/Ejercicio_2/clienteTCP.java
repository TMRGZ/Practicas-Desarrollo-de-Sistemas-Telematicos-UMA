// Miguel Angel Ruiz

package Bloque_1.Ejercicio_2;

import java.io.*;
import java.net.*;

public class clienteTCP {
    private static final int PORT = 1234;

    public static void main(String[] args) {
        if ((args.length < 1) || args.length > 2)
            throw new IllegalArgumentException("Parametros incorrectos, servidor / puerto");
        int serverPort = args.length == 2 ? Integer.parseInt(args[1]) : PORT;

        try {
            InetAddress address = InetAddress.getByName(args[0]);
            Socket socket = new Socket(address, serverPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String message;
            new Recibir(in, socket).start();

            System.out.println("Cliente preparado, puede comenzar a escribir:\n");
            while (true) {
                if (socket.isClosed()) break;
                message = stdin.readLine();
                out.println(message);
                if (message.equals(".")) break;
                System.out.println("Mensaje enviado: " + message);
            }

            socket.close();
            out.close();
            stdin.close();
        } catch (IOException e) {
            System.out.println("Error de servidor");
        }
    }

    static class Recibir extends Thread {
        BufferedReader in;
        Socket socket;

        public Recibir(BufferedReader in, Socket socket) {
            this.in = in;
            this.socket = socket;
        }

        @Override
        public void run() {

            while (true) {
                try {
                    String message = in.readLine().trim();
                    System.out.println("Mensaje recibido de " + socket.getInetAddress().getCanonicalHostName() + ":" + socket.getLocalPort() + ", mensaje: " + message);
                    if (message.equals(".")) break;
                } catch (IOException e) {
                    System.out.println("Servidor desconectado");
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    return;
                }
            }
            try {
                System.out.println(in.readLine().trim());
                in.close();
                socket.close();
            } catch (IOException ignored) { }
        }
    }
}
