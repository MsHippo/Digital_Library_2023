package com.digitallibrary.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

//serializable is important to send the book object to another activity
public class Book implements Serializable, Parcelable {

    private String title,description,author,imgUrl, categories, fav_status;
    private int pages, book_id;
    private int drawableResource; // this for testing purpose
    private Bitmap imgid;


    public Book() {
    }

    public Book(int drawableResource) {
        this.drawableResource = drawableResource;
    }

    public Book(int book_id, String title, String description, String author, String imgUrl, int pages, String categories, Bitmap imgid, String fav_status) {
        this.book_id = book_id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.imgUrl = imgUrl;
        this.pages = pages;
        this.categories = categories;
        this.imgid = imgid;
        this.fav_status = fav_status;
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

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getDrawableResource() {
        return drawableResource;
    }

    public void setDrawableResource(int drawableResource) {
        this.drawableResource = drawableResource;
    }

    public String getCategories(){
        return categories;
    }

    public void setCategories(String categories){
        this.categories = categories;
    }
    public Bitmap getImgid() {
        return imgid;
    }

    public void setImgid(Bitmap imgid) {
        this.imgid = imgid;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getFav_status() {
        return fav_status;
    }

    public void setFav_status(String fav_status) {
        this.fav_status = fav_status;
    }

    public Book(Parcel in) {

        String[] data = new String[8];
        in.readStringArray(data);

        this.book_id = Integer.parseInt(data[0]);
        this.title = data[1];
        this.description = data[2];
        this.author = data[3];
        this.imgUrl = String.valueOf(data[4]);
        this.pages = Integer.parseInt(data[5]);
        this.categories = data[6];
        this.fav_status = data[7];

//        imgid = Bitmap.CREATOR.createFromParcel(in);
//        book_id = in.readInt();
//        title = in.readString();
//        description = in.readString();
//        author = in.readString();
//        imgUrl = in.readString();
//        pages = in.readInt();
//        categories = in.readString();



    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//
//    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {String.valueOf(this.book_id),
        this.title ,
        this.description ,
        this.author ,
        this.imgUrl ,
                String.valueOf(this.pages),
        this.categories,
        this.fav_status});
//        imgid.writeToParcel(dest,0);
//        dest.writeInt(book_id);
//        dest.writeString(title);
//        dest.writeString(description);
//        dest.writeString(author);
//        dest.writeString(imgUrl);
//        dest.writeInt(pages);
//        dest.writeString(categories);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Book createFromParcel(Parcel in) {

            return new Book(in);
        }


        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    
}