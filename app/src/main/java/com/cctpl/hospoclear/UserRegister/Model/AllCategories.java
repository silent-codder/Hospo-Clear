package com.cctpl.hospoclear.UserRegister.Model;

public class AllCategories {
    String Category;
    int Img;

    public AllCategories() {
    }

    public AllCategories(String category, int img) {
        Category = category;
        Img = img;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getImg() {
        return Img;
    }

    public void setImg(int img) {
        Img = img;
    }
}
