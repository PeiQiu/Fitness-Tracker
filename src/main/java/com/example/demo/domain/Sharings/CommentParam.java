package com.example.demo.domain.Sharings;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CommentParam {
    private String customerUsername;

    private String parentCommentId;

    private String sharingId;

    private String content;

    public CommentParam(){

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

    public String getSharingId() {
        return sharingId;
    }

    public void setSharingId(String sharingId) {
        this.sharingId = sharingId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
