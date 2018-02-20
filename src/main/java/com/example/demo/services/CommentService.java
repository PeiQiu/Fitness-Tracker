package com.example.demo.services;

import com.example.demo.domain.CustomerUser;
import com.example.demo.domain.Sharings.Comment;
import com.example.demo.domain.User;
import com.example.demo.repositories.Comment.CommentRepositoryMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepositoryMongo commentRepositoryMongo;

    @Autowired
    private UserService userService;

    private List<Comment> getParentCommentBySharingId(String sharingId, int state){
        //private Long commentDate
        return commentRepositoryMongo.findBySharingIdAndStateOrderByCommentDateDesc(sharingId, state);
    }

    private List<Comment> getReplyCommentByCommentId(String commentId, int state){
        return commentRepositoryMongo.findByParentCommentIdAndStateOrderByCommentDateDesc(commentId, state);
    }

    private void buildReplyComment(Comment comment, List<Comment> replys){
        int state = 1;
        List<Comment> replyComments = getReplyCommentByCommentId(comment.getId(), state);
        replys.addAll(replyComments);
        for(Comment replycomment : replyComments){
            String replyCommentCustomerUsername = replycomment.getCustomerUsername();
            // get the reply customer info
            User replyUser = userService.loadUserByUsername(replyCommentCustomerUsername);
            if(replyUser != null) replyUser.setPassword(null);
            // reply for parent customer user
            CustomerUser parentCustomerUser = comment.getCustomerUser();
            // who create this comment
            replycomment.setCustomerUser(replyUser);
            //reply for who
            replycomment.setReplyCustomerUser(parentCustomerUser);
            buildReplyComment(replycomment, replys);
        }
    }

    public Comment saveOneComment(Comment comment, String originalId){
        Comment c = commentRepositoryMongo.save(comment);
        if(originalId == null){
            originalId = c.getId();
        }
        return getOneCommentById( originalId );
    }

    public Comment getOneCommentById(String id){
        Comment comment =  commentRepositoryMongo.findOne(id);
        List<Comment> replys = new ArrayList<>();
        comment.setReplyComments(replys);
        String csm = comment.getCustomerUsername();
        User user = userService.loadUserByUsername(csm);
        if(user != null) user.setPassword(null);
        comment.setCustomerUser(user);
        buildReplyComment(comment, replys);
        return comment;
    }

    public Comment deleteOneComment(String commentId){
        Comment comment = commentRepositoryMongo.findOne(commentId);
        comment.setState(0);
        return commentRepositoryMongo.save(comment);
    }



    public List<Comment> findCommentsBySharingId(String sharingId){
        int state = 1;
        List<Comment> comments = getParentCommentBySharingId(sharingId, 1);
        for(Comment comment : comments){
            List<Comment> replys = new ArrayList<>();
            comment.setReplyComments(replys);
            String customerUsername = comment.getCustomerUsername();
            User user = userService.loadUserByUsername(customerUsername);
            if(user != null) user.setPassword(null);
            comment.setCustomerUser(user);
            buildReplyComment(comment, replys);
//            System.out.println(" *************username "+ comment.getCustomerUsername()+"--===---== parent customer size : " + comment.getReplyComments().size());
        }
        return comments;
    }

}
