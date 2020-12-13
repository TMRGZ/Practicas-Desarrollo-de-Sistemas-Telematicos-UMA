package Bloque_2.Ejercicio_1;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

class TFTPException extends RuntimeException {
    public TFTPException() {
        super();
    }

    public TFTPException(String s, int i, TFTPacket packet, DatagramSocket socket) throws IOException {
        super(s);
        TFTPacket.TFTPError error = new TFTPacket.TFTPError(i, s);
        error.envia(packet.getHost(), packet.getPort(), socket);
    }
}

public class TFTPacket {
    // Constantes
    public static final int DATA_LENGTH = 512;
    public static final int PACKET_LENGHT = 516;
    public static final int PORT = 6969;
    public static final boolean simulacion = true;

    // Codigos
    private static final byte[] rrqCode = {0, 1};
    private static final byte[] wrqCode = {0, 2};
    private static final byte[] dataCode = {0, 3};
    private static final byte[] ackCode = {0, 4};
    private static final byte[] errorCode = {0, 5};

    //Offsets
    static final int opOffset = 0;
    static final int fileOffset = rrqCode.length;
    private static final int blkOffset = dataCode.length;
    static final int dataOffset = dataCode.length + blkOffset;
    private static final int numOffset = errorCode.length;
    static final int msgOffset = errorCode.length + numOffset;

    //Paquete
    byte[] datos;
    protected int length;
    protected static String mode = "ascii";

    // Informacion de envio
    private InetAddress host;
    private int port;

    public TFTPacket() {
        this.datos = new byte[PACKET_LENGHT];
        length = PACKET_LENGHT;
    }

    public static TFTPacket recibe(DatagramSocket socket) throws IOException {
        TFTPacket recibido = new TFTPacket(), packet = new TFTPacket();

        DatagramPacket paqueteRecibido = new DatagramPacket(recibido.datos, recibido.length);
        socket.receive(paqueteRecibido);
        int codigo = recibido.get(0);

        if (codigo == rrqCode[1]) {
            packet = new TFTPRRQ();
        } else if (codigo == wrqCode[1]) {
            packet = new TFTPWRQ();
        } else if (codigo == dataCode[1]) {
            packet = new TFTPData();
        } else if (codigo == ackCode[1]) {
            packet = new TFTPACK();
        } else if (codigo == errorCode[1]) {
            packet = new TFTPError();
        }

        packet.datos = paqueteRecibido.getData();
        packet.length = paqueteRecibido.getLength();
        packet.host = paqueteRecibido.getAddress();
        packet.port = paqueteRecibido.getPort();

        if (packet instanceof TFTPRRQ) {
            System.out.println("                                            <------ RRQ " + ((TFTPRRQ) packet).getFileName() + " " + ((TFTPRRQ) packet).getMode() + " (" + packet.getHost().getCanonicalHostName() + ":" + packet.getPort() + ")");
        } else if (packet instanceof TFTPWRQ) {
            System.out.println("                                            <------ WRQ " + ((TFTPWRQ) packet).getFileName() + " " + ((TFTPWRQ) packet).getMode() + " (" + packet.getHost().getCanonicalHostName() + ":" + packet.getPort() + ")");
        } else if (packet instanceof TFTPError) {
            System.out.println("                                            <------ Error " + ((TFTPError) packet).getNum() + " " + ((TFTPError) packet).getMsg() + " (" + packet.getHost().getCanonicalHostName() + ":" + packet.getPort() + ")");
        } else if (packet instanceof TFTPData) {
            System.out.println("                                            <------ DATA " + ((TFTPData) packet).getNum() + " " + (packet.getLength() - 4) + " bytes" + " (" + packet.getHost().getCanonicalHostName() + ":" + packet.getPort() + ")");
        } else if (packet instanceof TFTPACK) {
            System.out.println("                                            <------ ACK " + (((TFTPACK) packet).getNum()) + " (" + packet.getHost().getCanonicalHostName() + ":" + packet.getPort() + ")");
        }
        return packet;
    }

