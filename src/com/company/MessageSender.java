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
                MQTTClient mqttClient = new MQTTClient(null,"tcp://iot.eclipse.org:1883","cashier:pub",false,false,null,null);
                String waiter = message.split("~")[0];
                String text = message.split("~")[1];
                if(text.equals(Constants.ORDER_RECEIVED_TOPIC)){
                    mqttClient.publish(Constants.ORDER_RECEIVED_TOPIC+waiter, 2,text.getBytes());
                }
                else {
                    //sending order completed message

                    mqttClient.publish(Constants.ORDER_COMPLETED_TOPIC+waiter, 2, text.getBytes());
                }

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            message = null;
        }
    }
}