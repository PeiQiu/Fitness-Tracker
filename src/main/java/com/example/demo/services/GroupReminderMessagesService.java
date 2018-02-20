package com.example.demo.services;

import com.example.demo.domain.ChatMessage.GroupReminderMessage;
import com.example.demo.domain.User;
import com.example.demo.repositories.ChatMessage.GroupReminderMessageRepositoryMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupReminderMessagesService {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupReminderMessageRepositoryMongo reminderMongo;


    public List<GroupReminderMessage> removeAllReminderMessages(String uid, String roomId) throws Exception {
        User user = userService.loadUserByUserId(uid);
        return reminderMongo.deleteByRoomIdAndReminderUsername(roomId, user.getUsername());
    }

    public List<GroupReminderMessage> getAllReminderMessages(String uid, String roomId) throws Exception {
        Sort sort = new Sort(Sort.Direction.ASC, "time");
        User user = userService.loadUserByUserId(uid);
        return reminderMongo.findByRoomIdAndReminderUsername(roomId, user.getUsername(), sort);
    }

    public GroupReminderMessage saveOne(GroupReminderMessage groupReminderMessage){
        return reminderMongo.save(groupReminderMessage);
    }

}
