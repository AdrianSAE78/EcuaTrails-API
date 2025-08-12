package com.ecuatrails.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecuatrails.api.model.User;

public interface UserRepository extends JpaRepository <User, Integer> {
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	Optional<User> findByUid(String uid);
	boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
