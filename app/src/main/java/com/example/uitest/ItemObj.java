package com.example.uitest;

import java.io.Serializable;
import java.util.List;

import com.example.uitest.comment.commentObj;
public class ItemObj implements Serializable {
    private String id;
    private String user_id;
    private String title;
    private String name;
    private String cost;
    private String producer;
    private String seats;
    private String url_image;
    private String view;
    private String detail;
    private String created_at;
    private String updated_at;
    private List<commentObj> comments;

    public List<commentObj> getComments() {
        return comments;
    }

    public void setComments(List<commentObj> comments) {
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ItemObj(String id, String user_id, String title, String name, String cost, String producer, String seats, String url_image, String view, String detail, String created_at, String updated_at) {
        this.id = id;
        this.user_id = user_id;
        this.title = title;
        this.name = name;
        this.cost = cost;
        this.producer = producer;
        this.seats = seats;
        this.url_image = url_image;
        this.view = view;
        this.detail = detail;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
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
