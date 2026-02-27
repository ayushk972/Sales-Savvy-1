package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.CartItem;

import jakarta.transaction.Transactional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Integer>{

	@Query("SELECT c FROM CartItem c WHERE  c.user.userid = :userid AND c.product.productId = :productId")
	Optional<CartItem> findByUserAndProduct(@Param("userid") int userid,@Param("productId") int productId);
	
	@Query("SELECT c FROM CartItem c JOIN FETCH c.product p LEFT JOIN FETCH ProductImages pi ON p.productId = pi.product.productId WHERE c.user.userid = :userid")
	List<CartItem> findCartItemsWithProductDetails(int userid);

	@Query("UPDATE CartItem c SET c.quantity = :quantity WHERE c.id = :cartItemId")
	void updateCartItemQuantity(int cartItemId, int quantity);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM CartItem c WHERE c.user.userid = :userid AND c.product.productId = :productId")
	void deleteCartItem(int userid, int productId);
	
	
	@Transactional
	@Query("SELECT COALESCE(SUM(c.quantity), 0) FROM CartItem c WHERE c.user.userid = :userid")
	int countTotalItems(int userid);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM CartItem c WHERE c.user.userid = :userid")
	void deleteAllCartItemsByUserId(int userid);

}
