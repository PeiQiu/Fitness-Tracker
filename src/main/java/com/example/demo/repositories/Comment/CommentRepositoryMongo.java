package com.example.demo.repositories.Comment;


import com.example.demo.domain.Sharings.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepositoryMongo extends MongoRepository<Comment, String>{

    public List<Comment> findBySharingIdAndStateOrderByCommentDateDesc(String sharingId, Integer state);

    public List<Comment> findByParentCommentIdAndStateOrderByCommentDateDesc(String parentCommentId, Integer state);


}
