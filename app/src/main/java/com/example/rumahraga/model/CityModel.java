package com.example.rumahraga.model;

public class CityModel {
    private String city_id;
    private String province_id;
    private String nama;

    public CityModel(String city_id, String province_id, String nama) {
        this.city_id = city_id;
        this.province_id = province_id;
        this.nama = nama;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