    public void envia(InetAddress ip, int port, DatagramSocket s) throws IOException {
        if (this instanceof TFTPRRQ) {
            System.out.println("------> RRQ " + ((TFTPRRQ) this).getFileName() + " " + mode);
        } else if (this instanceof TFTPWRQ) {
            System.out.println("------> WRQ " + ((TFTPWRQ) this).getFileName() + " " + mode);
        } else if (this instanceof TFTPError) {
            System.out.println("------> Error " + ((TFTPError) this).getNum() + " " + ((TFTPError) this).getMsg());
        } else if (this instanceof TFTPData) {
            System.out.println("------> DATA " + ((TFTPData) this).getNum() + " " + (this.getLength() - 4) + " bytes");
        } else if (this instanceof TFTPACK) {
            System.out.println("------> ACK " + (((TFTPACK) this).getNum() + 1));
        }

        if ((!(this instanceof TFTPData) && !(this instanceof TFTPACK))) {
            s.send(new DatagramPacket(datos, length, ip, port));
        } else {
            float perdida = this instanceof TFTPData ? ThreadLocalRandom.current().nextInt(0, (this.getLength())) : ThreadLocalRandom.current().nextFloat() % (float) this.getLength();

            //System.out.println("Rango: " + 0 +":"+ (this.getLength() * 0.05) + " Toca: " + perdida);

            if (((perdida <= 0 || perdida > (this.getLength() * 0.05)) || !simulacion)) {
                s.send(new DatagramPacket(datos, length, ip, port));
            } else {
                //System.out.println("Toca");
                System.out.print("\n");
            }
        }
    }

    public InetAddress getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public int getLength() {
        return length;
    }

    protected void put(int i, short value) {
        datos[i++] = (byte) (value >>> 8);
        datos[i] = (byte) (value % 256);
    }

    protected void put(int i, String value) {
        value.getBytes(0, value.length(), datos, i);
        datos[i + value.length()] = (byte) 0;
    }

    protected int get(int i) {
        return (datos[i] & 0xff) << 8 | datos[i + 1] & 0xff;
    }

    protected String get(int i, byte del) {
        StringBuilder result = new StringBuilder();
        while (datos[i] != del) result.append((char) datos[i++]);
        return result.toString();
    }

    protected static void tratarAscii(File f) {
        System.out.println("Texto recibido:");

        try (Scanner sc = new Scanner(new FileReader(f))) {
            // System.out.println(sc.hasNextLine());
            while (sc.hasNextLine()) {
                System.out.println(sc.nextLine());
            }
            sc.close();
            f.delete();

        } catch (IOException e) {
            System.out.println("Error de lectura de fichero en modo ASCII");
        }
    }

    /*
        DEFINICIONES DE PAQUETES
    */

    public static final class TFTPData extends TFTPacket {
        private TFTPData() {
        }

        public TFTPData(int num, FileInputStream inputStream) throws IOException {
            this.datos = new byte[PACKET_LENGHT];
            this.put(opOffset, dataCode[1]);
            this.put(blkOffset, (short) num);
            length = inputStream.read(datos, dataOffset, DATA_LENGTH) + 4;
        }

        public int getNum() {
            return this.get(blkOffset);
        }

        public int write(FileOutputStream outputStream) throws IOException {
            outputStream.write(datos, dataOffset, length - 4);
            return length - 4;
        }
    }

    public static final class TFTPError extends TFTPacket {
        private TFTPError() {
        }

        public TFTPError(int num, String msg) {
            length = 4 + msg.length() + 1;
            this.datos = new byte[length];

            put(opOffset, errorCode[1]);
            put(numOffset, (short) num);
            put(msgOffset, msg);
        }

        public int getNum() {
            return this.get(numOffset);
        }

        public String getMsg() {
            return this.get(msgOffset, (byte) 0);
        }
    }

    public static final class TFTPACK extends TFTPacket {
        protected TFTPACK() {
        }

        public TFTPACK(int num) {
            length = 4;
            this.datos = new byte[length];
            put(opOffset, ackCode[1]);
            put(blkOffset, (short) num);
        }

        public int getNum() {
            return this.get(blkOffset);
        }
    }

    public static final class TFTPRRQ extends TFTPacket {
        protected TFTPRRQ() {
        }

        public TFTPRRQ(String filename, String mode) {
            TFTPacket.mode = mode;
            length = 2 + filename.length() + 1 + TFTPacket.mode.length() + 1;
            datos = new byte[length];

            put(opOffset, rrqCode[1]);
            put(fileOffset, filename);
            put(fileOffset + filename.length() + 1, TFTPacket.mode);
        }

        public String getFileName() {
            return this.get(fileOffset, (byte) 0);
        }

        public String getMode() {
            return this.get(fileOffset + getFileName().length() + 1, (byte) 0);
        }
    }

    public static final class TFTPWRQ extends TFTPacket {
        protected TFTPWRQ() {
        }

        public TFTPWRQ(String filename, String mode) {
            TFTPacket.mode = mode;
            length = 2 + filename.length() + 1 + TFTPacket.mode.length() + 1;
            datos = new byte[length];

            put(opOffset, wrqCode[1]);
            put(fileOffset, filename);
            put(fileOffset + filename.length() + 1, TFTPacket.mode);

            //System.out.println("WRQ " + filename + " " + mode);
        }

        public String getFileName() {
            return this.get(fileOffset, (byte) 0);
        }

        public String getMode() {
            return this.get(fileOffset + getFileName().length() + 1, (byte) 0);
        }

    }
}
