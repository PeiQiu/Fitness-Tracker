package com.example.demo.domain.Friends;


import com.example.demo.domain.CustomerUser;
import com.example.demo.domain.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class FriendRequest {
    @Id
    private String id;

    private String fromCustomerUserId;

    private String toCustomerUserId;

    private Long dataTime;

    private Boolean status;

    @Transient
    private CustomerUser fromCustomerUser;

    @Transient
    private CustomerUser toCustomerUser;

    public FriendRequest(){

    }
    public FriendRequest(Builder builder){
        this.dataTime = builder.dataTime;
        this.fromCustomerUserId = builder.fromCustomerId;
        this.toCustomerUserId = builder.toCustomerId;
        this.status = builder.status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromCustomerUserId() {
        return fromCustomerUserId;
    }

    public void setFromCustomerUserId(String fromCustomerUserId) {
        this.fromCustomerUserId = fromCustomerUserId;
    }

    public String getToCustomerUserId() {
        return toCustomerUserId;
    }

    public void setToCustomerUserId(String toCustomerUserId) {
        this.toCustomerUserId = toCustomerUserId;
    }

    public Long getDataTime() {
        return dataTime;
    }

    public void setDataTime(Long dataTime) {
        this.dataTime = dataTime;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public CustomerUser getFromCustomerUser() {
        return fromCustomerUser;
    }

    public void setFromCustomerUser(User user){
        this.fromCustomerUser = new CustomerUser(user);
    }

    public void setFromCustomerUser(CustomerUser fromCustomerUser) {
        this.fromCustomerUser = fromCustomerUser;
    }

    public CustomerUser getToCustomerUser() {
        return toCustomerUser;
    }

    public void setToCustomerUser(CustomerUser toCustomerUser) {
        this.toCustomerUser = toCustomerUser;
    }

    public static class Builder {

        private String fromCustomerId;

        private String toCustomerId;

        private Long dataTime;

        private Boolean status;

        public Builder fromCustomerId(String fromCustomerId) {
            this.fromCustomerId = fromCustomerId;
            return this;
        }

        public Builder toCustomerId(String toCustomerId) {
            this.toCustomerId = toCustomerId;
            return this;
        }

        public Builder dataTime(Long dataTime) {
            this.dataTime = dataTime;
            return this;
        }

        public Builder status(Boolean status) {
            this.status = status;
            return this;
        }

        public FriendRequest build(){
            return new FriendRequest(this);
        }

    }
}
