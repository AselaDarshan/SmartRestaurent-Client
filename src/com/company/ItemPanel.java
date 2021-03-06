package com.company;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class ItemPanel {


    private JLabel name;
    private JLabel qty;
    private JButton readyButton;
    private Boolean state;
    private String tableId;
    private String waiterUsername;
    private Order order;
    private String orderItemId;

    public JPanel getItemPanel() {
        return itemPanel;
    }

    private JPanel itemPanel;


    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public ItemPanel(String tableId,String waiterUsername, Order order,String orderItemId) {
        this.order = order;
        this.tableId = tableId;
        this.waiterUsername = waiterUsername;
        this.orderItemId = orderItemId;
        itemPanel = new JPanel();
        final BufferedImage image;
        BufferedImage im;
        try {
            try {
                im = ImageIO.read(getClass().getResource("/order_bg.jpg"));
            }
            catch (java.lang.IllegalArgumentException e){
                im = ImageIO.read(new File("background.jpg"));
            }
            image = im;
            itemPanel = new JPanel() {
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


        itemPanel.setLayout(new GridBagLayout());
        GridBagConstraints nameC = new GridBagConstraints();
        nameC.fill = GridBagConstraints.HORIZONTAL;
        nameC.weightx = 1;
        nameC.gridx=0;
        nameC.gridy=0;
        nameC.anchor = GridBagConstraints.FIRST_LINE_START;

        GridBagConstraints qtyC = new GridBagConstraints();
        qtyC.anchor = GridBagConstraints.CENTER;
        qtyC.fill = GridBagConstraints.HORIZONTAL;
//        qtyC.weightx = 1;
        qtyC.gridx=1;
        qtyC.gridy=0;


        GridBagConstraints buttonC = new GridBagConstraints();
        buttonC.anchor = GridBagConstraints.LINE_END;
        buttonC.fill = GridBagConstraints.HORIZONTAL;
//        buttonC.weightx = 1;
        buttonC.gridx=2;
        buttonC.gridy=0;

        name = new JLabel();

        qty = new JLabel();
        readyButton = new JButton("PENDING");
        readyButton.setForeground(new Color(32,10,20));
        readyButton.setBackground(new Color(190,100,23));
        readyButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try {
                    String response = new WebServerCommunication().changeStateOfOrderItem(orderItemId,Constants.ITEM_STATE_PREPARED);
                    if(response!=null) {
                        readyButton.setText("READY");
                        readyButton.setForeground(new Color(12, 30, 20));
                        readyButton.setBackground(new Color(130, 150, 23));
                        CommuncationBus.putMessage(waiterUsername + "~" + qty.getText() + "x " + name.getText() + " for table " + tableId + " is Ready!`" + orderItemId);

                        order.itemReady(itemPanel);
                        readyButton.removeActionListener(this);
                    }
                    else{
                        new AndroidLikeToast().showDialog("Can't send message. No network connection",AndroidLikeToast.LENGTH_LONG);
                    }
                }catch (java.lang.NullPointerException ex){
                    new AndroidLikeToast().showDialog("Can't send the order completed message. No network connection",AndroidLikeToast.LENGTH_LONG);
                }


            }
        });

        itemPanel.add(name,nameC);
        itemPanel.add(qty,qtyC);
        itemPanel.add(readyButton,buttonC);

        itemPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        name.setBorder(BorderFactory.createEmptyBorder(5,30,5,0));
        name.setFont(new Font("serif", Font.BOLD, 16));
        qty.setBorder(BorderFactory.createEmptyBorder(0,40,0,40));
        qty.setFont(new Font("serif", Font.BOLD, 16));
        readyButton.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
//        readyButton.setSize(60,20);
        state = false;


    }

    public JLabel getNameLabel() {
        return name;
    }

    public void setNameLabelText(String name) {
        this.name.setText(name);
    }


    public JLabel getQtyLabel() {
        return qty;
    }

    public void setQtyLabelText(String qty) {
        this.qty.setText(qty);
    }

    public JButton getReadyButton() {
        return readyButton;
    }

    public void setReadyButton(JButton readyButton) {
        this.readyButton = readyButton;
    }

}