package com.sas.users.user;


import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Integer> {

	Optional<User> findByEmail(String email);
}
