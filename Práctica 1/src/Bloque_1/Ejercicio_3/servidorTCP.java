package Bloque_1.Ejercicio_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class servidorTCP {
    private static final int PORT = 1234;
    private static final int TIMEOUT_NEW_CONNECTIONS = 3*60000;
    private static final int TIMEOUT_SERVICE = 60000;


    public static void main(String[] args) {
        Logger logger = null;
        ExecutorService service;
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            // Opciones EJ3
            serverSocket.setReuseAddress(true);
            serverSocket.setSoTimeout(TIMEOUT_NEW_CONNECTIONS);

            logger = Logger.getLogger("servidorTCPecho");
            service = Executors.newCachedThreadPool();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                // Opciones EJ3
                clientSocket.setTcpNoDelay(true);
                clientSocket.setSoTimeout(TIMEOUT_SERVICE);

                service.execute(new servidorTCPHelper(clientSocket, logger));
            }
        } catch (IOException e) {
            Objects.requireNonNull(logger).info("Fin del servicio");

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
