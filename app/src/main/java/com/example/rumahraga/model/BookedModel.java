package com.example.rumahraga.model;

public class BookedModel {
    private String order_date;
    private String jam;

    public BookedModel(String order_date, String jam) {
        this.order_date = order_date;
        this.jam = jam;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }
}
