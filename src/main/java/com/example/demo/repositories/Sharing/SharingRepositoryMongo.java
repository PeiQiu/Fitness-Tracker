package com.example.demo.repositories.Sharing;


import com.example.demo.domain.Sharings.Sharing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SharingRepositoryMongo extends MongoRepository<Sharing, String>{

    public Page<Sharing> findAll(Pageable pageable);

    public List<Sharing> findByAutherIdAndState(String autherId, int state);
}
