package com.example.pet;

import android.graphics.drawable.Drawable;

public class Home_ListView_Item {
    private Drawable photoDrawable;
    private String nameStr;
    private String detailStr;

    public void setPhoto(Drawable photo) {
        photoDrawable = photo;
    }
    public void setName(String name) {
        nameStr = name;
    }
    public void setDetail(String detail) {
        detailStr = detail;
    }

    public Drawable getPhoto() {
        return this.photoDrawable;
    }
    public String getName() {
        return this.nameStr;
    }
    public String getDetail() {
        return this.detailStr;
    }
}
