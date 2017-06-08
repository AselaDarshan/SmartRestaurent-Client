package com.company;

/**
 * Created by asela on 6/5/17.
 */
public class Item {

    private double amount;
    private int qty;
    private String name;
    private int menuId;


    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setName(String name) {
        this.name = name;
    }



    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }



    public Item(double amount,int qty,String name){
        this.amount = amount;
        this.qty = qty;
        this.name =name;
    }

    public double getAmount() {
        return amount;
    }

    public int getQty() {
        return qty;
    }

    public String getName() {
        return name;
    }
}
