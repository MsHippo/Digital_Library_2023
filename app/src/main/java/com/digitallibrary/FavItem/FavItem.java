package com.digitallibrary.FavItem;

import android.graphics.Bitmap;

public class FavItem {
    private String title,description,author,imgUrl, categories;
    private int pages, book_id;
    private int drawableResource; // this for testing purpos...
    private Bitmap imgid;

    public FavItem(String title, String author, String imgUrl, String categories, int pages, int book_id, Bitmap imgid) {
        this.title = title;
        this.author = author;
        this.imgUrl = imgUrl;
        this.categories = categories;
        this.pages = pages;
        this.book_id = book_id;
        this.imgid = imgid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCategories() {
        return categories;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getDrawableResource() {
        return drawableResource;
    }

    public void setDrawableResource(int drawableResource) {
        this.drawableResource = drawableResource;
    }

    public Bitmap getImgid() {
        return imgid;
    }

    public void setImgid(Bitmap imgid) {
        this.imgid = imgid;
    }
}
