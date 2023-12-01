package id.co.rumahraga.model;

import id.co.rumahraga.data.remote.ApiService;

public class EventModel {
    private int id;
    private int category_id;
    private String title;
    private String picture_name;
    private String date;
    private String content;
    private String event_category;


    public EventModel(int id, int category_id, String title, String picture_name, String date, String content, String event_category) {
        this.id = id;
        this.category_id = category_id;
        this.title = title;
        this.picture_name = picture_name;
        this.date = date;
        this.content = content;
        this.event_category = event_category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture_name() {
        return ApiService.BASE_URL + "data/event/" + picture_name;
    }

    public void setPicture_name(String picture_name) {
        this.picture_name = picture_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEvent_category() {
        return event_category;
    }

    public void setEvent_category(String event_category) {
        this.event_category = event_category;
    }
}
