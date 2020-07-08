package com.weather.weashion;

import android.graphics.drawable.Drawable;

public class CartVO {

    private String img; //사진
    private String price; //가격(최저가)
    private String link; //상품 링크
    private String category; //분류

    public CartVO(String img, String price, String link, String category) {

        this.img = img;
        this.price = price;
        this.link = link;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
