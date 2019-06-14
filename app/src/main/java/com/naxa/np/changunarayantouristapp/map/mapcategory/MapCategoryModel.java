package com.naxa.np.changunarayantouristapp.map.mapcategory;

import android.graphics.drawable.Drawable;

public class MapCategoryModel {

    String categoryIcon ;
    String categoryName;
    Drawable drawablecategoryIcon;


    public MapCategoryModel(String categoryIcon, String categoryName) {
        this.categoryIcon = categoryIcon;
        this.categoryName = categoryName;
    }

    public MapCategoryModel(String categoryName, Drawable drawablecategoryIcon) {
        this.categoryName = categoryName;
        this.drawablecategoryIcon = drawablecategoryIcon;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Drawable getDrawablecategoryIcon() {
        return drawablecategoryIcon;
    }

    public void setDrawablecategoryIcon(Drawable drawablecategoryIcon) {
        this.drawablecategoryIcon = drawablecategoryIcon;
    }
}
