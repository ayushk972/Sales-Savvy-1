package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductImages;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductImageRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {

	private ProductRepository productRepo;
	
	private ProductImageRepository productImageRepo;
	
	private CategoryRepository categoryRepo;
	
	public List<Product> getProductsByCategory(String categoryName) {
		if(categoryName != null && !categoryName.isEmpty()) {
			Optional<Category> categoryOpt = categoryRepo.findByCategoryName(categoryName);
			if(categoryOpt.isPresent()) {
				Category category = categoryOpt.get();
				return productRepo.findByCategory_CategoryId(category.getCategory_id());
			} else {
				throw new RuntimeException("Category not found");
			}
		} else {
			return productRepo.findAll();
		}
	}
	
	public List<String> getProductImages(Integer productId) {
		List<ProductImages> productImages = productImageRepo.findByProduct_ProductId(productId);
		List<String> imagesUrls = new ArrayList<>();
		for(ProductImages images : productImages) {
			imagesUrls.add(images.getImageUrl());
		}
		return imagesUrls;
	}
}
