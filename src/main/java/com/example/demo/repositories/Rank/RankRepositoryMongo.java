package com.example.demo.repositories.Rank;


import com.example.demo.domain.Rank.Rank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RankRepositoryMongo extends MongoRepository<Rank, String> {
    public List<Rank> findByAuthorIdInAndDateTime(List<String> authorIds, Long dateTime);

    public Rank findByAuthorIdAndDateTime(String authorId, long dateTime);
}
