package com.company;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sun.misc.IOUtils;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by asela on 6/5/17.
 */
public class WebServerCommunication {
    public String changeStateOfOrder(int orderId, int state) {
        JSONObject dataObject = new JSONObject();
        try {
            dataObject.put("status_id", state);
            return sendJOSNPUT(Constants.API_BASE_URL + Constants.API_ORDERS + "/" + orderId, dataObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }


    public String changeStateOfOrderItem(String itemIdString, String state){

        JSONObject responses  = null;
        try {
            responses = new JSONObject( sendGetRequest("http://resmng.enetlk.com/api/api.php/forsj3vth_order_menus/?filter=comment,eq,"+itemIdString));


        JSONArray menuArray = responses.getJSONObject(Constants.API_ORDER_MENUS).getJSONArray(Constants.RECORDS_KEY);
        JSONArray item = menuArray.getJSONArray(0);
        int itemIdDB = item.getInt(0);


        JSONObject dataObject = new JSONObject();
        try {
            dataObject.put("option_values", state);
            return sendJOSNPUT( "http://resmng.enetlk.com/api/api.php/forsj3vth_order_menus/" + itemIdDB, dataObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void retreiveOrders(OrderWindow orderWindow){
//        try {
            HashMap<String,JSONObject> orderMap= new HashMap<>();
            String response = sendGetRequest(Constants.API_BASE_URL+Constants.API_ORDER_MENUS+"?filter[]=comment,ew,"+Parameters.mylocationId+"&filter[]=option_values,eq,"+Constants.ITEM_STATE_SENT+"&satisfy=all");
        try {
            JSONArray objectArray = new JSONObject(response).getJSONObject(Constants.API_ORDER_MENUS).getJSONArray("records");
            for(int i=0;i<objectArray.length();i++){
                JSONArray itemObjectArray = objectArray.getJSONArray(i);
                System.out.println(itemObjectArray.getString(3));

                String itemDetails = objectArray.getJSONArray(i).getString(8);
                String tableId= itemDetails.split("\\.")[1];

                if(orderMap.containsKey(tableId)){
                    JSONObject itemObj = new JSONObject();
                    String itemName = objectArray.getJSONArray(i).getString(3);

                    itemObj.put(Constants.ITEM_QTY_KEY,objectArray.getJSONArray(i).getInt(4));
                    itemObj.put(Constants.ITEM_ID_KEY,itemDetails);

                    orderMap.get(tableId).put(itemName,itemObj);
                }
                else{

                    JSONObject oderObj = new JSONObject();
                    oderObj.put("TABLE",tableId);

                    String waiterId = itemDetails.split("\\.")[0];
                    //get waiter name
                    JSONObject waiterObject = new JSONObject(sendGetRequest(Constants.API_BASE_URL+Constants.API_STAFF+"?filter=staff_id,eq,"+waiterId));
                    JSONArray waiter = waiterObject.getJSONObject(Constants.API_STAFF).getJSONArray("records").getJSONArray(0);

                    oderObj.put("WAITER",(waiter.getString(1)));//itemDetails.split("\\.")[0]);

                    JSONObject itemObj = new JSONObject();
                    String itemName = objectArray.getJSONArray(i).getString(3);

                    itemObj.put(Constants.ITEM_QTY_KEY,objectArray.getJSONArray(i).getInt(4));
                    itemObj.put(Constants.ITEM_ID_KEY,itemDetails);

                    oderObj.put(itemName,itemObj);

                    orderMap.put(tableId,oderObj);
                }

            }



            for(String key:orderMap.keySet())
                orderWindow.addOrder(orderMap.get(key).toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (java.lang.NullPointerException ex) {
            ex.printStackTrace();
            new AndroidLikeToast().showDialog("Order Retreiving Faild!",AndroidLikeToast.LENGTH_SHORT);
        }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

    }

    public void sendOrder(Order order){
        ArrayList<Item> itemList = order.getItemList();
        String name;
        Double amount = 0.0;
        Double total=0.0;
        int itemCount = itemList.size();
        int menuId=0;
        int orderId=0;

        for(Item item:itemList) {
            name = item.getName();
            System.out.println("Get details for item "+name);

//                sendGetRequest("http://resmng.enetlk.com/api/api.php/forsj3vth_menus?filter=name,eq,");

            try {
                System.out.println("http://resmng.enetlk.com/api/api.php/forsj3vth_menus?filter=menu_name,eq," + URLEncoder.encode(name,"UTF-8"));

                String response = sendGetRequest("http://resmng.enetlk.com/api/api.php/forsj3vth_menus?filter=menu_name,eq," + URLEncoder.encode(name,"UTF-8"));
                amount = Double.parseDouble(new JSONObject(response).getJSONObject("forsj3vth_menus").getJSONArray("records").getJSONArray(0).getString(3));
                menuId = new JSONObject(response).getJSONObject("forsj3vth_menus").getJSONArray("records").getJSONArray(0).getInt(0);

                item.setAmount(amount);
                item.setMenuId(menuId);
                total += amount*item.getQty();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        JSONObject orderObject = new JSONObject();
        try {
            Date date = new Date();
            orderObject.put("total_items", itemCount);
            orderObject.put("date_added", new java.sql.Timestamp(date.getTime()));
            orderObject.put("order_time", new java.sql.Time(date.getTime()));
            orderObject.put("name","testitem");
            orderObject.put("order_date", new java.sql.Timestamp(date.getTime()));
            orderObject.put("order_total", total);
            orderObject.put("first_name", "");
            orderObject.put("last_name", "");
            orderObject.put("assignee_id", 11);
            orderObject.put("customer_id", 11);
            orderObject.put("status_id", 11);

            orderId = Integer.parseInt(SendJOSNPOST("http://resmng.enetlk.com/api/api.php/forsj3vth_orders",orderObject));
            order.setOrderId(orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(Item item:itemList) {
            JSONObject orderItem = new JSONObject();
            try {

                orderItem.put("order_id", orderId);
                orderItem.put("menu_id", item.getMenuId());
                orderItem.put("name", item.getName());
                orderItem.put("quantity", item.getQty());
                orderItem.put("price", item.getAmount());
                orderItem.put("subtotal", item.getAmount()*item.getQty());
                SendJOSNPOST("http://resmng.enetlk.com/api/api.php/forsj3vth_order_menus", orderItem);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        JSONObject orderTotalObject = new JSONObject();
//        try {
//            orderTotalObject.put("order_id", 2001);
//            orderTotalObject.put("date_added", "2017-05-08 09:43:50");
//            orderTotalObject.put("order_time", "10:00:00");
//            orderTotalObject.put("value",450.00);
//            orderTotalObject.put("title", "Test Order");
//            orderTotalObject.put("code", "234df");
//
//            SendJOSNPOST("http://resmng.enetlk.com/api/api.php/forsj3vth_order_totals",orderTotalObject);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    public String SendJOSNPOST(String url,JSONObject json) {


        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(json.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            System.out.println("Http Response for POST: "+responseString);
            return responseString;


// handle response here...
        } catch (Exception ex) {
            // handle exception here
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String sendJOSNPUT(String url,JSONObject json) {


        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        System.out.println("Sending put request: "+url);
        try {
            HttpPut request = new HttpPut(url);
            StringEntity params = new StringEntity(json.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            System.out.println("Http Response for PUT: "+responseString);
            return responseString;


// handle response here...
        } catch (Exception ex) {
            // handle exception here
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String sendGetRequest(String url){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        System.out.println("Send Http GET: "+url);
        try {
            HttpGet request = new HttpGet(url);

            HttpResponse response = httpClient.execute(request);

            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            System.out.println("Http Response for GET: "+responseString);
            return responseString;


// handle response here...
        } catch (Exception ex) {
            System.out.println("HTTP GET error: "+ex);
            // handle exception here
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
