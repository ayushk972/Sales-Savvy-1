package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="categories")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Integer categoryId;
	@Column(nullable = false, unique = true)
	private String categoryName;
	public Category() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Category(Integer category_id, String categoryName) {
		super();
		this.categoryId = category_id;
		this.categoryName = categoryName;
	}
	public Category(String categoryName) {
		super();
		this.categoryName = categoryName;
	}
	public Integer getCategory_id() {
		return categoryId;
	}
	public void setCategory_id(Integer category_id) {
		this.categoryId = category_id;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	
}
