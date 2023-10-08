package com.example.rumahraga.model;

import com.example.rumahraga.data.remote.ApiService;

public class FieldModel {
    private String field_id;
    private String mitra_id;
    private String category_id;
    private String city_id;
    private String name;
    private String description;
    private String image;
    private String address;
    private int price;
    private int is_available;
    private String city_name;
    private String rating;
    public FieldModel(String field_id, String mitra_id, String category_id, String city_id, String name, String description, String image, String address, int price, int is_available,
                      String city_name, String rating) {
        this.field_id = field_id;
        this.mitra_id = mitra_id;
        this.category_id = category_id;
        this.city_id = city_id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.address = address;
        this.price = price;
        this.is_available = is_available;
        this.city_name = city_name;
        this.rating= rating;
    }


    public String getField_id() {
        return field_id;
    }

    public void setField_id(String field_id) {
        this.field_id = field_id;
    }

    public String getMitra_id() {
        return mitra_id;
    }

    public void setMitra_id(String mitra_id) {
        this.mitra_id = mitra_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        if (image != null) {
            return ApiService.BASE_URL + "data/fields/" + image;
        }else {
            return image;

        }
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getIs_available() {
        return is_available;
    }

    public void setIs_available(int is_available) {
        this.is_available = is_available;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
