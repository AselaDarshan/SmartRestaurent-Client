package com.company;

import com.company.Item;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by asela on 5/19/17.
 */
public class Order {



    private ArrayList<JPanel> items;
    private int itemCount;

    public String getTableId() {
        return tableId;
    }

    private String tableId;


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

        items = new ArrayList<>();

        System.out.println("decoding json");
        try {
            oderObj = new JSONObject(order);
        } catch (JSONException e) {
            System.out.println("Json error in decoding");
//            e.printStackTrace();
        }

        Iterator<?> keys = oderObj.keys();
        String key = (String)keys.next();
        try {
            tableId = oderObj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        while( keys.hasNext() ) {
            key = (String)keys.next();
            try {
//                if ( oderObj.get(key) instanceof JSONObject ) {
                    System.out.println(key+"---"+oderObj.get(key));
                    Item item =  new Item(tableId,this);
                    item.setNameLabelText(key);
                    item.setQtyLabelText(String.valueOf(oderObj.getInt(key)));

                    items.add(item.getItemPanel());
//                }
            } catch (JSONException e) {
                System.out.println("Json error in keys");
                e.printStackTrace();
            }
        }
        itemCount = items.size();
        CommuncationBus.putMessage("order_success");

    }
    public ArrayList<JPanel> getItems() {
        return items;
    }
}

