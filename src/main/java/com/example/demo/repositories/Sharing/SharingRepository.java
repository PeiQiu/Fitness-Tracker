package com.example.demo.repositories.Sharing;

import com.example.demo.domain.Sharings.Sharing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by peiqiutian on 30/11/2017.
 */
public interface SharingRepository {

    public List<Sharing> findFilter(String autherId, Long dateTime, String content, Pageable pageable);

    public List<Sharing> findByFriendsIdsAndState(List<String> friends, int state, Pageable pageable);
}
