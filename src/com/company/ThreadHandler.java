package com.company;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.*;
import java.net.*;
import java.util.Vector;

class ThreadHandler extends Thread {
    Socket newsock;
    PrintWriter outp;
    int n;

    static final int MAXQUEUE = 5;
    private Vector messages = new Vector();

    ThreadHandler(Socket s, int v) {
        newsock = s;
        n = v;
    }


    public void run() {
        try {

            outp = new PrintWriter(newsock.getOutputStream(), true);
            SendHandler sendHandler = new SendHandler();
            sendHandler.start();


            BufferedReader inp = new BufferedReader(new InputStreamReader(
                    newsock.getInputStream()));

            outp.println("Hello :: enter QUIT to exit \n");
            boolean more_data = true;
            String line;

            while (more_data) {
                line = inp.readLine();
                putMessage(line);
//                System.out.println("Message '" + line + "' echoed back to client.");
                if (line == null) {
                    System.out.println("line = null");
                    more_data = false;
                } else {
                  //  outp.println("From server: " + line + ". \n");
                    if (line.trim().equals("QUIT"))
                        more_data = false;
                }
            }

            newsock.close();
            System.out.println("Disconnected from client number: " + n);
        } catch (Exception e) {
            System.out.println("IO error inside thread " + e);
        }

    }

    private synchronized void putMessage(String m) throws InterruptedException {

        while (messages.size() == MAXQUEUE)
            wait();
        messages.addElement(m);
        notify();
    }

    // Called by Consumer
    public synchronized String getMessage() throws InterruptedException {
        notify();
        while (messages.size() == 0)
            wait();
        String message = (String) messages.firstElement();
        messages.removeElement(message);
        return message;
    }

    public class SendHandler extends Thread {

        @Override
        public void run() {
            String message  = null;
            while (true){
                while (message==null){
                    message = CommuncationBus.getMessage();
                }
                System.out.println("Sending to client: "+message);

                outp.println(message);

                if(outp.checkError()){
                    System.out.println("Connection status: " + !outp.checkError());
                   // CommuncationBus.putMessage(message);
                }
//                while (outp.checkError()) {
//                    System.out.println("Connection status: " + !outp.checkError());
//                    try {
//                        outp.close();
//                        outp = new PrintWriter(newsock.getOutputStream(), true);
//                        outp.println(message);
//                    } catch (IOException e) {
//                        System.out.println(e);
//                        e.printStackTrace();
//                    }
//
//                }
                message = null;
            }
        }
    }
}