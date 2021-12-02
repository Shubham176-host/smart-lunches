package com.example.smartlunches.Model;

public class Products
{
    private String pname, timeslot, price, image, category,pid, date, time , rating ,quantity ;
    private boolean itemveg , available;// should initallze in firebase not done yet
    private long type;

    public Products()
    {

    }
    public Products(boolean available , boolean itemveg, String quantity , String rating, String pname, String timeslot, long type, String price, String image, String category, String pid, String date, String time) {
        this.pname = pname;
        this.timeslot = timeslot;
        this.price = price;
        this.image = image;
        this.category = category;
        this.pid = pid;
        this.date = date;
        this.time = time;
        this.type = type;
        this.itemveg = itemveg;
        this.rating = rating;
        this.quantity = quantity;
        this.available = available;
    }


    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean availble) {
        this.available = availble;
    }


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public String getPname() {

        return pname;
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


    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(String timeslot) {
        this.timeslot = timeslot;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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
}
