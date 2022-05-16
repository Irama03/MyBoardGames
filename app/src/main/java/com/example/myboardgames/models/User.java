package com.example.myboardgames.models;

import com.example.myboardgames.database.AppDatabase;

import java.io.Serializable;

public class User implements UniqueObject, Serializable {

    private String id;
    private String username;

    public User() {

    }

    public User(String id, String username){
        this.id = id;
        this.username = username;
    }

    @Override
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

    @Override
    public  String toString(){
        return "Id: " + id + ", Псевдонім: " + username;
    }

    @Override
    public String whichGroup() {
        return AppDatabase.USERS_GROUP_KEY;
    }

}
