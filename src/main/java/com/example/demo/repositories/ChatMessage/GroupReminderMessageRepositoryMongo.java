package com.example.demo.repositories.ChatMessage;


import com.example.demo.domain.ChatMessage.GroupReminderMessage;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupReminderMessageRepositoryMongo extends MongoRepository<GroupReminderMessage, String> {
    public List<GroupReminderMessage> deleteByRoomIdAndReminderUsername(String roomId, String reminderUsername);

    public List<GroupReminderMessage> findByRoomIdAndReminderUsername(String roomIdm, String reminderUsername, Sort sort);
}
