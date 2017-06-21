package com.company;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static javax.swing.JOptionPane.WARNING_MESSAGE;

/**
 * Created by asela on 6/18/17.
 */
public class ConfigReader {

    public static void readConfig() {
        try {
            File file = new File("/home/config.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuffer.append(line);
//                stringBuffer.append("\n");
//                Parameters.myid = bufferedReader.readLine();
//            }
            Parameters.myid = bufferedReader.readLine().split(":")[1];
            Parameters.subTopic1 = bufferedReader.readLine().split(":")[1];
            Parameters.subTopic2 = bufferedReader.readLine().split(":")[1];
            Parameters.mylocationId = bufferedReader.readLine().split(":")[1];
            Parameters.numberOfMQTTRetries = Integer.parseInt(bufferedReader.readLine().split(":")[1]);
            fileReader.close();
            System.out.println("Contents of file:");
            System.out.println(Parameters.myid+","+Parameters.subTopic1+","+Parameters.subTopic2);
        } catch (IOException e) {
            JOptionPane msg= new JOptionPane( "config.txt file is missing. Checked in /home directory", WARNING_MESSAGE);
            JDialog dialog = msg.createDialog("Error!");
            dialog.setAlwaysOnTop(true); // to show top of all other application
            dialog.setVisible(true); // to visible the dialog
            e.printStackTrace();
            System.exit(0);

        }
    }
}
