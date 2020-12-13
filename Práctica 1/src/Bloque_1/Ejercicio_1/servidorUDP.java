// Miguel Angel Ruiz Gomez

package Bloque_1.Ejercicio_1;

import java.io.IOException;
import java.net.*;

public class servidorUDP {
    private static final int PORT = 1234;
    private static final int BUFFER_LENGTH = 255;

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(PORT);
        System.out.println("Socket creado, puerto: " + PORT);
        DatagramPacket packet = new DatagramPacket(new byte[BUFFER_LENGTH], BUFFER_LENGTH);

        try {
            while (true) {
                socket.receive(packet);
                //System.out.println("Nuevo cliente conectado");
                new servidorUDPHelper(packet).start();
                packet.setLength(BUFFER_LENGTH);
            }
        } catch (SocketTimeoutException se) {
            throw new SocketTimeoutException("Tiempo máximo de espera superado");
        }
    }
}

class servidorUDPHelper extends Thread {
    private byte[] datos;
    private InetAddress address;
    private int port;
    private static final int BUFFER_LENGTH = 255;

    public servidorUDPHelper(DatagramPacket dp) {
        this.address = dp.getAddress();
        this.datos = dp.getData();
        this.port = dp.getPort();
    }

    @Override
    public void run() {
        try (DatagramSocket ds = new DatagramSocket()) {
            DatagramPacket paqueteReenvio = new DatagramPacket(datos, datos.length, address, port);
            DatagramPacket paqueteRecibido = new DatagramPacket(new byte[BUFFER_LENGTH], BUFFER_LENGTH);

            ds.send(paqueteReenvio);
            System.out.println("Paquete reenviado a dirección: " + address.getCanonicalHostName() + ", puerto: " + port);

            String msg = new String(paqueteRecibido.getData()).trim();

            while (!msg.equals(".")) {
                // Cojo paquete recibido
                paqueteRecibido = new DatagramPacket(new byte[BUFFER_LENGTH], BUFFER_LENGTH);

                while (true){
                    ds.receive(paqueteRecibido);

                    if (paqueteRecibido.getPort() != port) continue; // Ignora el paquete
                    break;
                }

                msg = new String(paqueteRecibido.getData()).trim();
                //System.out.println(msg);
                // Nuevo paquete a enviar
                paqueteReenvio.setData(msg.getBytes());
                paqueteReenvio.setLength(msg.getBytes().length);
                // Reenvio
                ds.send(paqueteReenvio);
                System.out.println("Paquete reenviado a dirección: " + address.getCanonicalHostName() + ", puerto: " + port);
            }
            // Cierro conexión y hebra
            ds.close();
            System.out.println("Hebra cerrada por .");

        } catch (Exception e) {
            System.out.println("Hebra cerrada inesperadamente");
            Thread.currentThread().interrupt();
        }
    }
}
