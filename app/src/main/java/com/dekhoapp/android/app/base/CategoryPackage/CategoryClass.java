package com.dekhoapp.android.app.base.CategoryPackage;

public class CategoryClass
{
    private String category_name;
    int image;

    public CategoryClass(int image, String category_name) {
        this.image = image;
        this.category_name = category_name;
    }

    public CategoryClass() {
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
