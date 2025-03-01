package com.ecommerce.userService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.userService.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	public Optional<User> findByUsername(String userName);
	
	public Boolean existsByUsername(String username);
	
	public Boolean existsByEmail(String username);
}
