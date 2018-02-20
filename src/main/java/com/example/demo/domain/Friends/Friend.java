package com.example.demo.domain.Friends;

import com.example.demo.domain.CustomerUser;
import com.example.demo.domain.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Friend {
    @Id
    private String id;

    private String userId;

    private String friendId;

    private Long dataTime;

    @Transient
    private CustomerUser friend;

    public Friend() {

    }

    public Friend(Builder builder){
        this.userId = builder.userId;
        this.friendId = builder.friendId;
        this.dataTime = builder.dataTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public Long getDataTime() {
        return dataTime;
    }

    public void setDataTime(Long dataTime) {
        this.dataTime = dataTime;
    }

    public CustomerUser getFriend() {
        return friend;
    }

    public void setFriend(User user){
        this.friend = new CustomerUser(user);
    }

    public void setFriend(CustomerUser friend) {
        this.friend = friend;
    }

    public static class Builder{
        private String userId;

        private String friendId;

        private Long dataTime;

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder friendId(String friendId) {
            this.friendId = friendId;
            return this;
        }

        public Builder dataTime(Long dataTime) {
            this.dataTime = dataTime;
            return this;
        }

        public Friend build(){
            return new Friend(this);
        }
    }
}
