package com.company;

import org.json.JSONException;
import org.json.JSONObject;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.*;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by asela on 6/18/17.
 */
public class BillPrinter {

    private ArrayList<String> itemIdList;
    private int orderId;

    public void printBill(String orderObjectString)  {
        itemIdList = new ArrayList<>();
        System.out.println("Creating bill:" +orderObjectString);
        JSONObject oderObj = null;
        String billString = "<p>_____________________________</p>\n" +
                "<p style=\"text-align:center;font-family:new century schoolbook;font-size: 9pt; font-weight:bold;\">Sliver Ring Village Hotel\n" +
                "</p>" +
                "<p style=\"text-align:center;font-family:new century schoolbook;font-size: 7pt;\">No:52/1, Kidagammulla, Gampaha\n" +
                "</p>" +
                "<p style=\"text-align:center;font-family:new century schoolbook;font-size: 7pt; \">Tel : 033-2224534" +
                "<br>_____________________________</p>";
        try {
            oderObj = new JSONObject(orderObjectString);
            String waiter = oderObj.getString("WAITER");
            orderId = oderObj.getInt("ORDER_ID");
            oderObj.remove("WAITER");
            oderObj.remove("ORDER_ID");
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
            Date date = new Date();
            billString+="<p style=\"font-family:new century schoolbook;font-size: 7pt;\" >"+dateFormat.format(date) +"" +
                    "<br> Waiter: "+waiter+"&nbsp&nbsp&nbsp Order ID: "+orderId+"<br> _____________________________</p>" +
                    "<table >";

            Iterator<?> keys = oderObj.keys();
            String key;
            System.out.println("item count: "+oderObj.keySet().size());
            double total=0;
            double subTotal=0;
            DecimalFormat df = new DecimalFormat("#.00");
            while( keys.hasNext() ) {
                key = (String) keys.next();
                try {
//                if ( oderObj.get(key) instanceof JSONObject ) {
                    JSONObject menuItem;
                    menuItem = oderObj.getJSONObject(key);
                    subTotal = menuItem.getDouble(Constants.ITEM_PRICE_KEY)*menuItem.getInt(Constants.ITEM_QTY_KEY);

                    itemIdList.add(menuItem.getString(Constants.ITEM_ID_KEY));

                    String subTotalFormated = df.format(subTotal);

                    billString+="<tr style=\"font-size: 8pt\"><td> "+menuItem.getString(Constants.ITEM_QTY_KEY)+"x</td><td >"+menuItem.getString(Constants.ITEM_NAME_KEY)+"</td><td style=\"text-align:right\">"+subTotalFormated +"</td></tr>";
                    total+=subTotal;
//                }
                } catch (JSONException e) {
                    System.out.println("Json error in keys");
                    e.printStackTrace();
                }
            }
            String totalFormated = df.format(total);
            billString+="<tr><td></td><td></td><td>-----------</td></tr>";
            billString+="<tr><td></td><td><p style=\"font-size: 8pt;font-weight:bold;\">Total:</td><td style=\"font-size: 8pt;text-align:right\"> "+totalFormated+"</p></td></tr></table>" +
                    "<p>_____________________________</p>"+
                    "<p style=\"text-align:center;font-family:new century schoolbook;font-size: 8pt; font-weight:bold;\">Thank you!</p>";
        } catch (JSONException e) {
            System.out.println("Json error in decoding");
//            e.printStackTrace();
        }

        System.out.println("bill prepared: "+billString);
//        PrinterService printerService = new PrinterService();
//
//        System.out.println(printerService.getPrinters());

        //print some stuff
        // printerService.printString("wd", "Silver Ring Village Hotel");

//        // cut that paper!
//        byte[] cutP = new byte[] { 0x1d, 'V', 1 };
//
//        printerService.printBytes("wd", cutP);

        System.out.println("printing bill started");
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        URL urlToPage = null;

        PrintWriter out = null;
        try {
            out = new PrintWriter("print.html");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        out.println(billString);
        out.close();
        try {
            urlToPage = new File("print.html").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            editorPane.setPage(urlToPage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JFrame frame= new JFrame("Bill");

        JPanel panel =  new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel();
        panel.add(editorPane);

        JButton printButton =  new JButton("Print");
        printButton.addActionListener(e -> {
                   print(editorPane);
                    frame.dispose();
                }

        );
        JButton closeButton =  new JButton("Close");
        closeButton.addActionListener(e -> {
                    frame.dispose();                }

        );
        buttonPanel.add(closeButton);
        buttonPanel.add(printButton);

        panel.add(buttonPanel);

        frame.setContentPane(panel);
        frame.setSize(200,400);
        frame.setVisible(true);




    }
    private static PrintService findPrintService(String printerName,
                                                 PrintService[] services) {
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                return service;
            }
        }

        return null;
    }

    private void print(JEditorPane editorPane){
        //printer setup
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

        aset.add(OrientationRequested.PORTRAIT);
        aset.add(MediaSizeName.INVOICE);
        aset.add(new MediaPrintableArea(0f, 0f,58f,210f , MediaPrintableArea.MM));

        DocFlavor flavor = DocFlavor.BYTE_ARRAY.TEXT_HTML_UTF_8;

        // PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();


        PrintService printService[] = PrintServiceLookup.lookupPrintServices(
                flavor, aset);

        PrintService service = findPrintService("XP-58", printService);



        try {
            boolean printed = editorPane.print(null, null, true, service, aset, true);
            if(printed) {
                System.out.println("Print success");
                new AndroidLikeToast().showDialog("Print job has been submitted to the printer",AndroidLikeToast.LENGTH_SHORT);
                String response = null;
                for(String itemId:itemIdList) {
                    response = new WebServerCommunication().changeStateOfOrderItem(itemId, Constants.ITEM_STATE_COMPLETED);
                }
                if(response!=null){
                    new WebServerCommunication().changeStateOfOrder(orderId,Constants.ORDER_STATUS_COMPLETED_ID);
                }
                else
                    new AndroidLikeToast().showDialog("Database Update Failed! Network error",AndroidLikeToast.LENGTH_SHORT);


            }
            else{
                System.out.println("Print canceled");
            }
            return;
        }
        catch (PrinterException e) {
            new AndroidLikeToast().showDialog("Printing failed!",AndroidLikeToast.LENGTH_SHORT);

            e.printStackTrace();
        }
        catch (java.lang.NullPointerException e){
            new AndroidLikeToast().showDialog("Database Update Failed! Network error",AndroidLikeToast.LENGTH_SHORT);

        }
        System.out.println("Print failed!");
    }

}
