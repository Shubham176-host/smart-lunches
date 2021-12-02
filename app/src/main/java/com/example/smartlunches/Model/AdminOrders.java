package com.example.smartlunches.Model;

import android.widget.ImageView;

public class AdminOrders
{
    private String name;
    private String usn;
    private String phone;
    private String date;
    private String time;
    private boolean orderstatus;
    private String products;
    private String productid;
    private String orderid;
    private String totalAmount;
    private String size;
    private String orderimg;
    private String rating;

    public AdminOrders() {
    }


    public AdminOrders(String rating , String orderid, String orderimg , String size, String name, String phone, boolean orderstatus, String products, String date, String time, String productid, String totalAmount) {
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.orderstatus = orderstatus;
        this.products = products;
        this.time = time;
        this.productid = productid;
        this.totalAmount = totalAmount;
        this.size = size;
        this.orderimg = orderimg;
        this.orderid =orderid;
        this.rating = rating;
    }
    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrderimg() {
        return orderimg;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setOrderimg(String orderimg) {
        this.orderimg = orderimg;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(boolean orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }


    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

}