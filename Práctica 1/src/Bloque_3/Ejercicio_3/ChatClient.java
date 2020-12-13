// Miguel Angel Ruiz

package Bloque_3.Ejercicio_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ChatClient {
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
                    System.out.println(message);
                    if (message.toCharArray()[message.length() - 1] == '.') break;
                } catch (IOException e) {
                    System.out.println("Desconectado");
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    return;
                }
            }
            System.out.println("Salida");
            try {
                System.out.println(in.readLine().trim());
                in.close();
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }
}
