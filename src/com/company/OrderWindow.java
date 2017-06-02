package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by asela on 5/18/17.
 */
public class OrderWindow {


    private JPanel panel;
    private JLabel newOrderIndicator;

    private JFrame frame;

    private int count = 0;

    public OrderWindow(){
        //panel =  new JPanel();
//        newOrderIndicator =  new JLabel();
//        newOrderIndicator.setText("Ready");
//
        //panel.setBackground(Color.lightGray);
        final BufferedImage image;
        BufferedImage im;
        try {
            try {
                im = ImageIO.read(getClass().getResource("/background.jpg"));
            }
            catch (java.lang.IllegalArgumentException e){
                im = ImageIO.read(new File("background.jpg"));
            }
            image = im;

            panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(image, 0, 0, null);
                }
            };
        } catch (IOException e) {

            System.out.println("File Not Found");
            e.printStackTrace();
        }

//        JButton closeButton = new JButton("x");
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        closeButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
//        panel.add(closeButton);


//
//        panel.add(newOrderIndicator);
//
//        panel.setBounds(0,0,400,300);
    }

    public void run() {

        frame = new JFrame("Sliver Ring Village Hotel");
      //  frame.setIconImage(new ImageIcon("background.jpg").getImage());
        //frame.setBounds(0,0,400,400);

        JScrollPane jsp = new JScrollPane(panel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.setPreferredSize(new Dimension(frame.getWidth(),frame.getHeight()));
        frame.setContentPane(panel);
//        frame.setContentPane(jsp);
//        frame.setLayout(new BorderLayout());
        //frame.setContentPane(new JLabel(new ImageIcon("\\background.jpg")));
//        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
      //  frame.setUndecorated(true);
        frame.pack();
        frame.setVisible(true);


        int nreq = 1;
        try
        {
            ServerSocket sock = new ServerSocket(8080);
            System.out.println("Starting Server ...");

            for (;;)
            {

                Socket newsock = sock.accept();
                System.out.println("Creating thread ...");

                ThreadHandler t = new ThreadHandler(newsock,nreq);
                t.start();
                UIUpdater t1=new UIUpdater(t);
                t1.start();

            }
        }
        catch (Exception e)
        {
            System.out.println("IO error in loop " + e);
        }
        System.out.println("End!");
    }

    public void addOrder(String message){

        System.out.println("Adding order");
        JPanel orderPanel = new JPanel();

        BufferedImage im;
        final BufferedImage image;
        try {
           // image = ImageIO.read(new File("order_title.jpg"));
            try {
                im = ImageIO.read(getClass().getResource("/order_title.jpg"));
            }
            catch (java.lang.IllegalArgumentException e){
                im = ImageIO.read(new File("order_title.jpg"));
            }
            image = im;
            orderPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(image, 0, 0, null);
                }
            };
        } catch (IOException e) {

            System.out.println("File Not Found");
            e.printStackTrace();
        }

        orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
        orderPanel.setMinimumSize(new Dimension(50,100));
        orderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);




        newOrderIndicator.setText("New Order Arrived!");



        Order order = new Order(message);

        JLabel label  =  new JLabel();

        label.setVisible(true);
        count++;
        label.setForeground(Color.ORANGE);
        label.setText("Order "+count+": Table "+order.getTableId());
        label.setFont(new Font("serif", Font.PLAIN, 24));
        orderPanel.add(label);

        for(JPanel item:order.getItems()){
            orderPanel.add(item);

        }
        panel.add(orderPanel);
        panel.revalidate();
        //
//        panel.repaint();
//        frame.revalidate();
//        frame.repaint();



    }
    class UIUpdater extends Thread {
//        public void run() {
//            System.out.println("thread is running...");
//            addOrder();
//        }

        ThreadHandler producer;

        UIUpdater(ThreadHandler p) {
            producer = p;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String message = producer.getMessage();
                    System.out.println("Got message: " + message);
                    if(message!=null && !message.equals("updates"))
                        addOrder(message);
                    sleep(200);
                }
            } catch (InterruptedException e) {
            }
        }


    }

}
