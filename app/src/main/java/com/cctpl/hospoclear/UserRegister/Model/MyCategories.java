package com.cctpl.hospoclear.UserRegister.Model;

public class MyCategories {
    String Category;
    int imageId;

    public MyCategories(MyCategories[] myCategory) {
    }

    public MyCategories(String category, int imageId) {
        Category = category;
        this.imageId = imageId;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
