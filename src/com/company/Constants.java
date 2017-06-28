package com.company;

/**
 * Created by asela on 6/8/17.
 */
public class Constants {
    public static final String ORDER_RECEIVED_TOPIC = "silver_ring_order_received";
    public static final String ORDER_COMPLETED_TOPIC = "silver_ring_order_completed";
    public static final String HEARTBEAT_TOPIC = "sliverring_device_heartbeat";

    public static String ITEM_QTY_KEY = "item_qty";
    public static String ITEM_ID_KEY = "item_id";

    public static String ITEM_NAME_KEY = "item_name_key";
    public static String ITEM_PRICE_KEY = "item_price_key";
//    public static String CLIENT_ID_SUB = "cashier_sub";
//    public static String CLIENT_ID_PUB = "cashier_pub";
    public static String MQTT_BROKER_URL = "tcp://development.enetlk.com:1884";

    public static final String API_MENU = "forsj3vth_menus";
    public static final String API_CATEGORIES = "forsj3vth_categories";
    public static final String API_STAFF = "forsj3vth_staffs";
    public static final String API_ORDER_MENUS = "forsj3vth_order_menus";
    public static final String API_ORDERS = "forsj3vth_orders";

    public static final String RECORDS_KEY = "records";

    public static final String SERVER_IP = "http://resmng.enetlk.com";
    public static final String API_BASE_URL = SERVER_IP+"/api/api.php/";

    public static String ITEM_STATE_SENT = "SENT";
    public static String ITEM_STATE_PREPARED = "PREPARED";
    public static String ITEM_STATE_READY = "READY";
    public static String ITEM_STATE_COMPLETED = "COMPLETED";

    public static int ORDER_STATUS_COMPLETED_ID = 15;
    public static int ORDER_STATUS_PREPARING_ID = 13;
}
