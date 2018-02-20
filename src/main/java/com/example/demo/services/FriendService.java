package com.example.demo.services;

import com.example.demo.domain.Friends.Friend;
import com.example.demo.domain.User;
import com.example.demo.repositories.Friend.FriendRepository;
import com.example.demo.repositories.Friend.FriendRepositoryMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FriendService {
    @Autowired
    private FriendRepositoryMongo friendRepositoryMongo;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendRequestService requestService;

    private List<Friend> getFriendsByUserId( String userId ){
        return friendRepositoryMongo.findByUserIdOrFriendId( userId, userId, new Sort(new Sort.Order(Sort.Direction.DESC, "dataTime")));
    }

    public List<Friend> getFriends(String uid){
        List<Friend> friends = getFriendsByUserId(uid);
        for(Friend friend : friends){
            String friendId = null;
            if(friend.getUserId().equals(uid)){
                friendId = friend.getFriendId();
            }else{
                friendId = friend.getUserId();
            }

            if(friendId == null){
                continue;
            }

            User user = null;
            try {
                user = userService.loadUserByUserId(friendId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(user != null){
                user.setPassword(null);
            }
            friend.setFriend( user );
        }
        return friends;
    }


    public Friend SaveOneFriend(Friend friend){
        return friendRepositoryMongo.save(friend);
    }

    public Friend updateFriendContactDataTime(String id){
        Friend friend = friendRepositoryMongo.findOne( id );
        friend.setDataTime(new Date().getTime());
        return friendRepositoryMongo.save(friend);
    }

    public Friend deleteOneFriend(String id){
        Friend friend = friendRepositoryMongo.findOne(id);
        friendRepositoryMongo.delete( id );
        requestService.deleteFriendRequestByFromIdAndToId(friend.getUserId(), friend.getFriendId());
        return friend;
    }

}
