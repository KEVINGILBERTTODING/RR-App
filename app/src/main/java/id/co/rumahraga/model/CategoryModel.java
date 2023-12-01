package id.co.rumahraga.model;

import id.co.rumahraga.data.remote.ApiService;

public class CategoryModel {
    private String category_id;
    private String name;
    private String image;

    public CategoryModel(String category_id, String name, String image) {
        this.category_id = category_id;
        this.name = name;
        this.image = image;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        if (image != null) {
            return ApiService.BASE_URL + "data/category/" + image;
        }else {
            return image;

        }
    }

    public void setImage(String image) {
        this.image = image;
    }
}
