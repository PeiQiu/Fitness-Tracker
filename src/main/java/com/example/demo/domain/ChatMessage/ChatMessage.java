package com.example.demo.domain.ChatMessage;

import com.example.demo.domain.CustomerUser;
import com.example.demo.domain.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ChatMessage {

    @Id
    private String id;

    private CustomerUser sender;

    private String content;

    private Long datetime;

    private String roomId;

    private boolean isRead;

    public ChatMessage(){

    }

    public ChatMessage(Builder builder){
        this.sender = builder.sender;
        this.content = builder.content;
        this.datetime = builder.datetime;
        this.roomId = builder.roomId;
        this.id = builder.id;
        this.isRead = builder.isRead;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public CustomerUser getSender() {
        return sender;
    }

    public void setSender(User user){
        this.sender = new CustomerUser(user);
    }

    public void setSender(CustomerUser sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(Long datetime) {
        this.datetime = datetime;
    }

    public static class Builder {

        private String id;

        private CustomerUser sender;

        private String content;

        private Long datetime;

        private String roomId;

        private boolean isRead;

        public Builder id(String id){
            this.id = id;
            return this;
        }

        public Builder isRead(boolean isRead){
            this.isRead = isRead;
            return this;
        }

        public Builder roomId(String roomid){
            this.roomId = roomid;
            return this;
        }

        public Builder sender(User user) {
            this.sender = new CustomerUser(user);
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder datetime(Long datetime) {
            this.datetime = datetime;
            return this;
        }

        public ChatMessage build(){
            return new ChatMessage(this);
        }
    }
}
