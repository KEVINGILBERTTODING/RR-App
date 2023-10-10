package com.example.rumahraga.model;

import java.io.Serializable;

public class BookedModel implements Serializable {
    private String order_date;
    private String transaction_code;
    private String jam_id;
    private int price;
    private String jam;

    public BookedModel(String order_date, String transaction_code, String jam_id, int price, String jam) {
        this.order_date = order_date;
        this.transaction_code = transaction_code;
        this.jam_id = jam_id;
        this.price = price;
        this.jam = jam;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
    }
    public String getJam_id() {
        return jam_id;
    }

    public void setJam_id(String jam_id) {
        this.jam_id = jam_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }
}
