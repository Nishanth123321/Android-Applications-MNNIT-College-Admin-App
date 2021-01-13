package com.nishanth.mnnit_admin.Faculty;

public class TeacherData {
    private String name, email, post, key, imageUrl="";

    public TeacherData(String name, String email, String post, String key, String imageUrl) {
        this.name = name;
        this.email = email;
        this.post = post;
        this.key = key;
        this.imageUrl = imageUrl;
    }
    public TeacherData(String name, String email, String post, String key) {
        this.name = name;
        this.email = email;
        this.post = post;
        this.key = key;

    }
    public TeacherData() {


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
