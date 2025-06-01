package com.wazzups.analytics.userapi.repository;

import com.wazzups.analytics.userapi.model.User;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepository {

    private final ConcurrentMap<UUID, User> storage = new ConcurrentHashMap<>();

    public void saveAll(List<User> users) {
        for (User user : users)
            storage.put(user.getId(), user);
    }

    public List<User> findAll() {
        return storage.values().stream().toList();
    }

    public int count() {
        return storage.size();
    }

    public void clean() {
        storage.clear();
    }
}
