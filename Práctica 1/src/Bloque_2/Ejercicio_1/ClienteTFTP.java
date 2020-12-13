package Bloque_2.Ejercicio_1;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * Miguel Ángel Ruiz Gómez
 */


public class ClienteTFTP {
    //Opciones iniciales
    private static DatagramSocket socket = null;
    private static InetAddress serverAdress;
    private static String consola = "TFTP> ";
    private static String mode = TFTPacket.mode;
    public static boolean conectado = false;

    public static void getCommand(BufferedReader stdin)  {
        String[] inputArgs = new String[0];
        try {
            inputArgs = stdin.readLine().toLowerCase().trim().split("\\s+");
        } catch (IOException e) {
            System.out.println("Error en comando");
        }

        switch (inputArgs[0]) {
            case "connect":
                if (inputArgs.length < 2) {
                    System.out.println("Uso:    connect <IP>");
                } else {
                    connect(inputArgs[1]);
                }
                break;
            case "get":
                if (inputArgs.length < 2) {
                    System.out.println("Uso:    get <IP>* <Archivo>");
                } else {
                    get(inputArgs);
                }
                break;
            case "quit":
                quit();
                break;
            case "help":
                help();
                break;
            case "put":
                if (inputArgs.length < 2) {
                    System.out.println("Uso:    put <IP>* <Archivo>");
                } else {
                    put(inputArgs);
                }
                break;
            case "mode":
                if (inputArgs.length < 2) {
                    System.out.println("Uso:    mode <Modo> ascii/binary");
                } else {
                    mode(inputArgs[1]);
                }
                break;
            case "debug":
                System.out.print("La simulación de perdida de paquetes está ");
                System.out.print(TFTPacket.simulacion ? "activada\n" : "desactivada\n");
                System.out.println("Para cambiar el parametro modifica la variable simulacion en TFTPacket.java");
                break;
            case "":
                break;
            default:
                System.out.println("Comando no valido");
                break;
        }
    }

    public static void mode(String modo) {
        switch (modo) {
            case "ascii":
                mode = modo;
                System.out.println("Modo configurado en: " + mode);
                break;
            case "binary":
                mode = modo;
                System.out.println("Modo configurado en: " + mode);
                break;
            default:
                System.out.println("Modos aceptados: ascii / binary");
        }
    }

    public static void help() {
        System.out.print("Comandos: \n" +
                "\tconnect\t<IP>\t\t\tConectar a servidor\n" +
                "\tget\t\t<IP>* <Archivo>\tPedir archivo\n" +
                "\tquit\t\t\t\t\tDesconectar del servidor\n" +
                "\thelp\t\t\t\t\tInfo sobre comandos\n" +
                "\tput\t\t<IP>* <Archivo>\tSubir archivo a servidor\n" +
                "\tmode\t<Modo>\t\t\tElige el modo de transferencia (ascii/binary)\n" +
                "\tdebug\t\t\t\t\tCheckea si la simulación de perdida de paquetes está activa\n");
    }

    public static void connect(String ip) {
        try {
            serverAdress = InetAddress.getByName(ip);
            if (!serverAdress.isReachable(4000)) {
                System.out.println("El servidor no se encuentra");
            } else {
                //System.out.println("Conectando a " + serverAdress.getCanonicalHostName() + " en el puerto " + TFTPacket.PORT);
                consola = "TFTP@" + serverAdress.getCanonicalHostName() + ":" + TFTPacket.PORT + "> ";
                conectado = true;
            }
        } catch (IOException e) {
            System.out.println("Error en connect");
        }
    }

    public static void quit() {
        System.out.println("Bye!");
        exit(0);
    }

