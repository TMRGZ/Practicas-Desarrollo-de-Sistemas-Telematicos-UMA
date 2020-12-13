// Miguel Angel Ruiz Gomez

package Bloque_1.Ejercicio_1;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class clienteUDP {
    private static final int PORT = 1234;
    private static final int MAX_RETRANSMISSIONS = 5;
    private static final int TEMP = 2000;

    public static void main(String[] args) throws IOException {
        if ((args.length < 1) || args.length > 2)
            throw new IllegalArgumentException("Parametros incorrectos, servidor / puerto");

        // Gestion inicial
        InetAddress serverAdress = InetAddress.getByName(args[0]);
        int serverPort = args.length == 2 ? Integer.parseInt(args[1]) : PORT;
        // Creacion de socket
        DatagramSocket socket = new DatagramSocket(); // No hace falta asignar puerto manualmente
        System.out.println("Socket creado con IP: " + serverAdress.getCanonicalHostName() + " puerto: " + serverPort);
        // Lectura de mensajes
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        ByteArrayOutputStream bytesToSend;

        boolean funcionando = true;
        boolean puertoFijo = false;

        do {
            String message = stdin.readLine();
            byte[] bytesToMsg = message.getBytes();
            bytesToSend = new ByteArrayOutputStream(bytesToMsg.length);
            bytesToSend.write(bytesToMsg);
            DatagramPacket sendPacket = new DatagramPacket(bytesToSend.toByteArray(), bytesToSend.size(), serverAdress, serverPort);
            DatagramPacket receivePacket = new DatagramPacket(new byte[bytesToSend.size()], bytesToSend.size());

            socket.send(sendPacket);
            System.out.println("Paquete enviado, dirección: " + serverAdress.getCanonicalHostName() + ", puerto: " + serverPort);
            socket.setSoTimeout(TEMP);
            int timeOut = MAX_RETRANSMISSIONS;

            while (timeOut != 0) {
                try {
                    socket.receive(receivePacket);
                    System.out.println("ECO: " + new String(receivePacket.getData()));

                    if (!puertoFijo){
                        serverPort = receivePacket.getPort();
                        puertoFijo = true;
                    }

                    if (receivePacket.getPort() != serverPort) continue; // Ignora el paquete

                    break;
                } catch (SocketTimeoutException se) {
                    socket.send(sendPacket);
                    System.out.println("Paquete retransmitido");
                    timeOut--;
                }
            }

            if (message.equals(".")) {
                funcionando = false;
                System.out.println("Cerrando cliente...");
            }

            if (timeOut == 0) {
                System.out.println("Limite timout alcanzado");
                funcionando = false;
            }
        } while (funcionando);
        socket.close();
        stdin.close();
        bytesToSend.close();
        System.out.println("Cliente cerrado");


    }
}
