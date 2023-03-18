package com.example.hand2sale.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.hand2sale.MyApplication;
import com.google.firebase.firestore.FieldValue;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "Post")
public class Post {

    @PrimaryKey
    @NonNull
    private String id;
    private String authorEmail;
    private String title;
    private String desc;
    private String image;
    private Double price;
    private Long uploadTimestamp;
    private Long lastUpdated;


    public Post(@NonNull String id, String authorEmail, String title, String desc, String image, Double price,Long uploadTimestamp) {
        this.id = id;
        this.authorEmail = authorEmail;
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.price = price;
        this.uploadTimestamp=uploadTimestamp;
    }

    static final String TITLE="title";
    static final String ID="id";
    static final String AUTHOR_EMAIL="authorEmail";
    static final String DESCRIPTION="description";
    static final String IMAGE_URL="imageUrl";
    static final String PRICE="price";
    static final String UPLOAD_TIMESTAMP="uploadTimestamp";
    static final String COLLECTION ="posts";
    static final String LAST_UPDATED="lastUpdated";
    static final String LOCAL_LAST_UPDATED="post_local_last_update";

    public static Post fromJson(Map<String,Object> json){

        String id = (String) json.get(ID);
        String title =(String)json.get(TITLE);
        String description = (String) json.get(DESCRIPTION);
        String image = (String) json.get(IMAGE_URL);
        String authorEmail = (String) json.get(AUTHOR_EMAIL);
        Double price = (Double) json.get(PRICE);
        Long uploadTimestamp=(Long) json.get(UPLOAD_TIMESTAMP);
        Post post = new Post(id,authorEmail,title,description,image,price,uploadTimestamp);
        try{
            Timestamp time = (Timestamp) json.get(LAST_UPDATED);
            post.setLastUpdated(time.getSeconds());
        }catch(Exception e){
            e.printStackTrace();
        }
        return post;
    }

    public static Long getLocalLastUpdate() {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sharedPref.getLong(LOCAL_LAST_UPDATED, 0);
    }

    public static void setLocalLastUpdate(Long time) {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(LOCAL_LAST_UPDATED,time);
        editor.apply();
    }

    public Map<String, Object> toJson() {
        HashMap<String, Object> result = new HashMap<>();

        result.put(ID, id);
        result.put(AUTHOR_EMAIL, authorEmail);
        result.put(TITLE, title);
        result.put(DESCRIPTION, desc);
        result.put(IMAGE_URL, image);
        result.put(PRICE, price);
        result.put(UPLOAD_TIMESTAMP,new Date().getTime());
        result.put(LAST_UPDATED, FieldValue.serverTimestamp());
        return result;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getLastUpdated(){return this.lastUpdated;}

    public void setLastUpdated(Long lastUpdated){this.lastUpdated=lastUpdated;}

    public Long getUploadTimestamp(){
        return this.uploadTimestamp;
    }

    public void setUploadTimestamp(Long uploadTimestamp){
        this.uploadTimestamp=uploadTimestamp;
    }

}