    public static void put(String[] inputArgs)  {
        try {
            socket = new DatagramSocket();
            String fname;
            if (inputArgs.length == 3) {
                serverAdress = InetAddress.getByName(inputArgs[1]);
                fname = inputArgs[2];
            } else if (inputArgs.length == 2 && conectado) {
                fname = inputArgs[1];
            } else {
                throw new RuntimeException();
            }

            int timeOut = 5;
            socket.setSoTimeout(1000);
            FileInputStream fileInputStream = new FileInputStream(fname);
            TFTPacket.TFTPWRQ sWRQ = new TFTPacket.TFTPWRQ(fname, mode);
            sWRQ.envia(serverAdress, TFTPacket.PORT, socket);
            TFTPacket rWRQ = TFTPacket.recibe(socket);

            int nPort = rWRQ.getPort();

            if (rWRQ instanceof TFTPacket.TFTPError) {
                fileInputStream.close();
                throw new TFTPException();
            }

            float contPerdidos = 0;
            float contRetrans = 0;
            float bloque = 1;
            for (int numRead = TFTPacket.PACKET_LENGHT; numRead == TFTPacket.PACKET_LENGHT; bloque++) {
                TFTPacket.TFTPData envio = new TFTPacket.TFTPData((int) bloque, fileInputStream);
                numRead = envio.getLength();
                envio.envia(serverAdress, nPort, socket);

                while (timeOut != 0) {
                    try {
                        TFTPacket ack = TFTPacket.recibe(socket);
                        if (ack instanceof TFTPacket.TFTPACK) {
                            TFTPacket.TFTPACK ackDATA = (TFTPacket.TFTPACK) ack;

                            if (nPort != ackDATA.getPort()) {
                                TFTPacket.TFTPError error = new TFTPacket.TFTPError(0, "Cliente ocupado");
                                error.envia(ackDATA.getHost(), ackDATA.getPort(), socket);
                                continue;
                            }

                            if (ackDATA.getNum() != bloque) {
                                //System.out.println("AAAA");
                                contPerdidos++;
                                throw new SocketTimeoutException("Paquete perdido, reenviando");
                            }
                        }
                        break;
                    } catch (SocketTimeoutException se) {
                        System.out.print("(R)");
                        envio.envia(serverAdress, nPort, socket);
                        contRetrans++;
                        timeOut--;
                    }
                }

                if (timeOut == 0) {
                    if (numRead < TFTPacket.PACKET_LENGHT) System.out.println("Posible perdida del último ACK");


                    System.out.println("%P: " + (contPerdidos / bloque) * 100 + " %R: " + (contRetrans / bloque) * 100);
                    throw new TFTPException();
                }
            }
            System.out.println("%P: " + (contPerdidos / bloque) * 100 + " %R: " + (contRetrans / bloque) * 100);
            fileInputStream.close();

        } catch (UnknownHostException ie) {
            System.out.println("IP incorrecta o comando mal escrito, uso correcto:\n put <IP> <Archivo>");
        } catch (FileNotFoundException fe) {
            System.out.println("Archivo no encontrado");
        } catch (TFTPException | IOException ignored) {
        } catch (RuntimeException ee) {
            System.out.println("Error en comandos");
        }
        socket.close();
        socket = null;
        consola = "TFTP> ";
        conectado = false;

           /* byte[] buf = new byte[DATA_LENGTH];
            String filename = inputArgs[1];

            // Mandar WRQ
            DatagramPacket envioWRQ = paquete.crearWRQ(filename);
            socket.send(envioWRQ);
            DatagramPacket recepcionACK = new DatagramPacket(buf, buf.length);
            socket.receive(recepcionACK);

            int nPort = recepcionACK.getPort();
            paquete.setPort(nPort);
            byte[] datos = recepcionACK.getData();
            byte[] codigo = paquete.getCode(datos);

            if (codigo[1] == 5) { // Error
                String errorMsg = paquete.getError(datos);
                System.out.println(errorMsg);

            } else if (codigo[1] == 4) { // Es ACK
                try (FileInputStream in = new FileInputStream(new File(filename))) {
                    int tam = (int) f.length();
                    System.out.println("Se va a enviar: " + filename + " de tamaño: " + tam);

                    int num = PACKET_LENGHT;

                    for (int i = 1; num == PACKET_LENGHT; i++) {
                        DatagramPacket envio = paquete.crearData(i, in);
                        num = paquete.getLastLength();
                        System.out.println(num);

                        socket.send(envio);

                        // Espero ACK
                        recepcionACK = new DatagramPacket(buf, buf.length);
                        socket.receive(recepcionACK);
                        System.out.println(paquete.getACKNum(recepcionACK.getData()) + " " + i);
                    }
                }
            }*/

    }

