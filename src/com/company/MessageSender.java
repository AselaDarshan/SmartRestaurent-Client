package com.company;

/**
 * Created by asela on 6/8/17.
 */
public class MessageSender extends Thread {

    @Override
    public void run() {
        String message  = null;
        while (true){
            while (message==null){
                message = CommuncationBus.getMessage();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            if(message.equals("connection_lost")){
//                MQTTCommuncationThread r1 = new MQTTCommuncationThread(null);
//                Thread t1 = new Thread(r1);
//                t1.start();
//            }
            System.out.println("Sending to client: "+message);

            try {
                MQTTClient mqttClient = new MQTTClient(null,"tcp://iot.eclipse.org:1883","cashier:pub",true,false,null,null);
                if(message.equals(Constants.ORDER_RECEIVED_TOPIC)){
                    mqttClient.publish(Constants.ORDER_RECEIVED_TOPIC, 0, message.getBytes());
                }
                else {
                    mqttClient.publish(Constants.ORDER_COMPLETED_TOPIC, 0, message.getBytes());
                }

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            message = null;
        }
    }
}