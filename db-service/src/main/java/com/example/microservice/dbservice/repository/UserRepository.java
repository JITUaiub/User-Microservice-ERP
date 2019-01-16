package com.example.microservice.dbservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.microservice.dbservice.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	public User findByUsername(String username);
}
 