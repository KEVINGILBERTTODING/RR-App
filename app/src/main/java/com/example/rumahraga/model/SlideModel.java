package com.example.rumahraga.model;

public class SlideModel {
    private String imageUrl;
    private String title;

    public SlideModel(String imageUrl, String title) {
        this.imageUrl = imageUrl;
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }
}