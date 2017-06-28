package com.company;

import org.eclipse.paho.client.mqttv3.MqttException;

import static java.awt.SystemColor.text;

/**
 * Created by asela on 6/29/17.
 */
public class HeartbeatHandler extends Thread {

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(1000*60*5);//wait 5 mintiz
                if(!GlobalState.heartbeat){
                    new AndroidLikeToast().showDialog("Connection Error! Please restart the device",AndroidLikeToast.LENGTH_VERY_LONG);

                }
                GlobalState.heartbeat =false;
                MQTTClient mqttClient = new MQTTClient(null,Constants.MQTT_BROKER_URL,Parameters.myid+"heartbeat",false,false,null,null);

                mqttClient.publish(Constants.HEARTBEAT_TOPIC+Parameters.myid,2,(Parameters.myid+"_alive").getBytes());

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (MqttException e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }
    }
}