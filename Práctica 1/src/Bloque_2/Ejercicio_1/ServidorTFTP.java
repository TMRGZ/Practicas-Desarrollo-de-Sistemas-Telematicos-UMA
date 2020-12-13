package Bloque_2.Ejercicio_1;

import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Scanner;


/**
 * Miguel Ángel Ruiz Gómez
 */

public class ServidorTFTP {
    private static final int PORT = TFTPacket.PORT;
    private static final int TIME_OUT = 100000;

    private static void WRQ(DatagramSocket socket, String fname, InetAddress clientAdress, int port, String mode) {
        try {
            int timeOut = 5;
            FileOutputStream fileOutputStream = new FileOutputStream(fname);
            socket.setSoTimeout(1000);

            float contPerdidos = 0;
            float contRetrans = 0;
            float bloque = 1;
            for (int bytes = TFTPacket.DATA_LENGTH; bytes == TFTPacket.DATA_LENGTH; bloque++) {
                TFTPacket packet;
                while (timeOut != 0) {
                    try {
                        packet = TFTPacket.recibe(socket);

                        if (packet instanceof TFTPacket.TFTPError) {
                            throw new TFTPException();

                        } else if (packet instanceof TFTPacket.TFTPData) {
                            TFTPacket.TFTPData data = (TFTPacket.TFTPData) packet;

                            if (data.getPort() != port){
                                TFTPacket.TFTPError error = new TFTPacket.TFTPError(0, "Servidor ocupado");
                                error.envia(data.getHost(), data.getPort(), socket);
                                continue;
                            }

                            if (bloque != data.getNum()) {
                                contPerdidos++;
                                throw new SocketTimeoutException();
                            }

                            bytes = data.write(fileOutputStream);
                            TFTPacket.TFTPACK ack = new TFTPacket.TFTPACK((int) bloque);
                            ack.envia(clientAdress, port, socket);
                            break;
                        }
                    } catch (SocketTimeoutException se) {
                        System.out.print("(R)");
                        TFTPacket.TFTPACK ackr = new TFTPacket.TFTPACK((int) (bloque - 1));
                        ackr.envia(clientAdress, port, socket);
                        contRetrans++;
                        timeOut--;
                    }
                }
                if (timeOut == 0) {
                    System.out.println("%P: " + (contPerdidos / bloque) * 100 + " %R: " + (contRetrans / bloque) * 100);
                    fileOutputStream.close();
                    throw new TFTPException();
                }
            }
            fileOutputStream.close();
            System.out.println("%P: " + (contPerdidos / bloque) * 100 + " %R: " + (contRetrans / bloque) * 100);

            if (mode.equalsIgnoreCase("ascii")) TFTPacket.tratarAscii(new File(fname));

        } catch (TFTPException | IOException e) {
            File malo = new File(fname);
            malo.delete();
        }
    }

    private static void RRQ(DatagramSocket socket, String fname, InetAddress clientAdress, int port) {
        try {
            int timeOut = 5;
            FileInputStream fileInputStream = new FileInputStream(fname);
            socket.setSoTimeout(1000);

            float contPerdidos = 0;
            float contRetrans = 0;
            float bloque = 1;
            for (int numRead = TFTPacket.PACKET_LENGHT; numRead == TFTPacket.PACKET_LENGHT; bloque++) {
                TFTPacket.TFTPData out = new TFTPacket.TFTPData((int) bloque, fileInputStream);
                numRead = out.getLength();
                out.envia(clientAdress, port, socket);

                while (timeOut != 0) {
                    try {
                        TFTPacket packet = TFTPacket.recibe(socket);

                        if (packet instanceof TFTPacket.TFTPACK) {
                            TFTPacket.TFTPACK ack = (TFTPacket.TFTPACK) packet;

                            if (ack.getPort() != port){
                                TFTPacket.TFTPError error = new TFTPacket.TFTPError(0, "Servidor ocupado");
                                error.envia(ack.getHost(), ack.getPort(), socket);
                                continue;
                            }

                            if (ack.getNum() != bloque) {
                                contPerdidos++;
                                throw new SocketTimeoutException("");
                            }
                        }
                        break;
                    } catch (SocketTimeoutException e) {
                        System.out.print("(R)");
                        out.envia(clientAdress, port, socket);
                        contRetrans++;
                        timeOut--;
                    }
                }
                if (timeOut == 0) {
                    if (numRead < TFTPacket.PACKET_LENGHT) System.out.println("Posible perdida del último ACK");
                    System.out.println("%P: " + (contPerdidos / bloque) * 100 + " %R: " + (contRetrans / bloque) * 100);
                    fileInputStream.close();
                    throw new TFTPException();
                }
            }
            System.out.println("%P: " + (contPerdidos / bloque) * 100 + " %R: " + (contRetrans / bloque) * 100);
            fileInputStream.close();

        } catch (SocketException e) {
            System.out.println("Error de socket");
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado");
        } catch (IOException e) {
            System.out.println("Error de lectura");
        }
    }

    public static void main(String[] args)  {
        try {
            DatagramSocket socket = new DatagramSocket(PORT);
            System.out.println("Servidor TFTP iniciado en el puerto: " + PORT);
            System.out.println(TFTPacket.simulacion ? "La simulacion está activada" : "Las simulacion está desactivada");
            TFTPacket packet;

            while (!socket.isClosed()) {
                socket.setSoTimeout(TIME_OUT);
                try {
                    packet = TFTPacket.recibe(socket);
                    if (packet instanceof TFTPacket.TFTPWRQ) {
                        File guardar = new File(((TFTPacket.TFTPWRQ) packet).getFileName());
                        if (!guardar.exists()) {
                            TFTPacket.TFTPACK aWRQ = new TFTPacket.TFTPACK(0);
                            aWRQ.envia(packet.getHost(), packet.getPort(), socket);
                            WRQ(socket, ((TFTPacket.TFTPWRQ) packet).getFileName(), packet.getHost(), packet.getPort(), ((TFTPacket.TFTPWRQ) packet).getMode());

                        } else {
                            throw new TFTPException("File already exists", 6, packet, socket);
                        }

                    } else if (packet instanceof TFTPacket.TFTPRRQ) {
                        File cargar = new File(((TFTPacket.TFTPRRQ) packet).getFileName());
                        if (cargar.exists() && cargar.isFile() && cargar.canRead()) {
                            RRQ(socket, ((TFTPacket.TFTPRRQ) packet).getFileName(), packet.getHost(), packet.getPort());
                        } else {
                            throw new TFTPException("File not found", 1, packet, socket);
                        }
                    }
                } catch (TFTPException ignored) {

                } catch (SocketTimeoutException se) {
                    socket.close();
                }
            }
        } catch (IOException e) {
        }

        System.out.println("Servidor desconectado por inactividad");
    }

}
