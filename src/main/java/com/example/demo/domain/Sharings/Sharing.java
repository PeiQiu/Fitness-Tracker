package com.example.demo.domain.Sharings;

import com.example.demo.domain.CommitParam;
import com.example.demo.domain.CustomerUser;
import com.example.demo.domain.User;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document
public class Sharing implements Serializable{
    @Id
    private String id;

    private String content;

    private String activeDataId;

    private Long sharingDate;

    private String autherId;

    private Integer state;

    private List<String> follow;

    @Transient
    private CustomerUser author;

    @Transient
    private SharingActiveData activeData;

    @Transient
    private List<Comment> comments;

    @Transient
    private List<CustomerUser> followers;

    public Sharing(){

    }

    public Sharing(Builder builder){
        this.content = builder.content;
        this.activeDataId = builder.activeDataId;
        this.sharingDate = builder.sharingDate;
        this.autherId = builder.autherId;
        this.state = builder.state;
    }


    public List<String> getFollow() {
        return follow;
    }

    public void setFollow(List<String> follow) {
        this.follow = follow;
    }

    public List<CustomerUser> getFollowers() {
        return followers;
    }

    public void setFollowers(List<CustomerUser> followers) {
        this.followers = followers;
    }

    public void setAuthor(CustomerUser author) {
        this.author = author;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public SharingActiveData getActiveData() {
        return activeData;
    }

    public void setActiveData(SharingActiveData activeData) {
        this.activeData = activeData;
    }

    public String getAutherId() {
        return autherId;
    }

    public void setAutherId(String autherId) {
        this.autherId = autherId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getActiveDataId() {
        return activeDataId;
    }

    public void setActiveDataId(String activeDataId) {
        this.activeDataId = activeDataId;
    }

    public Long getSharingDate() {
        return sharingDate;
    }

    public void setSharingDate(Long sharingDate) {
        this.sharingDate = sharingDate;
    }

    public CustomerUser getAuthor() {
        return author;
    }

    public void setAuthor(User user) {
        this.author = new CustomerUser(user);
    }

    public static class Builder{

       private String content;

       private String activeDataId;

       private Long sharingDate;

       private String autherId;

        private Integer state;

        private List<String> follow;

        public Builder follow(List<String> follow){
            this.follow = follow;
            return this;
        }

       public Builder autherId(String autherId){
           this.autherId = autherId;
           return this;
       }
       private Builder state(Integer state){
           this.state = state;
           return this;
       }

       public Builder content(String content) {
           this.content = content;
           return this;
       }

       public Builder activeDataId(String activeDataId) {
           this.activeDataId = activeDataId;
           return this;
       }

       public Builder sharingDate(Long sharingDate) {
           this.sharingDate = sharingDate;
           return this;
       }

       public Sharing build(){
           return new Sharing(this);
       }
   }

}
