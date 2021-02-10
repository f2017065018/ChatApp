package com.example.chatapp.model;

public class Chatting
{
    private String receivr,sendr,messag;
    private boolean is_seen;

    public Chatting(String receivr,String sendr,String messag,boolean is_seen)
    {
        this.receivr=receivr;
        this.sendr=sendr;
        this.messag=messag;
        this.is_seen=is_seen;
    }

    //setters
    public void setReceivr(String receivr) {
        this.receivr = receivr;
    }

    public void setSendr(String sendr) {
        this.sendr = sendr;
    }

    public void setMessag(String messag) {
        this.messag = messag;
    }

    public void setIs_seen(boolean is_seen)
    {
        this.is_seen = is_seen;
    }

    //getters


    public String getReceivr() {
        return receivr;
    }

    public String getSendr() {
        return sendr;
    }

    public String getMessag() {
        return messag;
    }

    public boolean isIs_seen()
    {
        return is_seen;
    }

    public Chatting()
    {




    }
}
