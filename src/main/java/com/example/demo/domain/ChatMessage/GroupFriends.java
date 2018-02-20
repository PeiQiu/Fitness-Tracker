package com.example.demo.domain.ChatMessage;

import com.example.demo.domain.CustomerUser;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class GroupFriends {

    @Id
    private String id;

    private String name;

    private List<String> groupIds;

    private Long datetime;

    @Transient
    private List<CustomerUser> groupUsers;

    public GroupFriends() {

    }

    public GroupFriends(Builder builder){
        this.name = builder.name;
        this.groupIds = builder.groupIds;
        this.datetime = builder.datetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }

    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(Long datetime) {
        this.datetime = datetime;
    }

    public List<CustomerUser> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(List<CustomerUser> groupUsers) {
        this.groupUsers = groupUsers;
    }

    public static class Builder {
        private String name;

        private List<String> groupIds;

        private Long datetime;

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder groupIds(List<String> groupIds) {
            this.groupIds = groupIds;
            return this;
        }

        public Builder datetime(Long datetime) {
            this.datetime = datetime;
            return this;
        }

        public GroupFriends build(){
            return new GroupFriends(this);
        }
    }

}
