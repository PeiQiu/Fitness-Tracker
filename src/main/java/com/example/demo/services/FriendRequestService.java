package com.example.demo.services;

import com.example.demo.domain.Friends.Friend;
import com.example.demo.domain.Friends.FriendRequest;
import com.example.demo.domain.User;
import com.example.demo.repositories.Friend.FriendRequestRepositoryMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendRequestService {

    @Autowired
    private FriendRequestRepositoryMongo friendRequestRepositoryMongo;

    @Autowired
    private UserService userService;

    private void configCustomerUser(FriendRequest friendRequest){
        User user = null;
        try {
             user = userService.loadUserByUserId(friendRequest.getFromCustomerUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(user != null){
            user.setPassword(null);
        }
        friendRequest.setFromCustomerUser( user );
    }

    public FriendRequest saveOneFriendRequest(FriendRequest friendRequest){
        return friendRequestRepositoryMongo.save(friendRequest);
    }

    public List<FriendRequest> findFriendRequestbyToCustomerUserId(String toId, boolean status){
        List<FriendRequest> friendRequests = friendRequestRepositoryMongo.findByToCustomerUserIdAndStatus(toId, status, new Sort(new Sort.Order(Sort.Direction.DESC,"dataTime")));
        for(FriendRequest friendRequest : friendRequests){
            configCustomerUser(friendRequest);
        }
        return friendRequests;
    }

    public FriendRequest deleteOneFriendRequest(String friendReqId){
        FriendRequest friendRequest = friendRequestRepositoryMongo.findOne(friendReqId);
        friendRequestRepositoryMongo.delete(friendReqId);
        return friendRequest;
    }

    public FriendRequest findOneFriendReuqestById(String id){
        return friendRequestRepositoryMongo.findOne(id);
    }

    public FriendRequest updateOneFriendRequestStatus(String id){
        FriendRequest friendRequest = friendRequestRepositoryMongo.findOne(id);
        friendRequest.setStatus(true);
        return friendRequestRepositoryMongo.save(friendRequest);
    }

    public void deleteFriendRequestByFromIdAndToId(String uid, String fid){
        FriendRequest friendRequest =  friendRequestRepositoryMongo.findByFromCustomerUserIdAndToCustomerUserId(uid, fid);
        if(friendRequest == null){
            friendRequest = friendRequestRepositoryMongo.findByFromCustomerUserIdAndToCustomerUserId(fid, uid);
        }
        friendRequestRepositoryMongo.delete(friendRequest.getId());
    }

    public List<FriendRequest> getFriendRequestsFromOneUser(String uid){
        return friendRequestRepositoryMongo.findByFromCustomerUserIdOrToCustomerUserId(uid, uid);
    }
}
