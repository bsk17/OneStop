package com.ecommerce.onestop.Model;

public class AdminOrders {

    private String name, address, date, time, statusOfOrder, totalAmount, city, phone;

    public AdminOrders() {
    }

    public AdminOrders(String name, String address, String date, String time, String statusOfOrder, String totalAmount, String city, String phone) {
        this.name = name;
        this.address = address;
        this.date = date;
        this.time = time;
        this.statusOfOrder = statusOfOrder;
        this.totalAmount = totalAmount;
        this.city = city;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getStatusOfOrder() {
        return statusOfOrder;
    }

    public void setStatusOfOrder(String statusOfOrder) {
        this.statusOfOrder = statusOfOrder;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