    public static void get(String[] inputArgs) {
        String fname = null;
        try {
            socket = new DatagramSocket();
            if (inputArgs.length == 3) {
                serverAdress = InetAddress.getByName(inputArgs[1]);
                fname = inputArgs[2].toLowerCase();
            } else if (inputArgs.length == 2 && conectado) {
                fname = inputArgs[1];
            } else {
                throw new RuntimeException();
            }

            socket.setSoTimeout(1000);
            int timeOut = 5;
            FileOutputStream fileOutputStream = new FileOutputStream(fname);
            TFTPacket.TFTPRRQ sRRQ = new TFTPacket.TFTPRRQ(fname, mode);
            sRRQ.envia(serverAdress, TFTPacket.PORT, socket);
            TFTPacket.TFTPACK ack;
            int nPort = 0;
            InetAddress nIP;

            float contPerdidos = 0;
            float contRetrans = 0;
            float bloque = 1;
            for (int bytes = TFTPacket.DATA_LENGTH; bytes == TFTPacket.DATA_LENGTH; bloque++) {
                while (timeOut != 0) {
                    try {
                        TFTPacket packet = TFTPacket.recibe(socket);

                        if (packet instanceof TFTPacket.TFTPError) {
                            fileOutputStream.close();
                            throw new TFTPException();

                        } else if (packet instanceof TFTPacket.TFTPData) {
                            TFTPacket.TFTPData data = (TFTPacket.TFTPData) packet;

                            if (nPort != 0 && nPort != data.getPort()) {
                                TFTPacket.TFTPError error = new TFTPacket.TFTPError(0, "Cliente ocupado");
                                error.envia(data.getHost(), data.getPort(), socket);
                                continue;
                            }
                            nPort = data.getPort();
                            nIP = data.getHost();

                            if (bloque != data.getNum()) {
                                contPerdidos++;
                                throw new SocketTimeoutException();
                            }

                            bytes = data.write(fileOutputStream);
                            ack = new TFTPacket.TFTPACK((int) bloque);
                            ack.envia(nIP, nPort, socket);
                            break;
                        }

                    } catch (SocketTimeoutException se) {
                        if (bloque == 1) {
                            System.out.print("(R)");
                            sRRQ.envia(serverAdress, nPort, socket);
                            timeOut--;

                        } else {
                            ack = new TFTPacket.TFTPACK((int) (bloque - 1));
                            System.out.print("(R)");
                            ack.envia(serverAdress, nPort, socket);
                            contRetrans++;
                            timeOut--;
                        }
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

        } catch (UnknownHostException ie) {
            System.out.println("IP incorrecta o Comando mal escrito, uso correcto:\n get <IP> <Archivo>");
        } catch (TFTPException | IOException e) {
            assert fname != null;
            File malo = new File(fname);
            malo.delete();
        } catch (RuntimeException ee) {
            System.out.println("Error en comandos");
        }
        socket.close();
        socket = null;
        consola = "TFTP> ";
        conectado = false;
            /* ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            String filename = inputArgs[1];
            byte[] buf = new byte[PACKET_LENGHT];

            // Mandar RRQ
            DatagramPacket envio = paquete.crearRRQ(filename);
            socket.send(envio);

            // Recibiendo paquete
            boolean continua = true;

            while (continua) {
                DatagramPacket recepcion = new DatagramPacket(buf, buf.length);
                socket.receive(recepcion);
                byte[] datos = recepcion.getData();
                byte[] codigo = paquete.getCode(datos);

                // Datos conseguidos
                if (codigo[1] == 5) { // Error
                    String errorMsg = paquete.getError(datos);
                    System.out.println(errorMsg);
                    continua = false;

                } else if (recepcion.getLength() < PACKET_LENGHT && codigo[1] == 3) {
                    try (FileOutputStream fileOutputStream = new FileOutputStream(filename)) {
                        byte[] fileBytes = paquete.getData(datos);
                        outputStream.write(fileBytes);
                        fileOutputStream.write(outputStream.toByteArray());
                    }

                    byte[] bNum = paquete.getNum(datos);
                    DatagramPacket ack = paquete.crearACK(bNum, recepcion.getPort());
                    socket.send(ack);
                    System.out.println("Paquete transferido");
                    continua = false;

                } else if (codigo[1] == 3) {
                    System.out.println("Transfiriendo");
                    outputStream.write(paquete.getData(datos));

                    byte[] bNum = paquete.getNum(datos);
                    DatagramPacket ack = paquete.crearACK(bNum, recepcion.getPort());
                    socket.send(ack);
                }
            }*/

    }

    public static void main(String[] args) {
        // Lectura de mensajes
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Comandos implementados: connect, get, put, quit, mode, debug. Para mas info use el comando: help");
        System.out.println("Modo por defecto: " + mode);
        System.out.println(TFTPacket.simulacion ? "La simulacion está activada" : "Las simulacion está desactivada");

        while (true) {
            System.out.print(consola);
            getCommand(stdin);
        }
    }
}
