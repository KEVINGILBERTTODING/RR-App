package com.example.rumahraga.model;

public class NotificationModel {
    private int notif_id;
    private String user_id;
    private int is_read;
    private int type;
    private String created_at;
    private String transaction_code;
    private int status;
    private int total_price;
    private String reason;
    private String payment_name;

    public NotificationModel(int notif_id, String user_id, int is_read, int type, String created_at, String transaction_code, int status, int total_price, String reason, String payment_name) {
        this.notif_id = notif_id;
        this.user_id = user_id;
        this.is_read = is_read;
        this.type = type;
        this.created_at = created_at;
        this.transaction_code = transaction_code;
        this.status = status;
        this.total_price = total_price;
        this.reason = reason;
        this.payment_name = payment_name;
    }

    public int getNotif_id() {
        return notif_id;
    }

    public void setNotif_id(int notif_id) {
        this.notif_id = notif_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
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

    public String getPayment_name() {
        return payment_name;
    }

    public void setPayment_name(String payment_name) {
        this.payment_name = payment_name;
    }
}
