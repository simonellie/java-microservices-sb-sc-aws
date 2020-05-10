package com.xantrix.webapp.repository;

import com.xantrix.webapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
	User findByUserId(String UserId);
}
