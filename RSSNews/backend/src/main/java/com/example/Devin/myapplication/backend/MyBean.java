package com.example.Devin.myapplication.backend;

/** The object model for the data we are sending through endpoints */
public class MyBean {

    private String url;

    public String getData() {
        return url;
    }

    public void setData(String data) {
        url = data;
    }
}