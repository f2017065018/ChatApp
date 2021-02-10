package com.example.chatapp.model;

public class Users {

    private String username,id,imageurl,v_status;

    //caling constructors

    public Users()
    {}


    public Users(String username,String id,String imageurl)
    {

        this.username=username;

        this.id=id;

        this.imageurl=imageurl;

        this.v_status= v_status;
    }

    //getters


    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getV_status()
    {
        return v_status;
    }

    //setters

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setImageurl(String imageurl)
    {
        this.imageurl = imageurl;
    }

    public void setV_status(String v_status) {
        this.v_status = v_status;
    }
}
