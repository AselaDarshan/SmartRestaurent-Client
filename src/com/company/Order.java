package com.company;

import org.json.JSONException;
import org.json.JSONObject;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by asela on 5/19/17.
 */
public class Order {



    private ArrayList<JPanel> itemPanelList;

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    private ArrayList<Item> itemList;
    private int itemCount;
    private int orderId;
    private String tableId;
    private String waiterUsername;
    public String getWaiterUsername() {
        return waiterUsername;
    }

    public void setWaiterUsername(String waiterUsername) {
        this.waiterUsername = waiterUsername;
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }



    public String getTableId() {
        return tableId;
    }




    public void itemReady(JPanel itemPanel){
        itemCount--;
        if(itemCount==0){
            JButton closeButton = new JButton("Close");
            closeButton.setBackground(new Color(182,40,20));
            closeButton.addActionListener(e -> {
                        JPanel parentPanel = (JPanel) itemPanel.getParent().getParent();
                        itemPanel.getParent().removeAll();
                        parentPanel.revalidate();
                    }

            );
            itemPanel.getParent().setBackground(new Color(52,30,20));
            itemPanel.getParent().add(closeButton);
        }
    }

    public Order(String order){
        JSONObject oderObj = null;

        itemPanelList = new ArrayList<>();
        itemList = new ArrayList<>();

        System.out.println("decoding json");
        try {
            oderObj = new JSONObject(order);
        } catch (JSONException e) {
            System.out.println("Json error in decoding");
//            e.printStackTrace();
        }


        try {
            tableId = oderObj.getString("TABLE");
            waiterUsername = oderObj.getString("WAITER");
            oderObj.remove("TABLE");
            oderObj.remove("WAITER");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Iterator<?> keys = oderObj.keys();
        String key;
        while( keys.hasNext() ) {
            key = (String)keys.next();
            try {
//                if ( oderObj.get(key) instanceof JSONObject ) {
                    JSONObject menuItem = new JSONObject();
                    menuItem = oderObj.getJSONObject(key);

                    System.out.println(key+"---"+oderObj.get(key));
                    ItemPanel itemPanel =  new ItemPanel(tableId,waiterUsername,this,menuItem.getString(Constants.ITEM_ID_KEY));
                    itemPanel.setNameLabelText(key);


                    itemPanel.setQtyLabelText(String.valueOf(menuItem.getInt(Constants.ITEM_QTY_KEY)));

                    itemPanelList.add(itemPanel.getItemPanel());

                    itemList.add(new Item(0,menuItem.getInt(Constants.ITEM_QTY_KEY),key,menuItem.getString(Constants.ITEM_ID_KEY)));


//                }
            } catch (JSONException e) {
                System.out.println("Json error in keys");
                e.printStackTrace();
            }
        }
        itemCount = itemPanelList.size();
        CommuncationBus.putMessage(waiterUsername+"~"+Constants.ORDER_RECEIVED_TOPIC);
      //  new WebServerCommunication().sendOrder(this);

    }
    public ArrayList<JPanel> getItemPanelList() {
        return itemPanelList;
    }
}

