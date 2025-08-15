package com.ecuatrails.api.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecuatrails.api.model.User;

public interface UserRepository extends JpaRepository <User, Integer> {
	User findByUsername(String username);
	Optional<User> findByEmail(String email);
	Optional<User> findByUid(String uid);
	boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    @Query("""
    	      select h.route.routeId
    	      from UserHistoryRoute h
    	      where h.user.userId = :userId
    	        and h.isFinished = true
    	        and h.routeDate >= :cutoff
    	    """)
    	    List<Integer> findFinishedRouteIdsSince(
    	        @Param("userId") Integer userId,
    	        @Param("cutoff") LocalDateTime cutoff
    	    );
}
