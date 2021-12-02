package com.example.smartlunches.Model;

public class Cart
{
    private String pid, pname, price, quantity, discount, pimage, psize , rating;
    private boolean itemveg;

    public Cart()
    {

    }

    public Cart(boolean itemveg ,String rating ,String pid, String pname, String price, String quantity, String discount, String pimage, String psize)
    {
        this.itemveg = itemveg;
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.pimage = pimage;
        this.psize = psize;
        this.rating = rating;
    }

    public String getPsize() {
        return psize;
    }

    public void setPsize(String psize) {
        this.psize = psize;
    }

    public boolean isItemveg() {
        return itemveg;
    }

    public void setItemveg(boolean itemveg) {
        this.itemveg = itemveg;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }
}
