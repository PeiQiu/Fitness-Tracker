package com.example.demo.services;

import com.example.demo.domain.Sharings.SharingActiveData;
import com.example.demo.repositories.Sharing.SharingDataRepositoryMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SharingDataService {

    @Autowired
    private SharingDataRepositoryMongo sharingDataRepositoryMongo;

    public SharingActiveData findActiveDataById(String id){
        return sharingDataRepositoryMongo.findOne(id);
    }

    public SharingActiveData Save(SharingActiveData sharingActiveData){
        return sharingDataRepositoryMongo.save(sharingActiveData);
    }

    public SharingActiveData getActiveDataById(String id){
        return sharingDataRepositoryMongo.findOne(id);
    }
}
