package com.example.rumahraga.model;

public class TransactionDetailModel {
    private int detail_id;
    private String transaction_code;
    private int jam_id;
    private int price;
    private String order_date;
    private String jam;
    private String field_name;
    private int status;

    public TransactionDetailModel(int detail_id, String transaction_code, int jam_id, int price, String order_date,
                                  String jam, String field_name, int status) {
        this.detail_id = detail_id;
        this.transaction_code = transaction_code;
        this.jam_id = jam_id;
        this.price = price;
        this.order_date = order_date;
        this.jam = jam;
        this.field_name = field_name;
        this.status = status;
    }

    public int getDetail_id() {
        return detail_id;
    }

    public void setDetail_id(int detail_id) {
        this.detail_id = detail_id;
    }

    public String getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
    }


    public int getJam_id() {
        return jam_id;
    }

    public void setJam_id(int jam_id) {
        this.jam_id = jam_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public int getStatus() {
        return status;
    }
}
