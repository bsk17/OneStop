// this class will get all the data from FireBase DB

package com.ecommerce.onestop.Model;

public class Product {

    // nmae because in DB we accidentally stored name as nmae
    private String nmae, category, date, time, description, image, pid, price;

    // we have to add this constructor to avoid app crash this is a no argument constructor
    public Product() {}

    public Product(String nmae, String category, String date, String time, String description, String image, String pid, String price) {
        this.nmae = nmae;
        this.category = category;
        this.date = date;
        this.time = time;
        this.description = description;
        this.image = image;
        this.pid = pid;
        this.price = price;
    }

    public String getNmae() {
        return nmae;
    }

    public void setNmae(String nmae) {
        this.nmae = nmae;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
