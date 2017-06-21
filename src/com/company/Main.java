package com.company;

/**
 * Created by asela on 5/18/17.
 */
public class Main {
    public static void main(String[] args) {
        ConfigReader.readConfig();
        new OrderWindow().run();
    }
}
