package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.OrderStatus;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.transaction.Transactional;

@Service
public class PaymentService {

	@Value("${razorpay.key_id}")
	private String razorpayKeyId;
	
	@Value("${razorpay.key_secret}")
	private String razorpayKeySecret;
	
	private final OrderRepository orderRepo;
	private final OrderItemRepository orderItemRepo;
	private final CartRepository cartRepo;
	
	public PaymentService(OrderRepository orderRepo, OrderItemRepository orderItemRepo, CartRepository cartRepo) {
		this.orderRepo = orderRepo;
		this.orderItemRepo = orderItemRepo;
		this.cartRepo = cartRepo;
	}
	
	@Transactional
	public String createOrder(int userid, BigDecimal totalAmount, List<OrderItem> cartItems) 
	throws RazorpayException {
		
		//Create rozarpay client
		RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
		
		//Prepare Razorpay order request
		var orderRequest = new JSONObject();
		orderRequest.put("amount", totalAmount.multiply(BigDecimal.valueOf(100)).intValue()); //Amount in paise
		orderRequest.put("currency", "INR");
		orderRequest.put("receipt", "txn_" + System.currentTimeMillis());
		
		//Prepare Razorpay order
		com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);
		
		//Save order details in the database
		Order order = new Order();
		order.setOrderId(razorpayOrder.get("id"));
		order.setUserId(userid);
		order.setTotalAmount(totalAmount);
		order.setStatus(OrderStatus.PENDING);
		order.setCreatedAt(LocalDateTime.now());
		orderRepo.save(order);
		
		return razorpayOrder.get("id");
	}
	
	@Transactional
	public boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature, int userid) {
	try {
		//prepare signature validation attributes
		JSONObject attributes = new JSONObject();
		attributes.put("razorpay_order_id", razorpayOrderId);
		attributes.put("razorpay_payment_id", razorpayPaymentId);
		attributes.put("razorpay_signature", razorpaySignature);
		
		//Verify razorpay signature
		boolean isSignatureValid = com.razorpay.Utils.verifyPaymentSignature(attributes, razorpayKeySecret);
		
		if(isSignatureValid) {
			//Update order status to Success
			Order order = orderRepo.findById(razorpayOrderId)
					.orElseThrow(() -> new RuntimeException("Order not Found"));
			order.setStatus(OrderStatus.SUCCESS);
			order.setUpdatedAt(LocalDateTime.now());
			orderRepo.save(order);
		
		
		// Fetch cart items for the user
		List<CartItem> cartItems = cartRepo.findCartItemsWithProductDetails(userid);
		
		
		//Save Order items
		for(CartItem cartItem: cartItems) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProductId(cartItem.getProduct().getProduct_id());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setPricePerUnit(cartItem.getProduct().getPrice());
			orderItem.setTotalPrice(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
			orderItemRepo.save(orderItem);
		}
		
		// clear user's cart
		cartRepo.deleteAllCartItemsByUserId(userid);
		
		return true;
		} else {
			return false;
		}
		
		
		
	} catch (Exception e) {
		e.printStackTrace();
		return false;
	}	
	}
	
	@Transactional
	public void saveorderItems(String orderId, List<OrderItem> items) {
		Order order = orderRepo.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found"));
		
		
		for(OrderItem item: items) {
			item.setOrder(order);
			orderItemRepo.save(item);
		}
		
	}
	
	
	
}


























