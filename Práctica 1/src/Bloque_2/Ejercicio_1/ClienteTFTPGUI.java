package Bloque_2.Ejercicio_1;

import javax.swing.*;

public class ClienteTFTPGUI {
    public static void crearGUI(JFrame ventana) {
        Panel panel = new Panel();
        Controlador ctr = new Controlador(panel);
        panel.controlador(ctr);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setContentPane(panel);
        ventana.pack();
        ventana.setVisible(true);
    }



    public static void main(String[] args) {
        final JFrame ventana = new JFrame("Cliente TFTP");
        SwingUtilities.invokeLater(() -> crearGUI(ventana));
    }
}
