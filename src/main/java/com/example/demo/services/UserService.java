package com.example.demo.services;

import com.example.demo.domain.Friends.Friend;
import com.example.demo.domain.Friends.FriendRequest;
import com.example.demo.domain.User;
import com.example.demo.repositories.Friend.FriendRepositoryMongo;
import com.example.demo.repositories.User.UserRepository;
import com.example.demo.repositories.User.UserRepositoryMongo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peiqiutian on 17/07/2017.
 */
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepositoryMongo userRepository;

    @Autowired
    private UserRepository userQuery;

    @Autowired
    private FriendRequestService requestService;


   // @Override
    public User loadUserByUsername(String username){
        User user = userRepository.findByUsername(username);
        //user.setPassword(null);
        return user;
    }

    public User loadUserByUserId(String id) throws Exception {
        User user = userRepository.findOne(id);
        if(user == null) throw new Exception("Id not exists");
        //user.setPassword(null);
        return user;
    }

    public User LoadUserByEmail(String email){
        User user = userRepository.findByEmail(email);
        return user;
    }

    public User device(String uid, String device, String accesscode, long timelen) throws Exception {
        User user = loadUserByUserId(uid);
        user.setDevice(device);
        user.setAccessCode(accesscode);
        user.setAccessCodeStartTime(timelen);
        return Save(user);
    }

    public User Save(User user){
        return userRepository.save(user);
    }

    public List<User> findAll(){
        List<User> users = userRepository.findAll();
        for(User user : users){
            user.setPassword(null);
        }
        return users;
    }

    private boolean varifyContainFriendRequest(String uid, List<FriendRequest> requests){
        for(FriendRequest friendRequest : requests){
            if(friendRequest.getFromCustomerUserId().equals(uid) || friendRequest.getToCustomerUserId().equals(uid)){
                return true;
            }
        }
        return false;
    }
    public List<User> findByFilter(String uid, String keyWord){
        List<User> users = userQuery.findFiltered(keyWord);
        List<FriendRequest> friendRequests = requestService.getFriendRequestsFromOneUser(uid);
        List<User> ans = new ArrayList<>();
        for(int i =0; i < users.size(); i ++){
//            System.out.println("one such friend :: " + users.get(i).getUsername() + " each user id : " + users.get(i).getId() + " uid : " + uid);
            User user = users.get(i);
            user.setPassword(null);
            if( !user.getId().equals(uid) && !varifyContainFriendRequest(user.getId(), friendRequests)){
                ans.add(user);
            }
        }
        return ans;
    }
}
