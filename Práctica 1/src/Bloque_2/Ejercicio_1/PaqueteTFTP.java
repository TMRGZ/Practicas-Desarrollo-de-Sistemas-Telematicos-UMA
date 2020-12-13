package Bloque_2.Ejercicio_1;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;


/**
 Miguel Ángel Ruiz Gómez

 */


public class PaqueteTFTP {
    private final byte[] rrqCode = {0, 1};
    private final byte[] wrqCode = {0, 2};
    private final byte[] dataCode = {0, 3};
    private final byte[] ackCode = {0, 4};
    private final byte[] errorCode = {0, 5};

    // Constantes
    private static final int DATA_LENGTH = 512;
    private static final int PACKET_LENGHT = 516;

    private static String cMode = "octet";
    private static byte term = 0;

    //Variables constructor
    private int length;
    private InetAddress serverAddress;
    private int port;
    private int lastLength;

    public PaqueteTFTP(int length, InetAddress serverAddress, int port) {
        this.length = length;
        this.serverAddress = serverAddress;
        this.port = port;
        this.lastLength = PACKET_LENGHT;
    }

    public DatagramPacket crearRRQ(String strFileName) {
        // 2 + N + 1 + N + 1

        byte[] fileName = strFileName.getBytes();
        byte[] mode = cMode.getBytes();
        int len = rrqCode.length + fileName.length + mode.length + 2;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(len);

        try {
            outputStream.write(rrqCode);
            outputStream.write(fileName);
            outputStream.write(term);
            outputStream.write(mode);
            outputStream.write(term);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] leerPaqueteArray = outputStream.toByteArray();
        return new DatagramPacket(leerPaqueteArray, leerPaqueteArray.length, serverAddress, port);
    }

    public DatagramPacket crearWRQ(String strFileName) {
        byte[] fileName = strFileName.getBytes();
        byte[] mode = cMode.getBytes();
        int len = wrqCode.length + fileName.length + mode.length + 2;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(len);

        try {
            outputStream.write(wrqCode);
            outputStream.write(fileName);
            outputStream.write(term);
            outputStream.write(mode);
            outputStream.write(term);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] leerPaqueteArray = outputStream.toByteArray();
        return new DatagramPacket(leerPaqueteArray, leerPaqueteArray.length, serverAddress, port);
    }

    public DatagramPacket crearACK(byte[] bNum, int num) {
        int len = ackCode.length + bNum.length;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(len);
        try {
            outputStream.write(ackCode);
            outputStream.write(bNum);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] leerPaqueteArray = outputStream.toByteArray();
        return new DatagramPacket(leerPaqueteArray, leerPaqueteArray.length, serverAddress, num);
    }

    public DatagramPacket crearError(int codigo, String msgError) {
        int len = errorCode.length + 2 + msgError.getBytes().length + 1;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(len);

        try {
            outputStream.write(errorCode);
            outputStream.write(Integer.toString(codigo).getBytes());
            outputStream.write(msgError.getBytes());
            outputStream.write(term);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] leerPaqueteArray = outputStream.toByteArray();
        return new DatagramPacket(leerPaqueteArray, leerPaqueteArray.length, serverAddress, port);
    }

    public DatagramPacket crearData(int numi, FileInputStream datos) throws IOException {
        /*int len = dataCode.length + num.length + datos.length;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(len);

        try {
            outputStream.write(dataCode);
            outputStream.write(num);
            outputStream.write(datos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] leerPaqueteArray = outputStream.toByteArray();
        System.out.println(Arrays.toString(leerPaqueteArray));*/
        short num = (short) numi;
        byte[] mensaje = new byte[length];
        int i = 0;
        mensaje[i++] = dataCode[0];
        mensaje[i++] = dataCode[1];
        mensaje[i++] = (byte) (num >>> 8);
        mensaje[i] = (byte) (num % 256);

        this.lastLength = datos.read(mensaje, 4, DATA_LENGTH) + 4;


        return new DatagramPacket(mensaje, mensaje.length, serverAddress, port);
    }

    public byte[] getCode(byte[] codigos) {
        byte[] code = new byte[2];
        code[0] = codigos[0];
        code[1] = codigos[1];
        return code;
    }

    public String getError(byte[] datos) throws IOException {
        byte[] errorBytes = new byte[datos.length - 5];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(errorBytes.length);

        int j = 0;
        for (int i = 4; i < datos.length - 1; i++) {
            errorBytes[j++] = datos[i];
        }

        outputStream.write(errorBytes);

        return outputStream.toString().trim();
    }

    public byte[] getData(byte[] datos) {
        byte[] data = new byte[length - 4];
        int j = 0;

        for (int i = 4; i < datos.length; i++) {
            data[j++] = datos[i];
        }

        return data;
    }

    public byte[] getNum(byte[] datos) {
        //System.out.println(Arrays.toString(datos));
        byte[] bNum = new byte[2];
        bNum[0] = datos[2];
        bNum[1] = datos[3];
        return bNum;
    }

    public int getACKNum(byte[] datos) {
        ByteBuffer bf = ByteBuffer.wrap(getNum(datos));
        //System.out.println(Arrays.toString(datos));
        return bf.getShort();
    }

    public int getLastLength() {
        return lastLength;
    }

    public void setPort(int nPort){
        this.port = nPort;
    }
}
