package com.example.demo.domain.Sharings;

import com.example.demo.domain.CustomerUser;
import com.example.demo.domain.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Comment {
    @Id
    private String id;

    private String customerUsername;

    private String parentCommentId;

    private String sharingId;

    private String content;

    private Long commentDate;

    private Integer state;


    //======================= list of comments =================
    @Transient
    private List<Comment> replyComments;
    @Transient
    private CustomerUser customerUser;
    @Transient
    private CustomerUser replyCustomerUser;


    public Comment(){

    }

    public Comment(Builder builder){
        this.customerUsername = builder.customerUsername;
        this.parentCommentId = builder.parentCommentId;
        this.sharingId = builder.sharingId;
        this.commentDate = builder.commentDate;
        this.content = builder.content;
        this.state = builder.state;
    }

    public void setCustomerUser(CustomerUser customerUser) {
        this.customerUser = customerUser;
    }

    public void setReplyCustomerUser(CustomerUser replyCustomerUser) {
        this.replyCustomerUser = replyCustomerUser;
    }

    public String getSharingId() {
        return sharingId;
    }

    public void setSharingId(String sharingId) {
        this.sharingId = sharingId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public String getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Long commentDate) {
        this.commentDate = commentDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }


    public List<Comment> getReplyComments() {
        return replyComments;
    }

    public void setReplyComments(List<Comment> replyComments) {
        this.replyComments = replyComments;
    }

    public CustomerUser getCustomerUser() {
        return customerUser;
    }

    public void setCustomerUser(User user) {
        this.customerUser = new CustomerUser(user);
    }

    public CustomerUser getReplyCustomerUser() {
        return replyCustomerUser;
    }

    public void setReplyCustomerUser(User user) {
        this.replyCustomerUser = new CustomerUser(user);
    }

    public static class Builder{
        private String customerUsername;

        private String parentCommentId;

        private String content;

        private Long commentDate;

        private Integer state;

        private String sharingId;

        public Builder sharingId(String sharingId){
            this.sharingId = sharingId;
            return this;
        }

        public Builder customerUsername(String customerUsername) {
            this.customerUsername = customerUsername;
            return this;
        }

        public Builder parentCommentId(String parentCommentId) {
            this.parentCommentId = parentCommentId;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder commentDate(Long commentDate) {
            this.commentDate = commentDate;
            return this;
        }


        public Builder state(Integer state) {
            this.state = state;
            return this;
        }


        public Comment build(){
            return new Comment(this);
        }
    }
}
