package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer>{

	@Query("SELECT COUNT(c) FROM CartItem c WHERE c.user.id = :userId")
	int countCartItemsByUserId(@Param("userId") int userId);
	
	@Query("SELECT u.id FROM User u WHERE u.username = :username")
	int findUserIdByUserName(@Param("username") String username);
}
