package com.company;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;

/**
 * Created by asela on 6/8/17.
 */
public class MQTTCommuncationThread implements Runnable {

    private OrderWindow orderWindow;
    private JOptionPane msg;
    private int retryLimit;
    public MQTTCommuncationThread (OrderWindow orderWindow){
        this.orderWindow = orderWindow;
    }
    public void run() {
        retryLimit = Parameters.numberOfMQTTRetries;
        int retryCount=0;
        boolean connected=false;
        new AndroidLikeToast().showDialog("Connecting..",AndroidLikeToast.LENGTH_LONG);
        while(!connected) {
            try {

                MQTTClient mqttClient = new MQTTClient(orderWindow, Constants.MQTT_BROKER_URL, Parameters.myid + "sub", false, false, null, null);
                mqttClient.setBillPrinter(new BillPrinter());
                mqttClient.subscribe(Parameters.subTopic1, Parameters.subTopic2, 2);
                connected = true;
            } catch (Throwable throwable) {

                new AndroidLikeToast().showDialog("Connection failed! Retrying..",AndroidLikeToast.LENGTH_SHORT);
//                msg = new JOptionPane("Connection failed! Retrying..",  WARNING_MESSAGE);
//                dialog = msg.createDialog("Connection failed");
//                dialog.setAlwaysOnTop(true); // to show top of all other application
//                dialog.setVisible(true); // to visible the dialog
                throwable.printStackTrace();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                dialog.setVisible(false);
                retryCount++;
            }
            if(retryCount>retryLimit){
                new AndroidLikeToast().showDialog("Can't connect!\n Please check the internet connection and restart the program",AndroidLikeToast.LENGTH_VERY_LONG);
//                msg = new JOptionPane("Can't connect to the messaging server. Please test the internet connection and restart the program", WARNING_MESSAGE);
//                dialog = msg.createDialog("Can't connect");
//                dialog.setAlwaysOnTop(true); // to show top of all other application
//                dialog.setVisible(true); // to visible the dialog
                return;
            }
        }

//
    }


}
