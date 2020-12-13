// Miguel Angel Ruiz Gomez

package Bloque_3.Ejercicio_3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.logging.Logger;

public class ChatServer {

    private static final int PORT = 1234;
    private static Set<SocketChannel> conectados = new HashSet<>();

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("ServidorChatTCPSelector");
        try {
            // Abrir canal pasivo
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            // Lectura no bloqueante
            Selector selector = Selector.open();
            serverSocketChannel.configureBlocking(false);
            SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("Servidor iniciado " + serverSocketChannel.getLocalAddress());

            while (true) {
                SelectionKey key = null;
                try {
                    // Llamada bloqueante
                    selector.select();
                    // Iterar canales activos
                    for (Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext(); ) {
                        key = i.next();
                        i.remove();

                        if (key == selectionKey && key.isAcceptable()) { // Llega petición de conexión
                            nuevaPeticionTCP(key, selector, logger);
                        } else {
                            gestionarPeticionTCP(key, logger);
                        }

                        if (key.isValid() && key.isWritable()) {
                            procesarPeticionTCP(logger, key);
                        }
                    }
                } catch (IOException e) {
                    logger.info("Desconexion forzosa de un cliente");
                    assert key != null;
                    String salida = ((SocketChannel) key.channel()).getRemoteAddress() + " ha salido del chat";
                    ByteBuffer bufferSalida = ByteBuffer.allocate(((ByteBuffer) key.attachment()).capacity());
                    conectados.remove(key.channel());

                    key.cancel();
                    bufferSalida.put(salida.getBytes());
                    bufferSalida.putChar('\n');
                    bufferSalida.flip();

                    for (SocketChannel cliente : conectados) {
                        if (!selectionKey.channel().equals(cliente)) {
                            cliente.write(bufferSalida);
                            bufferSalida.flip();
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.info("Fallo al iniciar");
        }
    }

    private static void nuevaPeticionTCP(SelectionKey selectionKey, Selector selector, Logger logger) throws IOException {
        SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
        logger.info("Nueva conexion TCP con " + socketChannel.getRemoteAddress());
        conectados.add(socketChannel);

        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(512));
        //System.out.println(x.isWritable());
    }

    private static void gestionarPeticionTCP(SelectionKey selectionKey, Logger logger) throws IOException {
        if (!selectionKey.isReadable()) return;
        //System.out.println("Gestiono peticion");
        SocketChannel client = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
        byteBuffer.clear();

        client.read(byteBuffer);
        //System.out.println(new String(byteBuffer.array()).trim());
        int bytesread = client.read(byteBuffer);

        if (bytesread == -1) {// Se ha cerrado la conexión?
            String salida = client.getRemoteAddress() + " ha salido del chat";
            ByteBuffer bufferSalida = ByteBuffer.allocate(byteBuffer.capacity());
            bufferSalida.put(salida.getBytes());
            bufferSalida.putChar('\n');
            bufferSalida.flip();
            for (SocketChannel cliente : conectados) {
                if (!selectionKey.channel().equals(cliente)) {
                    cliente.write(bufferSalida);
                    bufferSalida.flip();
                }
            }

            selectionKey.cancel();
            logger.info("Conexion terminada con " + client.getRemoteAddress());

            conectados.remove(client);
            client.close();
            return;
        }
        selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    private static void procesarPeticionTCP(Logger logger, SelectionKey key) throws IOException {
        // Coger datos a leer
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.flip();
        String salida = ((SocketChannel) key.channel()).getRemoteAddress() + ": " + new String(buffer.array()).trim();
        ByteBuffer bufferSalida = ByteBuffer.allocate(buffer.capacity());
        bufferSalida.put(salida.getBytes());
        bufferSalida.putChar('\n');
        bufferSalida.flip();
        logger.info("Enviando mensaje a " + (conectados.size() - 1) + " clientes");

        for (SocketChannel cliente : conectados) {
            if (!key.channel().equals(cliente)) {
                cliente.write(bufferSalida);
                bufferSalida.flip();
            }
        }
        key.interestOps(SelectionKey.OP_READ);
        buffer.compact();
    }


}
