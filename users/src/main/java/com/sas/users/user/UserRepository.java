package com.sas.users.user;


import java.util.List;
import java.util.Optional;

import com.sas.clients.users.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, Integer> {

	Optional<User> findByEmail(String email);
	List<User> findByRoleContaining(String role);
	@Query("SELECT u.id FROM User u JOIN u.role r WHERE r = :role")
	List<Long> findUserIdsByRole(@Param("role") Role role);
}
