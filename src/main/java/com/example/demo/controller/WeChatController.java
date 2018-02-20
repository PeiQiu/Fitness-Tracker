package com.example.demo.controller;

import com.example.demo.domain.ChatMessage.BaseMessage;
import com.example.demo.domain.ChatMessage.ChatMessage;
import com.example.demo.domain.ChatMessage.GroupFriends;
import com.example.demo.domain.ChatMessage.GroupReminderMessage;
import com.example.demo.domain.CustomerUser;
import com.example.demo.domain.User;
import com.example.demo.services.GroupFriendsService;
import com.example.demo.services.GroupReminderMessagesService;
import com.example.demo.services.MessageService;
import com.example.demo.services.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Date;
import java.util.List;

//@PreAuthorize("hasRole('ROLE_USER')")
@Controller
public class WeChatController {
    private static final String DEST =  "/topic/chat";
    private static final String DESTTOCHATROOM = "/topic/chat/group";
    private static final String REMINDERFRIEND = "/topic/reminder/friend";
    private static final String DELETEONECONNECT = "/topic/reminder/delete";
    private static final String ACCEPTNEWFRIEND = "/topic/accept/friend";


    private Gson gson = new Gson();

    @Autowired
    private UserService userService;

    @Autowired
    private GroupFriendsService groupFriendsService;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private MessageService messageService;

    @Autowired
    private GroupReminderMessagesService groupReminderMessagesService;

    @MessageMapping("/user/{uid}/group/{gid}")
    public void sendChatMessageToGroup(@DestinationVariable String uid, @DestinationVariable String gid, String message) throws Exception {
//        System.out.println(" ------ group chat message :: " + "   user id :: " + uid + "  gid ::: " + gid + "  message === " + message );
        User user = userService.loadUserByUserId(uid);
        BaseMessage baseMessage = gson.fromJson(message, BaseMessage.class);// room id, content
        baseMessage.setSenderUsername(user.getUsername());//base message sender username
        baseMessage.setTime(new Date().getTime());// time
        if(baseMessage.getRoomId() == null || baseMessage.getRoomId().equals("")){
            return;
        }
//        baseMessage.setRead(false);// base message status
        baseMessage = messageService.addNewChatMessage(baseMessage);// get base message id
        // save message for one group at sender by one person
        GroupFriends groupFriends = groupFriendsService.getOneGroupFriends(gid);
        List<CustomerUser> customerUsers = groupFriends.getGroupUsers();
        for(CustomerUser customerUser : customerUsers){
            baseMessage.setReceiverUsername(customerUser.getUsername());// base message receiver username
            if(!customerUser.getUsername().equals(user.getUsername())){
                GroupReminderMessage groupReminderMessage = new GroupReminderMessage(baseMessage);
                groupReminderMessagesService.saveOne(groupReminderMessage);
            }
            // for each person store group message
            ChatMessage chatMessage = makeChatMessage(baseMessage);
            chatMessage.setRoomId(gid);
            template.convertAndSendToUser(customerUser.getUsername(), DESTTOCHATROOM, gson.toJson(chatMessage));
        }
    }

    @MessageMapping("/chat")
    public void sendChatMessageToUser(Principal principal, String message) {
//        System.out.println("============= chat message info ------------- " + principal.getName() + " ;;; messge :: " + message);
        BaseMessage baseMessage = gson.fromJson(message, BaseMessage.class);
        baseMessage.setSenderUsername(principal.getName());
        baseMessage.setTime(new Date().getTime());
        baseMessage.setRead(false);
        baseMessage = messageService.addNewChatMessage(baseMessage);
        this.sendToUser(baseMessage);
    }

    @MessageMapping(value = "/user/{uid}/reminder/friends/{fid}")
    public void reminderOneFriend(@DestinationVariable String uid, @DestinationVariable String fid) throws Exception {
        User user = userService.loadUserByUserId(fid);
        template.convertAndSendToUser(user.getUsername(), REMINDERFRIEND, "friend");
    }

    @MessageMapping(value = "/user/{uid}/reminder/group/{gid}")
    public void reminderOneGroupMember(@DestinationVariable String uid, @DestinationVariable String gid){
        GroupFriends groupFriends = groupFriendsService.getOneGroupFriends(gid);
        List<CustomerUser> list = groupFriends.getGroupUsers();
        for(CustomerUser customerUser : list){
            if(customerUser.getId().equals(uid)) continue;
            template.convertAndSendToUser(customerUser.getUsername(), REMINDERFRIEND, "group");
        }
    }

    @MessageMapping(value = "/user/{uid}/accept/friends/{fid}")
    public void acceptNewFriends(@DestinationVariable String uid, @DestinationVariable String fid) throws Exception {
        User user = userService.loadUserByUserId(fid);
//        System.out.println("accept new friends :: " + user.getUsername());
        template.convertAndSendToUser(user.getUsername(), ACCEPTNEWFRIEND, "friend");
    }

    @MessageMapping(value = "/user/{uid}/delete/friends/{fid}")
    public void deleteOneFriend(@DestinationVariable String uid, @DestinationVariable String fid) throws Exception {
        User user = userService.loadUserByUserId(fid);
//        System.out.println("88888888888888888888888888888888888888delete one frind ::: " + user.getUsername());
        template.convertAndSendToUser(user.getUsername(), DELETEONECONNECT, "friend");
    }

    @MessageMapping(value = "/user/{uid}/delete/group/{gid}")
    public void deleteOneUserFromGroup(@DestinationVariable String uid, @DestinationVariable String gid){
        GroupFriends groupFriends = groupFriendsService.getOneGroupFriends(gid);
        List<CustomerUser> list = groupFriends.getGroupUsers();
        for(CustomerUser customerUser : list){
            if(customerUser.getId().equals(uid)) continue;
            template.convertAndSendToUser(customerUser.getUsername(), DELETEONECONNECT, "group");
        }
    }

    private ChatMessage makeChatMessage(BaseMessage message){
        User sender = userService.loadUserByUsername(message.getSenderUsername());
        ChatMessage chatMessage = new ChatMessage.Builder()
                .id(message.getId()).sender(sender)
                .roomId(message.getRoomId()).content(message.getContent())
                .isRead(message.getRead() == null ? true : message.getRead()).datetime(message.getTime()).build();
        return chatMessage;
    }
    private void sendToUser(BaseMessage message){
        ChatMessage chatMessage = makeChatMessage(message);
        template.convertAndSendToUser(message.getReceiverUsername(), DEST, gson.toJson(chatMessage));
    }


}
