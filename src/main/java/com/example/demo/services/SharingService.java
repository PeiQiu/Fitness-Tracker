package com.example.demo.services;

import com.example.demo.domain.CustomerUser;
import com.example.demo.domain.Friends.Friend;
import com.example.demo.domain.Sharings.Comment;
import com.example.demo.domain.Sharings.Sharing;
import com.example.demo.domain.Sharings.SharingActiveData;
import com.example.demo.domain.SpringPageable;
import com.example.demo.domain.User;
import com.example.demo.repositories.Sharing.SharingRepository;
import com.example.demo.repositories.Sharing.SharingRepositoryMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SharingService {

    @Autowired
    private SharingRepositoryMongo sharingRepositoryMongo;

    @Autowired
    private SharingRepository sharingRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SharingDataService sharingDataService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private CommentService commentService;

    private List<Sharing> findSharingByAutherId(String autherId,int state ){//Pageable pageable, long datetime, String content
        return sharingRepositoryMongo.findByAutherIdAndState(autherId, state);
//        return sharingRepository.findFilter(autherId, datetime, content, pageable);
    }

    private void configForAtherAndActiveData(Sharing sharing){
            User user = null;
            try {
                user = userService.loadUserByUserId(sharing.getAutherId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(user != null) user.setPassword(null);
            sharing.setAuthor(user);
            SharingActiveData sharingActiveData = sharingDataService.findActiveDataById(sharing.getActiveDataId());
            sharing.setActiveData(sharingActiveData);
    }

    private void configSharingComments(Sharing sharing){
        List<Comment> comments = commentService.findCommentsBySharingId(sharing.getId());
        sharing.setComments(comments);
    }
    public List<Sharing> findAllSharings(String uid, int pageNum){
//        List<Sharing> ans = new ArrayList<>();
        List<Friend> friends = friendService.getFriends(uid);
        List<String> conditions = new ArrayList<>();
        conditions.add(uid);
        for(Friend friend : friends){
//            System.out.println("^^^^^^^^^^^^^^^^ friend id : " + friend.getId() + "   friend username :: " + friend.getFriend().getUsername() + "  id :: " + friend.getFriend().getId());
            conditions.add(friend.getFriend().getId());
        }
        Pageable pageable = new PageRequest(pageNum, 3, Sort.Direction.DESC, "sharingDate");
        List<Sharing> sharings = sharingRepository.findByFriendsIdsAndState(conditions, 1, pageable);

//        List<Sharing> sharings =  findAllSharing(pageNum).getContent();
//        for(int i =0; i < sharings.size(); i ++) {
//            System.out.println("sharing size " + sharings.size());
//            Sharing sharing = sharings.get(i);
//            if( (uid.equals(sharing.getAutherId())|| varifyFriends(friends, sharing.getAutherId())) && sharing.getState() == 1){
//                configForAtherAndActiveData(sharing);
//                configSharingComments(sharing);
//                configSharingFollower(sharing);
//                ans.add(sharing);
//            }
//        }

        for(Sharing sharing : sharings){
            configForAtherAndActiveData(sharing);
            configSharingComments(sharing);
            configSharingFollower(sharing);
        }

//        System.out.println(" :::::::::::::: ++++++++++++++++++++++++ size : " + sharings.size());
        return sharings;
    }

    private boolean varifyFriends(List<Friend> friends, String sharingAutherId){
        for(Friend friend : friends){
//            System.out.println("friend id : " + friend.getFriend().getId() + " user name " + friend.getFriend().getUsername() + " sharing author id : " + sharingAutherId);
            if(friend.getFriend().getId().equals(sharingAutherId)){
                return true;
            }
        }
        return false;
    }

    public List<Sharing> findAllSharingsByAutherId(String autherid){ //int pageNum, long datetime, String content
//        Pageable pageable = new PageRequest(pageNum, 5, Sort.Direction.DESC, "sharingDate");
        List<Sharing> sharings = findSharingByAutherId(autherid, 1);//.getContent();pageable, datetime, content
        List<Sharing> ans = new ArrayList<>();
        for(Sharing sharing : sharings) {
            if(sharing.getState() == 1){
                configForAtherAndActiveData(sharing);
                configSharingComments(sharing);
                configSharingFollower(sharing);
                ans.add(sharing);
            }

        }
        return ans;
    }

    public Sharing getOneSharing(String id){
        Sharing sharing = sharingRepositoryMongo.findOne(id);
        if(sharing.getState() != 1){
            sharing = null;
        }
        if(sharing != null) {
            configForAtherAndActiveData(sharing);
            configSharingFollower(sharing);
            configSharingComments(sharing);
        }
        return sharing;
    }

    public Sharing addOrdeleteOneFollower(String sharingId, String uid){
        Sharing sharing = getOneSharing(sharingId);
        if(sharing == null){
            return sharing;
        }else if(sharing.getFollow() == null){
            sharing.setFollow(new ArrayList<String>());
        }
        List<String> follows = sharing.getFollow();
        if(follows.contains(uid)){
            follows.remove(uid);
        }else{
            follows.add(uid);
        }
        configSharingFollower(sharing);
        sharingRepositoryMongo.save(sharing);
        return sharing;
    }

    private void configSharingFollower(Sharing sharing){
        if(sharing.getFollow() == null){
            sharing.setFollow( new ArrayList<String>() );
        }
        List<String> followsIds = sharing.getFollow();
        List<CustomerUser> followers = new ArrayList<>();
        for(String uid : followsIds){
            User user = null;
            try {
                user = userService.loadUserByUserId(uid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(user == null) {
                continue;
            }
            CustomerUser customerUser = new CustomerUser(user);
            followers.add(customerUser);
        }
        sharing.setFollowers(followers);
    }

    public Sharing deleteSharing(String sharingid){
        Sharing sharing = sharingRepositoryMongo.findOne(sharingid);
        sharing.setState(0);
//        List<> = commentService.findCommenBySharingId();
        return sharingRepositoryMongo.save(sharing);
    }

    public Sharing saveOneSharing(Sharing sharing){
        return sharingRepositoryMongo.save(sharing);
    }



    private Page<Sharing> findAllSharing(int pageNum){
        Pageable pageable = new PageRequest(pageNum, 3, Sort.Direction.DESC, "sharingDate");
        return sharingRepositoryMongo.findAll(pageable);
    }


    private SpringPageable getSharingpageable(){
        SpringPageable pageable = new SpringPageable();
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "sharingDate"));
        pageable.setPagesize(10);
        pageable.setSort(sort);
        return pageable;
    }

}
