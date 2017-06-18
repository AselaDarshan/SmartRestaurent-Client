package com.company;

/**
 * Created by asela on 6/8/17.
 */
public class MQTTCommuncationThread implements Runnable {

    private OrderWindow orderWindow;
    public MQTTCommuncationThread (OrderWindow orderWindow){
        this.orderWindow = orderWindow;
    }
    public void run() {
         try {
             MQTTClient mqttClient = new MQTTClient(orderWindow,"tcp://iot.eclipse.org:1883", "cashier", false, false, null, null);
             mqttClient.setBillPrinter(new BillPrinter());
             mqttClient.subscribe("new_order","print_bill",2);

         } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
