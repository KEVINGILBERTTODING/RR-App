package com.example.rumahraga.model;

public class JamModel {
    private String jam_id;
    private String jam;
    private int is_available;

    public JamModel(String jam_id, String jam, int is_available) {
        this.jam_id = jam_id;
        this.jam = jam;
        this.is_available = is_available;
    }

    public String getJam_id() {
        return jam_id;
    }

    public void setJam_id(String jam_id) {
        this.jam_id = jam_id;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public int getIs_available() {
        return is_available;
    }

    public void setIs_available(int is_available) {
        this.is_available = is_available;
    }
}
