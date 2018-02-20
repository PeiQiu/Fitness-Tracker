package com.example.demo.controller;

import com.example.demo.domain.ChatMessage.BaseMessage;
import com.example.demo.domain.ChatMessage.ChatMessage;
import com.example.demo.domain.ChatMessage.GroupFriends;
import com.example.demo.domain.ChatMessage.GroupReminderMessage;
import com.example.demo.domain.User;
import com.example.demo.services.GroupFriendsService;
import com.example.demo.services.GroupReminderMessagesService;
import com.example.demo.services.MessageService;
import com.example.demo.services.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


@RequestMapping("/fitnesstracker/api/v1")
@PreAuthorize("hasPermission(#uid, 'Role', 'ROLE_USER')")
@RestController
public class WeChatMessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupFriendsService groupFriendsService;

    @Autowired
    private GroupReminderMessagesService groupReminderMessagesService;

    //---------save one group ---/user/{uid}/group/{gid}/{fid}
    @RequestMapping(value = "/user/{uid}/group" , method = RequestMethod.POST)
    public GroupFriends saveOneGroupFriends(@PathVariable String uid, @RequestBody GroupFriends groupFriends){
        groupFriends.setDatetime(new Date().getTime());
        return groupFriendsService.saveOneGroupFriends(groupFriends);
    }

    @RequestMapping(value = "/user/{uid}/group", method = RequestMethod.GET)
    public List<GroupFriends> getAllRelatedGroupFriends(@PathVariable String uid){
        return groupFriendsService.getListGroupFriends(uid);
    }

    @RequestMapping(value = "/user/{uid}/group/{gid}", method = RequestMethod.GET)
    public GroupFriends getOneGroupFriends(@PathVariable String uid, @PathVariable String gid){
        return groupFriendsService.getOneGroupFriends(gid);
    }

    @RequestMapping(value = "/user/{uid}/group/{gid}/friend/{fid}", method = RequestMethod.PUT)
    public GroupFriends addOneGroupFriends(@PathVariable String uid, @PathVariable String gid, @PathVariable String fid){
        return groupFriendsService.addOneUserToGroup(gid, fid);
    }

    @RequestMapping(value = "/user/{uid}/group/{gid}/friend/{fid}", method = RequestMethod.DELETE)
    public GroupFriends deleteOneGroupFriends(@PathVariable String uid, @PathVariable String gid, @PathVariable String fid){
        return groupFriendsService.deleteOneUserFromGroup(gid, fid);
    }



    @RequestMapping(value = "/messages/user/{uid}/group/{gid}", method = RequestMethod.GET)
    public List<ChatMessage> getAllGroupPreMessages(@PathVariable String uid, @PathVariable String gid){
        List<BaseMessage> messages =  messageService.getOneGroupMessages(gid);
        List<ChatMessage> ans = new ArrayList<>();
        for(BaseMessage baseMessage : messages){
                ans.add(makeChatMessage(baseMessage));
        }
        return ans;
    }


    // ---------- get Chat Messages from previous time --------------
    @RequestMapping(value = "/messages/user/{uid}/group/friend/{fid}", method = RequestMethod.GET)
    public List<ChatMessage> sendAllPrevMessagesByFriendId(@PathVariable String uid, @PathVariable String fid){
        List<BaseMessage> messages =  messageService.getAllMessageBySenderIdAndReceiverId(uid, fid);
        List<ChatMessage> ans = new LinkedList<>();
        for(BaseMessage baseMessage : messages){
            ans.add(makeChatMessage(baseMessage));
        }
        return ans;
    }

    @RequestMapping(value = "/messages/user/{uid}/group/{gid}/friend", method = RequestMethod.GET)
    public List<ChatMessage> sendAllPreMessageByRoomId(@PathVariable String uid, @PathVariable String gid) throws Exception {
        //List<BaseMessage> messages = messageService.getAllMessageByReceiverIdAndRoomId(uid, gid);
        List<BaseMessage> messages = messageService.getAllMessageByRoomId(gid);
        List<ChatMessage> ans = new LinkedList<>();
        for(BaseMessage baseMessage : messages){
            ans.add(makeChatMessage(baseMessage));
        }
        return ans;
    }

    @RequestMapping(value = "/messages/user/{uid}/isRead/{mid}", method = RequestMethod.PUT)
    public ChatMessage updateMessageStatusToRead(@PathVariable String uid, @PathVariable String mid){
        BaseMessage baseMessage = messageService.updateBasedMessageToisRead(uid, mid);
        return makeChatMessage(baseMessage);
    }

    @RequestMapping(value = "/message/user/{uid}/group/{gid}/reminder", method = RequestMethod.GET)
    public List<GroupReminderMessage> getAllReminderMessage(@PathVariable String uid, @PathVariable String gid) throws Exception {
        List<GroupReminderMessage> res = groupReminderMessagesService.getAllReminderMessages(uid, gid);
        return res;
    }

    @RequestMapping(value = "/message/user/{uid}/group/{gid}/reminder", method = RequestMethod.DELETE)
    public List<GroupReminderMessage> deleteAllRemonderMessage(@PathVariable String uid, @PathVariable String gid) throws Exception {
        List<GroupReminderMessage> res = groupReminderMessagesService.removeAllReminderMessages(uid, gid);
        return res;
    }

    private ChatMessage makeChatMessage(BaseMessage message){
        User sender = userService.loadUserByUsername(message.getSenderUsername());
        ChatMessage chatMessage = new ChatMessage.Builder()
                .id(message.getId()).content(message.getContent())
                .roomId(message.getRoomId()).datetime(message.getTime())
                .sender(sender).isRead(message.getRead() == null ? true : message.getRead())
                .build();
        return chatMessage;
    }
}
