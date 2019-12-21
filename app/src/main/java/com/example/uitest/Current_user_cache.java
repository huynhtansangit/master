package com.example.uitest;
import java.io.Serializable;

public class Current_user_cache implements Serializable {

    private String id;
    private String user_id;
    private String name;
    private String birth_day;
    private String phone;
    private String address;
    private String gender;
    private String avatar;
    private String created_at;
    private String updated_at;
    private String wallpaper;

    public String getWallpaper() {
        return wallpaper;
    }

    public void setWallpaper(String wallpaper) {
        this.wallpaper = wallpaper;
    }

    public Current_user_cache(String id, String user_id, String name, String birth_day, String phone, String address, String gender, String avatar, String created_at, String updated_at, String wallpaper) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.birth_day = birth_day;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.avatar = avatar;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.wallpaper = wallpaper;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(String birth_day) {
        this.birth_day = birth_day;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

}
