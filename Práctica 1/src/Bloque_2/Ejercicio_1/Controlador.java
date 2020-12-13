package Bloque_2.Ejercicio_1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

public class Controlador implements ActionListener {
    private Panel panel;

    public Controlador(Panel panel) {
        this.panel = panel;
        panel.cambiarDebug(TFTPacket.simulacion ? "Simulacion activada" : "Simulacion desactivada");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        String ip = panel.getIP().trim();
        String fnameGET = panel.getRemoto().trim();
        String comandoGET = "get ";
        String fnamePUT = panel.getLocal().trim();
        String comandoPUT = "put ";

        switch (cmd) {
            case "get":
                if (!ClienteTFTP.conectado) comandoGET += ip;
                comandoGET += " " + fnameGET;

                if (!(fnameGET.trim().length() == 0)) {
                    panel.habiliarConexiones(false);
                    ClienteTFTP.get(comandoGET.split("\\s+"));
                } else {
                    panel.popUp("Campo archivo remoto vacio");
                }
                panel.habiliarConexiones(true);
                panel.popUp("Transferencia finalizada, mire los resultados en la consola");
                break;
            case "put":
                if (!ClienteTFTP.conectado) comandoPUT += ip;
                comandoPUT += " " + fnamePUT;

                if (!(fnamePUT.trim().length() == 0)) {
                    panel.habiliarConexiones(false);
                    ClienteTFTP.put(comandoPUT.split("\\s+"));
                } else {
                    panel.popUp("Campo archivo local vacio");
                }
                panel.habiliarConexiones(true);
                panel.popUp("Transferencia finalizada, mire los resultados en la consola");
                break;
            case "connect":
                if (!(panel.getIP().trim().length() == 0)) {
                    ClienteTFTP.connect(ip);
                } else {
                    panel.popUp("Campo IP vacio");
                }
                if (ClienteTFTP.conectado) panel.habilitarConnect(false);
                break;
            case "modo":
                ClienteTFTP.mode(panel.getModo());
                break;
        }
    }
}
