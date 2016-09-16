package com.icannhas.searchexerciseapp;

/**
 * Created by user on 9/16/2016.
 */
public class JSONTestObject {

    private Long id;
    private Long userId;
    private String title;
    private String body;

    public JSONTestObject(Long id, Long userId, String title, String body){
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.body = body;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getUserId(){
        return userId;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getBody(){
        return body;
    }

    public void setBody(String body){
        this.body = body;
    }

}
