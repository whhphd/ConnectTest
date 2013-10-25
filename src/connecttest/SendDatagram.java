/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connecttest;

import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author Admin
 */
public class SendDatagram implements Runnable {

    boolean isStop;
    boolean firstStart;
    private static DatagramSocket socket;
    private static DatagramPacket sendPacket;
    private static Thread sendThread;
    private static InitUI ui;
    private String ip;
    private static SimpleDateFormat df;

    private static Log log;
    public SendDatagram(InitUI ui2) {
        try {
            socket = new DatagramSocket();

            sendThread = new Thread(this, "sendThread");
            ui = ui2;
            firstStart = true;
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            log=Log.getLoger();  
        } catch (SocketException ex) {
//            Logger.getLogger(SendDatagram.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void start() {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            byte[] sendBuf = {1};
            sendPacket = new DatagramPacket(sendBuf, sendBuf.length, ipAddress, 4455);
            if (firstStart) {


                isStop = false;
                sendThread.start();
                firstStart = false;
            } else {
                isStop = false;
            }
        } catch (UnknownHostException ex) {
//            Logger.getLogger(SendDatagram.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void stop() {
        isStop = true;
    }

    @Override
    public void run() {
        ui.getTextArea().append( df.format(new Date()) +":开始发送Ping包\n");
             log.loger.error(df.format(new Date()) + ":开始发送Ping包\n");  
        try {
            while (true) {
                if (!isStop) {
                    socket.send(sendPacket);
                }
                sendThread.sleep(100);

            }
        } catch (InterruptedException ex) {
//            Logger.getLogger(SendDatagram.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
//            Logger.getLogger(SendDatagram.class.getName()).log(Level.SEVERE, null, ex);
        }




    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }
}
