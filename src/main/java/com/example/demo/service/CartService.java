package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductImages;
import com.example.demo.entity.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductImageRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;

@Service
public class CartService {

	@Autowired
	private CartRepository cartRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ProductImageRepository productImageRepo;
	
	//Get the total cart item count for a user
	public int getCartItemCount(int userid) {
		return cartRepo.countTotalItems(userid);
		
	}
	
	//Add an item to the cart
	public void addToCart(int userid, int productId, int quantity) {
		User user = userRepo.findById(userid)
				.orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userid));
		
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found with ID:" + productId));
		
		// Fetch cart items for this userid and ProductId
		Optional<CartItem> existingItem = cartRepo.findByUserAndProduct(userid, productId);
		
		if(existingItem.isPresent()) {
			CartItem cartItem = existingItem.get();
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
			cartRepo.save(cartItem);
		} else {
			CartItem newItem = new CartItem(user, product, quantity);
			cartRepo.save(newItem);
		}
		
	}
	
	//Get Cart Items for a User
	public Map<String, Object> getCartItems(int userid) {
		//Fetch thr cart items for the user with product details
		List<CartItem> cartItems = cartRepo.findCartItemsWithProductDetails(userid);
		
		//create a reponse map to hold the cart details
		Map<String, Object> response = new HashMap<>();
		User user = userRepo.findById(userid)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
		
		response.put("username", user.getUsername());
		response.put("role", user.getRole().toString());
		
		//List to hold the product details
		List<Map<String, Object>> products = new ArrayList<>();
		double overallTotalPrice = 0.0;
		
		for(CartItem cartItem : cartItems) {
			Map<String, Object> productDetails = new HashMap<>();
			
			//Get prodcut details
			Product product = cartItem.getProduct();
			
			//Fetch product images from the ProductImagesRepossitory
			List<ProductImages> productImages = productImageRepo.findByProduct_ProductId(product.getProduct_id());
			String imageUrl = null;
			
			if(productImages != null && !productImages.isEmpty()) {
				//if there are images, get thr first images URL
				imageUrl = productImages.get(0).getImageUrl();
			}
			
			//Populate product details into the map
			productDetails.put("product_id", product.getProduct_id());
			productDetails.put("image-url", imageUrl);
			productDetails.put("name", product.getName());
			productDetails.put("description", product.getDescription());
			productDetails.put("price_per_unit", product.getPrice());
			productDetails.put("quantity", cartItem.getQuantity());
			productDetails.put("total_price", cartItem.getQuantity()*product.getPrice().doubleValue());
			
			//Add the product details to the products list
			products.add(productDetails);
			
			//Add to the overall total price
			overallTotalPrice += cartItem.getQuantity() * product.getPrice().doubleValue();
			
		}
		
		// Prepare the final cart response
		Map<String, Object> cart = new HashMap<>();
		cart.put("products", products);
		cart.put("overall_total_price", overallTotalPrice);
		
		//Add the cart details to the response
		response.put("cart", cart);
		
		return response;
		
	}
	
	//Updates Cart Item Quantity
	public void updateCartItemQuantity(int userid, int productId, int quantity) {
		User user = userRepo.findById(userid)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
		
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));
		
		//fetch cart item for this userid and productid
		Optional<CartItem> existingItem = cartRepo.findByUserAndProduct(userid, productId);
		
		if(existingItem.isPresent()) {
			CartItem cartItem = existingItem.get();
			if(quantity == 0) {
				deleteCartItem(userid, productId);
			} else {
				cartItem.setQuantity(quantity);
				cartRepo.save(cartItem);
			}
		}
	}
	
	
	//Delete cart Item
	public void deleteCartItem(int userid, int productId) {
		User user = userRepo.findById(userid)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
		
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));
		
		cartRepo.deleteCartItem(userid, productId);
	}
	
	
	
	
}



























