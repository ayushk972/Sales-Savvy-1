package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.JWTToken;

public interface JWTTokenRepository extends JpaRepository<JWTToken, Integer>{
	
	
	@Query("SELECT t FROM JWTToken t WHERE t.user.userid = :userid")
	JWTToken findByUserid(@Param("userid") int userid);

	Optional<JWTToken> findByToken(String token);
}
