package com.xantrix.webapp.userwebservice.repository;

import com.xantrix.webapp.userwebservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
	User findByUserId(String UserId);
}
