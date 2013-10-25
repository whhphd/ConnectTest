/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connecttest;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 *
 * @author HD
 */
public class Main {

    private static DatagramSocket socket;
    private static DatagramPacket reveivePacket;
    private static DatagramPacket sendPacket;
    private static InitUI ui;
    private static ReceiveDatagram receiveThread;
    private static Thread t;

    public static void main(String[] args) {
        // TODO code application logic here
        ui = new InitUI();
        ui.setVisible(true);
        initSocket();
        receiveThread = new ReceiveDatagram(ui, socket, reveivePacket);
//       t = new Thread(new ReceiveDatagram(ui, socket, reveivePacket));
//       t.start();
        receiveThread.startThread();
    }

    private static void initSocket() {
        try {
            socket = new DatagramSocket(4455);

            byte[] receiveBuf = new byte[1];
            reveivePacket = new DatagramPacket(receiveBuf, receiveBuf.length);
        } catch (SocketException ex) {
//            Logger.getLogger(InitUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
