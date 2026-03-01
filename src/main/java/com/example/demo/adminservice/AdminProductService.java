package com.example.demo.adminservice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductImages;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductImageRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class AdminProductService {

	private final ProductRepository productRepo;
	private final ProductImageRepository productImageRepo;
	private final CategoryRepository categoryRepo;
	
	
	public AdminProductService(ProductRepository productRepo, ProductImageRepository productImageRepo, CategoryRepository categoryRepo) {
		this.productRepo = productRepo;
		this.productImageRepo = productImageRepo;
		this.categoryRepo = categoryRepo;
	}
	
	public Product addProductWithImage(String name, String description, Double price, Integer stock, Integer categoryId, String imageUrl) {
		// Validate the category
		Optional<Category> category = categoryRepo.findById(categoryId);
		if(category.isEmpty()) {
			throw new IllegalArgumentException("Innvalid category ID");
		}
		
		// create and save the product
		Product product = new Product();
		product.setName(name);
		product.setDescription(description);
		product.setPrice(BigDecimal.valueOf(price));
		product.setStock(stock);
		product.setCategory(category.get());
		product.setCreatedAt(LocalDateTime.now());
		product.setUpdatedAt(LocalDateTime.now());
		
		Product saveproduct = productRepo.save(product);
		
		// create and save the product image
		if(imageUrl != null && !imageUrl.isEmpty()) {
			ProductImages productImage = new ProductImages();
			productImage.setProduct(saveproduct);
			productImage.setImageUrl(imageUrl);
			productImageRepo.save(productImage);
			
		} else {
			throw new IllegalArgumentException("Product image URL cannot be empty");
		}
		
		return saveproduct;
	}
	
	public void daleteProduct(Integer productId) {
		// Check if the product exists
		if(!productRepo.existsById(productId)) {
			throw new IllegalArgumentException("Product not found");
		}
		
		//Delete associated product images
		productImageRepo.deleteByProductId(productId);
		
		//Delete the product
		productRepo.deleteById(productId);
	}
}





















