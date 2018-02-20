package com.example.demo.services;

import com.example.demo.domain.ChatMessage.BaseMessage;
import com.example.demo.domain.User;
import com.example.demo.repositories.ChatMessage.BaseMessageRepositoryMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private BaseMessageRepositoryMongo chatMongo;

    @Autowired
    private UserService userService;

    public BaseMessage addNewChatMessage(BaseMessage baseMessage){
        return chatMongo.save(baseMessage);
    }

    public List<BaseMessage> getOneGroupMessages(String gid){
        Sort sort = new Sort(Sort.Direction.ASC, "time");
        return chatMongo.findByRoomId(gid, sort);
    }


    public List<BaseMessage> getAllMessageBySenderIdAndReceiverId(String uid, String fid){
        User[] users = getTwoUser(uid, fid);
        if(users == null) return new ArrayList<>();
        Sort sort = new Sort(Sort.Direction.ASC, "time");
//        System.out.println("look for mongo db");
        List<BaseMessage> alist = chatMongo.findBySenderUsernameAndReceiverUsername(users[0].getUsername(), users[1].getUsername(), sort);
        List<BaseMessage> blist = chatMongo.findBySenderUsernameAndReceiverUsername(users[1].getUsername(), users[0].getUsername(), sort);
        return mergeSortLists(alist, blist);
    }

    public List<BaseMessage> getAllMessageByReceiverIdAndRoomId(String uid, String rid) throws Exception {
        Sort sort = new Sort(Sort.Direction.ASC, "time");
        User user = userService.loadUserByUserId(uid);
        List<BaseMessage> res = chatMongo.findByReceiverUsernameAndRoomId(user.getUsername(), rid, sort);
        return res;
    }

    public List<BaseMessage> getAllMessageByRoomId(String rid){
        Sort sort = new Sort(Sort.Direction.ASC, "time");
        List<BaseMessage> res = chatMongo.findByRoomId(rid, sort);
        return res;
    }

    public BaseMessage updateBasedMessageToisRead(String uid, String mid){
        BaseMessage baseMessage = chatMongo.findOne(mid);
        baseMessage.setRead(true);
        return chatMongo.save(baseMessage);
    }



    private List<BaseMessage> mergeSortLists(List<BaseMessage> alist, List<BaseMessage> blist) {
        List<BaseMessage> ans = new ArrayList<>();
        int len = alist.size() + blist.size();
//        System.out.println("============== list length :: " + len);
        int i =0; int j =0;
        for(int k = 0; k < len; k ++){
            if(j == blist.size() || ((i < alist.size()) && (alist.get(i).getTime() <= blist.get(j).getTime()))){
                ans.add(alist.get(i));
                i ++;
            }else{
                ans.add(blist.get(j));
                j++;
            }
        }
        return ans;
    }

    private User[] getTwoUser(String uid, String fid){
        User[] users = new User[2];
        try {
            users[0] = userService.loadUserByUserId(uid);
            users[1] = userService.loadUserByUserId(fid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
//        System.out.println(" users  : " + users[0].getUsername() + "users2 :: " + users[1].getUsername());
        return users;
    }

}
