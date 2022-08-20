package com.kuymakov.socialmedia.auth.repository;

import com.kuymakov.socialmedia.auth.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {

    @Query("{username:'?0'}")
    User findUserByUsername(String username);

    boolean existsByUsername(String username);
}