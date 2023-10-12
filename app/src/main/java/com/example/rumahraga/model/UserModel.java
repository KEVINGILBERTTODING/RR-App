package com.example.rumahraga.model;

import com.example.rumahraga.data.remote.ApiService;

public class UserModel {
    private String user_id;
    private String username;
    private String email;
    private String profile_picture;
    private String name;
    private String city_name;

    public UserModel(String user_id, String username, String email, String profile_picture, String name) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.profile_picture = profile_picture;
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_picture() {
        return ApiService.BASE_URL + "data/photo_profile/" + profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
