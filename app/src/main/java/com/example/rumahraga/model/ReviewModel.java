package com.example.rumahraga.model;

import com.example.rumahraga.data.remote.ApiService;

public class ReviewModel {
    private String review_id;
    private String user_id;
    private String field_id;
    private String title;
    private String review_text;
    private int status;
    private float stars;
    private String username;
    private String profile_picture;
    private String created_at;
    private float total_rating;

    public ReviewModel(String review_id, String user_id, String field_id, String title, String review_text, int status, float stars, String username, String profile_picture, String created_at, float total_rating) {
        this.review_id = review_id;
        this.user_id = user_id;
        this.field_id = field_id;
        this.title = title;
        this.review_text = review_text;
        this.status = status;
        this.stars = stars;
        this.username = username;
        this.profile_picture = profile_picture;
        this.created_at = created_at;
        this.total_rating = total_rating;
    }

    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getField_id() {
        return field_id;
    }

    public void setField_id(String field_id) {
        this.field_id = field_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto_profile() {

        return ApiService.BASE_URL + "data/photo_profile/" + profile_picture;
    }

    public void setPhoto_profile(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public float getTotal_rating() {
        return total_rating;
    }

    public void setTotal_rating(float total_rating) {
        this.total_rating = total_rating;
    }
}
