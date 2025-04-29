package com.example.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.model.User;

public interface UserRepository extends JpaRepository<User,Long>{

	public Optional<User> findByUsername(String userName);

}
