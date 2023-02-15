package com.example.hand2sale.model;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String username;
    public String fullName;
    public String phone;
    public String photoUrl;

    public User(String username,String fullName,String phone,String photoUrl){
        this.fullName=fullName;
        this.username=username;
        this.phone=phone;
        this.photoUrl=photoUrl;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("fullName",fullName);
        userMap.put("username",username);
        userMap.put("phone",phone);
        userMap.put("photoUrl",photoUrl);
        return userMap;
    }
}