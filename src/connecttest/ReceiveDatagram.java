/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connecttest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class ReceiveDatagram implements Runnable {

    private InitUI ui;
    private int count;
    private int powOffCount;
    private DatagramSocket socket;
    private DatagramPacket reveivePacket;
    private Thread receiveThread;
    private SimpleDateFormat df;
    private Log log;
    private boolean isConnectDown;
    private boolean isReviveTimeOut;
     private   boolean handShaked = false;
    public ReceiveDatagram(InitUI ui2, DatagramSocket socket2, DatagramPacket reveivePacket2) {
        try {
            this.ui = ui2;
            this.socket = socket2;
            socket.setSoTimeout(100);
            this.reveivePacket = reveivePacket2;
            count = -1;
            powOffCount = -1;
            isConnectDown = false;
            isReviveTimeOut = false;
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            log = Log.getLoger();
        } catch (SocketException ex) {
//            Logger.getLogger(ReceiveDatagram.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void startThread() {
        receiveThread = new Thread(this, "receiveThread");
        receiveThread.start();
    }

    @Override
    public void run() {

        while (true) {
            try {

                socket.receive(reveivePacket);
                count = 0;
                powOffCount = 0;

                if (!handShaked) {   
                    handleHandShaked();
                    
                } else {
                    if (isConnectDown) {
                        powOfRevive();
                    }
                }
                //         Thread.sleep(100);
//                TimeUnit.MILLISECONDS.sleep(100);
            } catch (IOException ex) {
                //超时处理
//                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                if (handShaked) {
                    count++;
                    powOffCount++;
                    if (count == 3) {
                        count = 0;
                        if (isConnectDown == false) {
                            handle3TimesUnconnect();
                        }
                        isConnectDown = true;
                    }
                    if (powOffCount >= (Integer.parseInt(ui.getTimeText()) * 10 + 3)) {
                        powOffCount = 0;
                        if (isReviveTimeOut == false) {
                            handlePowOffTimeOut();
                           
                        }
                    }
                }
            }
        }
    }

    private void powOfRevive() {
        log.loger.error(":连接恢复\n");
        ui.getTextArea().append(df.format(new Date()) + ":连接恢复\n");
        isConnectDown = false;
        isReviveTimeOut = false;
    }

    private void handleHandShaked() {
        log.loger.error(":收到Ping包\n");
        ui.getTextArea().append(df.format(new Date()) + ":收到Ping包\n");
        handShaked = true;
    }

    private void handle3TimesUnconnect() {
        log.loger.error(":连接断开\n");
        ui.getTextArea().append(df.format(new Date()) + ":连接断开\n");
    }

    private void handlePowOffTimeOut() {
        log.loger.fatal(":**************掉电恢复超时************\n");
        ui.getTextArea().append(df.format(new Date()) + ":掉电恢复超时\n");
         isReviveTimeOut = true;
    }
}
