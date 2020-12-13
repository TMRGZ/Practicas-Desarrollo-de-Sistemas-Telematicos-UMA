package Bloque_2.Ejercicio_1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Panel extends JPanel {
    // Botones
    private JButton get = new JButton("GET");
    private JButton put = new JButton("PUT");
    private JButton connect = new JButton("Connect");
    private String[] modos = {"ascii", "binary"};
    private JComboBox<String> modo = new JComboBox<>(modos);
    // Texto
    private JLabel ip = new JLabel("IP");
    private JLabel archivoLocal = new JLabel("Archivo Local");
    private JLabel archivoRemoto = new JLabel("Archivo Remoto");
    private JLabel debug = new JLabel("Simulacion");
    // Introduccion
    private JTextField inIP = new JTextField(25);
    private JTextField inLocal = new JTextField(25);
    private JTextField inRemoto = new JTextField(25);


    public Panel() {
        this.setLayout(new BorderLayout());
        JPanel panelIP = new JPanel();
        JPanel panelLocal = new JPanel();
        JPanel panelRemoto = new JPanel();
        JPanel panelBotones = new JPanel();
        panelIP.setLayout(new FlowLayout());
        panelLocal.setLayout(new FlowLayout());
        panelRemoto.setLayout(new FlowLayout());
        panelBotones.setLayout(new FlowLayout());

        modo.setSelectedIndex(0);
        panelIP.add(ip);
        panelIP.add(inIP);
        panelIP.add(connect);
        panelLocal.add(archivoLocal);
        panelLocal.add(inLocal);
        panelRemoto.add(archivoRemoto);
        panelRemoto.add(inRemoto);
        panelBotones.add(modo);
        panelBotones.add(get);
        panelBotones.add(put);
        panelBotones.add(debug);

        this.add(BorderLayout.NORTH, panelIP);
        this.add(BorderLayout.WEST, panelLocal);
        this.add(BorderLayout.EAST, panelRemoto);
        this.add(BorderLayout.SOUTH, panelBotones);

    }

    public void controlador(ActionListener ctr) {
        get.addActionListener(ctr);
        get.setActionCommand("get");
        put.addActionListener(ctr);
        put.setActionCommand("put");
        connect.addActionListener(ctr);
        connect.setActionCommand("connect");
        modo.addActionListener(ctr);
        modo.setActionCommand("modo");
    }

    public void cambiarDebug(String str) {
        debug.setText(str);
    }

    public void habilitarConnect(boolean b) {
        ip.setEnabled(b);
        inIP.setEnabled(b);
        connect.setEnabled(b);
    }

    public void habiliarConexiones(boolean b) {
        habilitarConnect(b);
        get.setEnabled(b);
        put.setEnabled(b);
        archivoLocal.setEnabled(b);
        archivoRemoto.setEnabled(b);
        inLocal.setEnabled(b);
        inRemoto.setEnabled(b);
    }

    public String getIP() {
        return inIP.getText().trim();
    }

    public String getLocal() {
        return inLocal.getText().trim();
    }

    public String getRemoto() {
        return inRemoto.getText().trim();
    }

    public String getModo(){
        return (String) modo.getSelectedItem();
    }

    public void popUp(String s){
        JOptionPane.showMessageDialog(this, s);
    }
}
