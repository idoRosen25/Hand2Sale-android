package com.example.hand2sale.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.hand2sale.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "User")
public class User {

    @PrimaryKey
    @NonNull
    private String id;
    private String username;
    private String fullName;
    private String phone;

    private Long lastUpdated;

    public User(@NonNull String id,String username,String fullName,String phone){
        this.id=id;
        this.fullName=fullName;
        this.username=username;
        this.phone=phone;
    }

    static final String ID="id";
    static final String FULL_NAME="fullName";
    static final String USERNAME="username";
    static final String PHONE="phone";
    static final String COLLECTION="users";
    static final String LAST_UPDATED="lastUpdated";
    static final String LOCAL_LAST_UPDATED="user_local_last_update";


    public static User fromJson(Map<String,Object> json){

        String id = (String) json.get(ID);
        String fullName = (String) json.get(FULL_NAME);
        String username = (String) json.get(USERNAME);
        String phone = (String)json.get(PHONE);
        User user = new User(id,username,fullName,phone);

        try{
            Timestamp time = (Timestamp) json.get(LAST_UPDATED);
            user.setLastUpdated(time.getSeconds());
        }catch(Exception e){
            e.printStackTrace();
        }

        return user;
    }

    public static Long getLocalLastUpdate(){
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sharedPref.getLong(LOCAL_LAST_UPDATED,0);
    }

    public static void setLocalLastUpdate(Long time){
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPref.edit();
        editor.putLong(LOCAL_LAST_UPDATED,time);
        editor.apply();
    }

    public Map<String,Object> toMap(){
        Map<String,Object> userMap = new HashMap<>();
        userMap.put(ID,id);
        userMap.put(FULL_NAME,fullName);
        userMap.put(USERNAME,username);
        userMap.put(PHONE,phone);
        userMap.put(LAST_UPDATED, FieldValue.serverTimestamp());
        return userMap;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}