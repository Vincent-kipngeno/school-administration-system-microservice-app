package com.sas.users.user;


import java.util.List;
import java.util.Optional;

import com.sas.clients.users.Role;
import com.sas.users.user.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends MongoRepository<User, Integer> {

	Optional<User> findByEmail(String email);
	List<User> findByRolesContaining(String role);
	@Query("SELECT u.id FROM User u JOIN u.roles r WHERE r = :role")
	List<Long> findUserIdsByRole(@Param("role") Role role);
}
