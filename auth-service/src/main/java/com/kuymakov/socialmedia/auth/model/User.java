package com.kuymakov.socialmedia.auth.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Set;

@Document(collection = "users")
@Data
@Builder
public class User {

    private @MongoId ObjectId id;
    private String username;
    private String password;
    private Set<Role> roles;

}