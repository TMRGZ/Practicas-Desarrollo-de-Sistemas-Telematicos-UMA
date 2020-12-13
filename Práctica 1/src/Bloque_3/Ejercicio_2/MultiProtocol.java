package Bloque_3.Ejercicio_2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class MultiProtocol {
    private static final int PORT = 1234;

    public static void main(String[] args) throws IOException {
        // Apertura de canales TCP y UDP
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.bind(new InetSocketAddress(PORT));

        // Lectura no bloqueante
        serverSocketChannel.configureBlocking(false);
        datagramChannel.configureBlocking(false);

        // Selector y registro
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        datagramChannel.register(selector, SelectionKey.OP_READ, new ClientRecord());


        while (true) {
            selector.select(); // Llamada bloqueante
            Set<SelectionKey> keys = selector.selectedKeys();

            for (Iterator<SelectionKey> i = keys.iterator(); i.hasNext(); ) {
                SelectionKey selectionKey = i.next();
                i.remove();

                if (selectionKey.channel() instanceof ServerSocketChannel) { // Canal TCP pasivo
                    if (selectionKey.isAcceptable()) {  // Nueva peticion
                        nuevaPeticionTCP(serverSocketChannel, selector);
                    }

                } else if (selectionKey.channel() instanceof DatagramChannel) { // Canal UDP
                    if (selectionKey.isValid()) { // Llega un mensaje
                        gestionarPeticionUDP(selectionKey);
                    }
                } else if (selectionKey.channel() instanceof SocketChannel) { // Canal TCP Activo
                    gestionarPeticionTCP(selectionKey);
                }
            }
        }
    }

    private static void gestionarPeticionUDP(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isReadable()) { // Procesar mensaje
            DatagramChannel datagramChannel1 = (DatagramChannel) selectionKey.channel();
            ClientRecord cr = (ClientRecord) selectionKey.attachment();
            cr.buffer.clear();
            cr.clientAddress = datagramChannel1.receive(cr.buffer);

            if (cr.clientAddress != null){
                //System.out.println("Cerrada");
                selectionKey.interestOps(SelectionKey.OP_WRITE);
            }
        }
        if (selectionKey.isWritable()) {
            DatagramChannel datagramChannel1 = (DatagramChannel) selectionKey.channel();
            ClientRecord cr = (ClientRecord) selectionKey.attachment();
            cr.buffer.flip();
            int bs = datagramChannel1.send(cr.buffer, cr.clientAddress);
            if (bs != 0) {
                selectionKey.interestOps(SelectionKey.OP_READ);
            }
        }
    }

    private static void gestionarPeticionTCP(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isReadable()) {    // leo contenido
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
            byteBuffer.clear();
            int bytesRead = socketChannel.read(byteBuffer);

            if (new String(byteBuffer.array()).trim().equals(".")){
                selectionKey.cancel();
                socketChannel.close();
            } else if (bytesRead == -1) { // Error de lectura
                selectionKey.cancel();
                socketChannel.close();
                System.out.println("Error de lectura, cerrando");
                return;
            }
            selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }

        if (selectionKey.isWritable()) { // Devuelvo contenido
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
            buffer.flip();
            socketChannel.write(buffer);
            if (!buffer.hasRemaining()) {
                selectionKey.interestOps(SelectionKey.OP_READ);
            }
            buffer.compact();
        }
    }

    private static void nuevaPeticionTCP(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        System.out.println("Nueva conexion TCP");
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, ByteBuffer.allocate(512));
    }

    private static class ClientRecord {
        public ByteBuffer buffer;
        public SocketAddress clientAddress;

        public ClientRecord() {
            this.buffer = ByteBuffer.allocateDirect(512);
        }
    }
}
