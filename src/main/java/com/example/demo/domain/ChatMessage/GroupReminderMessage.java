package com.example.demo.domain.ChatMessage;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class GroupReminderMessage {
    @Id
    private String id;
    private String roomId;
    private String reminderUsername;
//    private boolean isRead;
    private String content;
    private Long time;

    public GroupReminderMessage(){

    }

    public GroupReminderMessage(BaseMessage baseMessage){
        this.roomId = baseMessage.getRoomId();
        this.reminderUsername = baseMessage.getReceiverUsername();
        this.time = baseMessage.getTime();
        this.content = baseMessage.getContent();
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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getReminderUsername() {
        return reminderUsername;
    }

    public void setReminderUsername(String reminderUsername) {
        this.reminderUsername = reminderUsername;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
