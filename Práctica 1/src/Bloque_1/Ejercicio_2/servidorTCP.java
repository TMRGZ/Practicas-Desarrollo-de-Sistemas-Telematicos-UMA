// Miguel Angel Ruiz Gomez

package Bloque_1.Ejercicio_2;

import java.io.*;
import java.net.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class servidorTCP {
    private static final int PORT = 1234;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        Logger logger = Logger.getLogger("servidorTCPecho");
        Executor service = Executors.newCachedThreadPool();

        while (true) {
            Socket clientSocket = serverSocket.accept();
            service.execute(new servidorTCPHelper(clientSocket, logger));
        }
    }
}

class servidorTCPHelper extends Thread {
    private Socket clientSocket;
    private Logger logger;
    private BufferedReader in;
    private PrintWriter out;

    public servidorTCPHelper(Socket clientSocket, Logger logger) throws IOException {
        this.clientSocket = clientSocket;
        this.logger = logger;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        logger.info("Hebra " + clientSocket.getInetAddress().getCanonicalHostName() + ":" + clientSocket.getPort() + " conectada");
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = in.readLine().trim();
                logger.info("Mensaje recibido: " + msg + " de IP: " + clientSocket.getInetAddress().getCanonicalHostName() + " puerto: " + clientSocket.getPort());

                if (msg.equals(".")) {
                    msg = "Bye.";
                    out.println(msg);
                    break;
                }
                out.println(msg);
            }
            logger.info("Hebra " + clientSocket.getInetAddress().getCanonicalHostName() + " puerto: " + clientSocket.getPort() + " desconectada");
            clientSocket.close();
            in.close();
            out.close();

        } catch (IOException e) {
            try {
                logger.info("Hebra " + clientSocket.getInetAddress().getCanonicalHostName() + " puerto: " + clientSocket.getPort() + " desconectada");
                clientSocket.close();
                in.close();
                out.close();

            } catch (IOException ignored) { }

        }
    }
}
