package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.ProductImages;

import jakarta.transaction.Transactional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImages, Integer>{

	List<ProductImages> findByProduct_ProductId(Integer productId);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM ProductImages pi WHERE pi.product.productId = :productId")
	void deleteByProductId(@Param("productId") Integer productId);
	
}
