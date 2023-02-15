package com.example.hand2sale.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.hand2sale.MyApplication;
import com.google.firebase.firestore.FieldValue;

import com.google.firebase.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Post {

    @PrimaryKey
    @NonNull
    private String id;
    private String authorID;
    private String title;
    private String desc;
    private String image;
    private Double price;
    private Long lastUpdated;

    public Post() {
    }


    public Post(String id, String authorID, String title, String desc, String image, Double price) {
        this.id = id;
        this.authorID = authorID;
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.price = price;
    }

    static final String TITLE="title";
    static final String ID="id";
    static final String AUTHOR_ID="authorID";
    static final String DESCRIPTION="description";
    static final String IMAGE_URL="imageUrl";
    static final String PRICE="price";
    static final String COLLECTION ="posts";
    static final String LAST_UPDATED="lastUpdated";
    static final String LOCAL_LAST_UPDATED="post_local_last_update";

    public static Post fromJson(Map<String,Object> json){

        String id = (String) json.get(ID);
        String title =(String)json.get(TITLE);
        String description = (String) json.get(DESCRIPTION);
        String image = (String) json.get(IMAGE_URL);
        String authorID = (String) json.get(AUTHOR_ID);
        Double price = (Double) json.get(PRICE);
        Post post = new Post(id,authorID,title,description,image,price);
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
        editor.commit();
    }

    public Map<String, Object> toJson() {
        HashMap<String, Object> result = new HashMap<>();

        result.put(ID, id);
        result.put(AUTHOR_ID, authorID);
        result.put(TITLE, title);
        result.put(DESCRIPTION, desc);
        result.put(IMAGE_URL, image);
        result.put(PRICE, price);
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

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
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

}
