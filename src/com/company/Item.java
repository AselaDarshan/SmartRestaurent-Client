package com.company;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class Item {


    private JLabel name;
    private JLabel qty;
    private JButton readyButton;
    private Boolean state;

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

    public Item() {
        itemPanel = new JPanel();
        final BufferedImage image;
        try {
            image = ImageIO.read(new File("order_bg.jpg"));
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
        readyButton = new JButton("READY");
        readyButton.setForeground(new Color(12,30,20));
        readyButton.setBackground(new Color(130,150,23));


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