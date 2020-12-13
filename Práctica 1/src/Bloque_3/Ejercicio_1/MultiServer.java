package Bloque_3.Ejercicio_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class MultiServer {
    private static final int PORT_1 = 1234;
    private static final int PORT_2 = 4321;


    public static void main(String[] args) throws IOException {
        // Apertura de canales TCP
        ServerSocketChannel serverSocketChannel_1 = ServerSocketChannel.open();
        serverSocketChannel_1.socket().bind(new InetSocketAddress(PORT_1));

        ServerSocketChannel serverSocketChannel_2 = ServerSocketChannel.open();
        serverSocketChannel_2.socket().bind(new InetSocketAddress(PORT_2));

        // Lectura no bloqueante
        serverSocketChannel_1.configureBlocking(false);
        serverSocketChannel_2.configureBlocking(false);

        // Selector y registro
        Selector selector = Selector.open();
        SelectionKey selectionKey_1 = serverSocketChannel_1.register(selector, SelectionKey.OP_ACCEPT);
        SelectionKey selectionKey_2 = serverSocketChannel_2.register(selector, SelectionKey.OP_ACCEPT);


        while (true) {
            selector.select(); // Llamada bloqueante
            Set<SelectionKey> keys = selector.selectedKeys();

            for (Iterator<SelectionKey> i = keys.iterator(); i.hasNext(); ) {
                SelectionKey selectionKey = i.next();
                i.remove();

                if ((selectionKey == selectionKey_1 || selectionKey == selectionKey_2) && selectionKey.isAcceptable()) {
                    nuevaPeticionTCP((ServerSocketChannel) selectionKey.channel(), selector);
                } else {
                    gestionarPeticionTCP(selectionKey);
                }

                if (selectionKey.isValid() && selectionKey.isWritable()) {
                    ByteBuffer buf = (ByteBuffer) selectionKey.attachment();
                    buf.flip();
                    SocketChannel client = (SocketChannel) selectionKey.channel();

                    //System.out.println(client.socket().getLocalPort() == PORT_2);
                    if (client.socket().getLocalPort() == PORT_2) {
                        String output = new String(buf.array()).toUpperCase().trim();
                        System.out.println("Mensaje leido: " + output);
                        ByteBuffer buf1 = ByteBuffer.allocate(buf.capacity());
                        buf1.put(output.getBytes());
                        buf1.putChar('\n');
                        buf1.flip();
                        client.write(buf1);

                        if (!buf1.hasRemaining() ) { // Fin operación escritura
                            selectionKey.interestOps(SelectionKey.OP_READ);
                        }
                        //System.out.println(new String(buf.array()));
                    } else {
                        client.write(buf);
                        if (!buf.hasRemaining() ) { // Fin operación escritura
                            selectionKey.interestOps(SelectionKey.OP_READ);
                        }
                    }


                    buf.compact();
                }
            }
        }
    }


    private static void gestionarPeticionTCP(SelectionKey selectionKey) throws IOException {
        if (!selectionKey.isReadable()) return;

        SocketChannel client = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
        byteBuffer.clear();

        //client.read(byteBuffer);
        //System.out.println(new String(byteBuffer.array()).trim());
        int bytesread = client.read(byteBuffer);

        if (bytesread == -1) {// Se ha cerrado la conexión?
            selectionKey.cancel();
            client.close();
            System.out.println("Conexion terminada");
            return;
        }
        selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    private static void nuevaPeticionTCP(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        System.out.println("Nueva conexion TCP");
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(512));
        //System.out.println(x.isWritable());
    }

}
