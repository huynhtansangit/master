package com.example.uitest;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

public class TokenUser extends Application {
    private String token;
    private String password;
    private String email;
    private String address="";
    private String id_item;
    private Current_user_cache userOnline;

    public Current_user_cache getUserOnline() {
        return userOnline;
    }

    public void setUserOnline(Current_user_cache userOnline) {
        this.userOnline = userOnline;
    }

    public String getId_item() {
        return id_item;
    }

    public void setId_item(String id_item) {
        this.id_item = id_item;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ItemObj> getListFirstLoad() {
        return ListFirstLoad;
    }

    public void setListFirstLoad(List<ItemObj> listFirstLoad) {
        ListFirstLoad = listFirstLoad;
    }

    private List<ItemObj> ListFirstLoad;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
