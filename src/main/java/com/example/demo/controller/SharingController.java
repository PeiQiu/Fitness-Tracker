package com.example.demo.controller;

import com.example.demo.domain.Friends.Friend;
import com.example.demo.domain.Friends.FriendRequest;
import com.example.demo.domain.Rank.Rank;
import com.example.demo.domain.Sharings.Comment;
import com.example.demo.domain.Sharings.CommentParam;
import com.example.demo.domain.Sharings.Sharing;
import com.example.demo.domain.Sharings.SharingActiveData;
import com.example.demo.domain.User;
import com.example.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/fitnesstracker/api/v1")
@PreAuthorize("hasPermission(#uid, 'Role', 'ROLE_USER')")
public class SharingController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private SharingDataService sharingDataService;

    @Autowired
    private SharingService sharingService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private FriendRequestService requestService;

    @Autowired
    private RankService rankService;

    //--------------get all friend posts ------------
    @RequestMapping(value = "/user/{uid}/sharings/friends", method = RequestMethod.GET)
    public List<Sharing> getAllRelatedSharings(@PathVariable String uid, @RequestParam("page") int page){ //Authentication auth, Principal principal,
        return sharingService.findAllSharings(uid , page);
    }

    // ------------- operation on owner posts -------------
    @RequestMapping(value= "/user/{uid}/sharings", method = RequestMethod.POST)
    public Sharing SaveNewSharing(@PathVariable String uid, @RequestBody Sharing sharing){
        sharing.setFollow(new ArrayList<String>());
        return sharingService.saveOneSharing(sharing);
    }

    @RequestMapping(value = "/user/{uid}/sharings", method = RequestMethod.GET)
    public List<Sharing> getAllSharingsByUserId(@PathVariable String uid){ // @RequestParam("page") int page, @RequestParam("selectDate") Long selectDate, @RequestParam("content") String content
//        System.out.println("uid : " + uid + " -- page num : " + page + " -- selectDate : " + selectDate + " --- content : " + content);
        return sharingService.findAllSharingsByAutherId(uid);
//        return null;
    }

    @RequestMapping(value = "/user/{uid}/sharings/{sid}", method = RequestMethod.GET)
    public Sharing findOneSharing(@PathVariable String uid, @PathVariable String sid){
//        System.out.println("user id : " + uid + "sharing id : " + sid);
        return sharingService.getOneSharing(sid);
//        return null;
    }

    @RequestMapping(value = "/user/{uid}/sharings/{sid}", method = RequestMethod.DELETE)
    public Sharing deleteOneSharing(@PathVariable String uid, @PathVariable String sid){
        return sharingService.deleteSharing(sid);
    }

    // -----------------add or delete the like of this sharing fo user.id
    @RequestMapping(value = "/user/{uid}/sharings/{sid}", method = RequestMethod.PUT)
    public Sharing addOrdeleteOneLikerToSharing(@PathVariable String uid, @PathVariable String sid){
        return sharingService.addOrdeleteOneFollower(sid, uid);
    }




    @RequestMapping(value = "/user/{uid}/activedata", method = RequestMethod.POST)
    public SharingActiveData SaveOneActiveData(@PathVariable String uid, @RequestBody SharingActiveData sharingActiveData){
        return sharingDataService.Save(sharingActiveData);
    }

    @RequestMapping(value = "/user/{uid}/activedata/{aid}", method = RequestMethod.GET)
    public SharingActiveData getActiveData(@PathVariable String uid, @PathVariable String aid){
        return sharingDataService.findActiveDataById(aid);
    }


    @RequestMapping(value = "/user/{uid}/sharings/{sid}/comments", method = RequestMethod.GET)
    public List<Comment> getThatSharingAllComments(@PathVariable String uid, @PathVariable String sid){
        return commentService.findCommentsBySharingId(sid);
    }

    @RequestMapping(value="/user/{uid}/sharings/{sid}/comments", method = RequestMethod.POST)
    public Comment createNewSharingsComment(@PathVariable String uid, @PathVariable String sid, @RequestBody CommentParam commentParam){
        Comment comment = new Comment.Builder()
                .commentDate(new Date().getTime()).parentCommentId(commentParam.getParentCommentId())
                .content(commentParam.getContent()).customerUsername(commentParam.getCustomerUsername())
                .sharingId(commentParam.getSharingId()).state(1).build();
        return commentService.saveOneComment(comment, null);
    }

    @RequestMapping(value="/user/{uid}/sharings/{sid}/comments/{cid}", method = RequestMethod.POST)
    public Comment createReplyComment(@PathVariable String uid, @PathVariable String sid, @PathVariable String cid, @RequestBody CommentParam commentParam){
        Comment comment = new Comment.Builder()
                .commentDate(new Date().getTime()).parentCommentId(commentParam.getParentCommentId())
                .content(commentParam.getContent()).customerUsername(commentParam.getCustomerUsername())
                .sharingId(commentParam.getSharingId()).state(1).build();
        return commentService.saveOneComment(comment, cid);
    }

    @RequestMapping(value="/user/{uid}/sharings/{sid}/comments/{cid}", method = RequestMethod.GET)
    public Comment getOneComment(@PathVariable String uid, @PathVariable String sid, @PathVariable String cid){
        return commentService.getOneCommentById(cid);
    }

    @RequestMapping(value = "/user/{uid}/sharings/{sid}/comments/{cid}", method = RequestMethod.DELETE)
    public Comment DeleteOneComment(@PathVariable String uid, @PathVariable String sid, @PathVariable String cid){
        return commentService.deleteOneComment(cid);
    }



    @RequestMapping(value = "/user/{uid}/ranks", method = RequestMethod.GET)
    public List<Rank> getAllFriendsRanks(@PathVariable String uid) throws Exception { //@RequestParam("page") int pageNum
        return rankService.getFriendsRank(uid);
    }

    @RequestMapping(value = "/user/{uid}/ranks/{rid}", method = RequestMethod.PUT)
    public Rank updateOneRankLike(@PathVariable String uid, @PathVariable String rid){
        return rankService.updateOneRankToLike(uid, rid);
    }

    @RequestMapping(value= "/user/{uid}/ranks/{rid}", method = RequestMethod.GET)
    public Rank getOneRankSummaryDetails(@PathVariable String uid, @PathVariable String rid) throws Exception {
        return rankService.getRankSummaryDetails(uid, rid);
    }


    @RequestMapping(value = "/user/{uid}/requests", method = RequestMethod.GET)
    public List<FriendRequest> getAllFriendRequests(@PathVariable String uid){
        return requestService.findFriendRequestbyToCustomerUserId(uid, false);
    }

    @RequestMapping(value = "/user/{uid}/requests", method = RequestMethod.POST)
    public FriendRequest cearteOneFriendRequest(@PathVariable String uid, @RequestBody String toUserid){
        if(toUserid == null){
            return null;
        }
        FriendRequest friendRequest = new FriendRequest.Builder()
                .dataTime(new Date().getTime()).fromCustomerId(uid).toCustomerId(toUserid).status(false).build();
        return requestService.saveOneFriendRequest(friendRequest);
    }

    @RequestMapping(value = "/user/{uid}/requests/{rid}", method = RequestMethod.PUT)
    public FriendRequest acceptFriendRequestAndCreateOneFriend(@PathVariable String uid, @PathVariable String rid){
        FriendRequest friendRequest = requestService.findOneFriendReuqestById(rid);
//        System.out.println("create friend : " + uid + " ? user id : "  + friendRequest.getFromCustomerUserId() + " friend id : " + friendRequest.getToCustomerUserId());
        Friend friend = new Friend.Builder()
                .dataTime(new Date().getTime()).friendId(friendRequest.getFromCustomerUserId()).userId(friendRequest.getToCustomerUserId()).build();
        friendService.SaveOneFriend(friend);
        return requestService.updateOneFriendRequestStatus(rid);
    }

    @RequestMapping(value = "/user/{uid}/friends", method = RequestMethod.GET)
    public List<Friend> getAllFriend(@PathVariable String uid) {
            return friendService.getFriends(uid);
    }

    @RequestMapping(value="/user/{uid}/friends/{fid}", method = RequestMethod.PUT)
    public Friend updateOneFriendContactDataTime(@PathVariable String uid, @PathVariable String fid){
        return friendService.updateFriendContactDataTime(fid);
    }

    @RequestMapping(value = "/user/{uid}/friends/{fid}", method = RequestMethod.DELETE)
    public Friend deletOneFriend(@PathVariable String uid, @PathVariable String fid){
        return friendService.deleteOneFriend(fid);
    }

}
