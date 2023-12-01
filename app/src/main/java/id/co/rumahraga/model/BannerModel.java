package id.co.rumahraga.model;

import id.co.rumahraga.data.remote.ApiService;

public class BannerModel {
    private String banner_id;
    private String image;

    public BannerModel(String banner_id, String image) {
        this.banner_id = banner_id;
        this.image = image;
    }

    public String getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(String banner_id) {
        this.banner_id = banner_id;
    }

    public String getImage() {
        if (image != null) {
            return ApiService.BASE_URL + "data/banner/" + image;
        }else {
            return image;

        }
    }

    public void setImage(String image) {
        this.image = image;
    }
}
