package com.example.rumahraga.model;

import com.example.rumahraga.data.remote.ApiService;

public class TransactionModel {
    private int transaction_id;
    private String transaction_code;
    private int mitra_id;
    private int field_id;
    private String user_id;
    private int status;
    private  int total_price;
    private String reason;
    private int payment_id;
    private String payment_receipt;
    private String created_at;
    private String updated_at;
    private String field_name;
    private String field_image;
    private String payment_name;
    private String review_id;

    public TransactionModel(int transaction_id, String transaction_code, int mitra_id, int field_id, String user_id, int status, int total_price, String reason, int payment_id, String payment_receipt, String created_at,
                            String updated_at, String field_name, String field_image, String payment_name, String review_id) {
        this.transaction_id = transaction_id;
        this.transaction_code = transaction_code;
        this.mitra_id = mitra_id;
        this.field_id = field_id;
        this.user_id = user_id;
        this.status = status;
        this.total_price = total_price;
        this.reason = reason;
        this.payment_id = payment_id;
        this.payment_receipt = payment_receipt;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.field_name = field_name;
        this.field_image = field_image;
        this.payment_name = payment_name;
        this.review_id = review_id;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
    }

    public int getMitra_id() {
        return mitra_id;
    }

    public void setMitra_id(int mitra_id) {
        this.mitra_id = mitra_id;
    }

    public int getField_id() {
        return field_id;
    }

    public void setField_id(int field_id) {
        this.field_id = field_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public String getPayment_receipt() {
        return payment_receipt;
    }

    public void setPayment_receipt(String payment_receipt) {
        this.payment_receipt = payment_receipt;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public String getField_image() {
        if (field_image != null) {
            return ApiService.BASE_URL + "data/fields/" + field_image;
        }else {
            return field_image;
        }
    }

    public void setField_image(String field_image) {
        this.field_image = field_image;
    }

    public String getPayment_name() {
        return payment_name;
    }

    public void setPayment_name(String payment_name) {
        this.payment_name = payment_name;
    }

    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }
}
