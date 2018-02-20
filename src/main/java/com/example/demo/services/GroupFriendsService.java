package com.example.demo.services;

import com.example.demo.domain.ChatMessage.GroupFriends;
import com.example.demo.domain.CustomerUser;
import com.example.demo.domain.User;
import com.example.demo.repositories.GroupFriends.GroupFriendsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GroupFriendsService {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupFriendsRepository mongoRepository;

    private void configGroupUsersAndName(GroupFriends groupFriends) throws Exception {
            List<CustomerUser> list = new ArrayList<>();
            List<String> tempNames = new ArrayList<>();
            List<String> ids = groupFriends.getGroupIds();
            for(String id : ids){
                User user = userService.loadUserByUserId(id);
                tempNames.add(user.getUsername());
                list.add(new CustomerUser(user));
            }
            groupFriends.setGroupUsers(list);
            if(groupFriends.getName() == null || groupFriends.getName().equals("")){
                groupFriends.setName(String.join(",", tempNames));
            }
    }


    public GroupFriends saveOneGroupFriends(GroupFriends groupFriends){
        return mongoRepository.save(groupFriends);
    }

    public GroupFriends getOneGroupFriends(String id){
        GroupFriends groupFriends =  mongoRepository.findOne(id);
        try {
            configGroupUsersAndName(groupFriends);
        } catch (Exception e) {
            e.printStackTrace();
           return null;
        }
        return groupFriends;
    }

    public List<GroupFriends> getListGroupFriends(String uid){
        Sort sort = new Sort(Sort.Direction.DESC, "datetime");
        List<GroupFriends> list = mongoRepository.findByGroupIdsContaining(uid);
        for(GroupFriends groupFriends : list){
            try {
                configGroupUsersAndName(groupFriends);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        return list;
    }

    public GroupFriends addOneUserToGroup(String id, String newUserId){
        GroupFriends groupFriends = mongoRepository.findOne(id);
        groupFriends.getGroupIds().add(newUserId);
        groupFriends.setDatetime(new Date().getTime());
        GroupFriends newGroupFriends = mongoRepository.save(groupFriends);
        try {
            configGroupUsersAndName(newGroupFriends);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return newGroupFriends;
    }

    public GroupFriends deleteOneUserFromGroup(String id, String userId){
        GroupFriends groupFriends = mongoRepository.findOne(id);
        groupFriends.getGroupIds().remove(userId);
        groupFriends.setDatetime(new Date().getTime());
        GroupFriends newGroupFriends = mongoRepository.save(groupFriends);
        try {
            configGroupUsersAndName(newGroupFriends);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return newGroupFriends;
    }

}
