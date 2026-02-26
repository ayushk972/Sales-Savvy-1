package com.example.demo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="productimages")
public class ProductImages {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer image_id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="product_id", nullable = false)
	private Product product;
	@Column(name = "image_url",nullable = false, columnDefinition = "TEXT")
	private String imageUrl;
	public ProductImages() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProductImages(Integer image_id, Product product, String imageUrl) {
		super();
		this.image_id = image_id;
		this.product = product;
		this.imageUrl = imageUrl;
	}
	public ProductImages(Product product, String imageUrl) {
		super();
		this.product = product;
		this.imageUrl = imageUrl;
	}
	public Integer getImage_id() {
		return image_id;
	}
	public void setImage_id(Integer image_id) {
		this.image_id = image_id;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	
	
	
}
