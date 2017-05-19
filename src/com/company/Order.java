package com.company;

import com.company.Item;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by asela on 5/19/17.
 */
public class Order {



    private ArrayList<JPanel> items;

    public String getTableId() {
        return tableId;
    }

    private String tableId;


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
                    Item item =  new Item();
                    item.setNameLabelText(key);
                    item.setQtyLabelText(String.valueOf(oderObj.getInt(key)));

                    items.add(item.getItemPanel());
//                }
            } catch (JSONException e) {
                System.out.println("Json error in keys");
                e.printStackTrace();
            }
        }

    }
    public ArrayList<JPanel> getItems() {
        return items;
    }
}

