package com.example.demo.repositories.Sharing;

import com.example.demo.domain.Sharings.SharingActiveData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SharingDataRepositoryMongo extends MongoRepository<SharingActiveData, String>{

}
