package com.company;

import javafx.print.Printer;
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
import java.util.Iterator;

/**
 * Created by asela on 6/18/17.
 */
public class BillPrinter {
    public void printBill(String orderObjectString)  {
        System.out.println("Creating bill:" +orderObjectString);
        JSONObject oderObj = null;
        String billString = "<p style=\"font-size: 7pt; font-weight:bold;\">Sliver Ring Village Hotel</p>";
        try {
            oderObj = new JSONObject(orderObjectString);
            Iterator<?> keys = oderObj.keys();
            String key;
            System.out.println("item count: "+oderObj.keySet().size());
            double total=0;
            double subTotal=0;
            while( keys.hasNext() ) {
                key = (String) keys.next();
                try {
//                if ( oderObj.get(key) instanceof JSONObject ) {
                    JSONObject menuItem;
                    menuItem = oderObj.getJSONObject(key);
                    subTotal = menuItem.getDouble(Constants.ITEM_PRICE_KEY)*menuItem.getInt(Constants.ITEM_QTY_KEY);
                    billString+="<p style=\"font-size: 8pt\">"+menuItem.getString(Constants.ITEM_NAME_KEY)+" "+menuItem.getDouble(Constants.ITEM_PRICE_KEY)+" * "+menuItem.getString(Constants.ITEM_QTY_KEY)+"="+subTotal+"</p>";
                    total+=subTotal;
//                }
                } catch (JSONException e) {
                    System.out.println("Json error in keys");
                    e.printStackTrace();
                }
            }
            billString+="<p style=\"font-size: 8pt;font-weight:bold;\">Total: "+total+"</p>";
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
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

        aset.add(OrientationRequested.PORTRAIT);
        aset.add(MediaSizeName.INVOICE);
        aset.add(new MediaPrintableArea(0f, 0f,58f,210f , MediaPrintableArea.MM));

        DocFlavor flavor = DocFlavor.BYTE_ARRAY.TEXT_HTML_UTF_8;

        // PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();


        PrintService printService[] = PrintServiceLookup.lookupPrintServices(
                flavor, aset);

        PrintService service = findPrintService("wd", printService);



        try {
            editorPane.print(null, null, true, service, aset, true);
            System.out.println("Print success");
            return;
        }
        catch (PrinterException e) {
            e.printStackTrace();
        }
        System.out.println("Print failed!");
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

    }
