package com.example.rumahraga.model;

public class PaymentMethodModel {
    private String payment_id;
    private String payment_name;
    private String name;
    private String credential;
    private String image;
    private int status;

    public PaymentMethodModel(String payment_id, String payment_name, String name, String credential, String image, int status) {
        this.payment_id = payment_id;
        this.payment_name = payment_name;
        this.name = name;
        this.credential = credential;
        this.image = image;
        this.status = status;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getPayment_name() {
        return payment_name;
    }

    public void setPayment_name(String payment_name) {
        this.payment_name = payment_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
