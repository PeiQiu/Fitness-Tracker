package com.example.demo.repositories.User;


import com.example.demo.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;

import java.util.List;

public interface UserRepository {
    public List<User> findFiltered(String keyWord);
}
