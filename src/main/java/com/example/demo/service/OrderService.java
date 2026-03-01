package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.controller.ProductController;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductImages;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductImageRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class OrderService {

    private final ProductController productController;

	@Autowired
	private OrderItemRepository orderItemRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ProductImageRepository productImageRepo;
	
	@Autowired
	private final OrderRepository orderRepo;


    OrderService(ProductController productController, OrderRepository orderRepo) {
        this.productController = productController;
		this.orderRepo = orderRepo;
	
    }
	
	
//	public Map<String, Object> getOrdersForUser(Integer integer) {
//		//Fetch all successful order items for the user
//		List<OrderItem> orderItems = orderItemRepo.findSuccessfulOrderItemsByUserid(integer.getUserid());
//		
//		
//		//Prepare the response map
//		Map<String, Object> response = new HashMap<>();
//		response.put("username", integer.getUsername());
//		response.put("role", integer.getRole());
//		
//		//transform oorder items into a list of product detasils
//		List<Map<String, Object>> products = new ArrayList<>();
//		for(OrderItem item : orderItems) {
//			Product product = productRepo.findById(item.getProductId()).orElse(null);
//			if(product == null) {
//				continue;
//			}
//			
//			//Fetch the product images(if avaialable)
//			List<ProductImages> images = productImageRepo.findByProduct_ProductId(product.getProduct_id());
//			String imageUrl = images.isEmpty() ? null : images.get(0).getImageUrl();
//			
//			
//			//create a product details map
//			Map<String, Object> productDetails = new HashMap<>();
//			productDetails.put("order_id", item.getOrder().getOrderId());
//			productDetails.put("quantity", item.getQuantity());
//			productDetails.put("total_price", item.getTotalPrice());
//			productDetails.put("image_url", imageUrl);
//			productDetails.put("product_id", product.getProduct_id());
//			productDetails.put("name", product.getName());
//			productDetails.put("description", product.getDescription());
//			productDetails.put("price_per_unit", item.getPricePerUnit());
//			
//			products.add(productDetails);
//		}
//		
//		//Add the product list to the response
//		response.put("products", products);
//		
//		return response;
//	}
    
    
    
    /**
     * GET /api/orders/myorders
     * Returns all orders (with items) for the authenticated user.
     */
    public List<Map<String, Object>> getOrdersForUser(int userId) {
        List<Order> orders = orderRepo.findByUserId(userId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Order order : orders) {
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("orderId",     order.getOrderId());
            orderMap.put("totalAmount", order.getTotalAmount());
            orderMap.put("status",      order.getStatus().name());
            orderMap.put("createdAt",   order.getCreatedAt());
            orderMap.put("updatedAt",   order.getUpdatedAt());

            // Fetch items and enrich with product name + image
            List<OrderItem> items = orderItemRepo.findByOrderId(order.getOrderId());
            List<Map<String, Object>> itemMaps = buildItemMaps(items);
            orderMap.put("orderItems", itemMaps);

            result.add(orderMap);
        }
        return result;
    }
	
	
	/**
     * GET /api/orders/{orderId}
     * Returns a single order's full detail including items.
     */
    public Map<String, Object> getOrderById(String orderId, int userId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        // Security: user can only see their own orders
        if (order.getUserId() != userId) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }

        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("orderId",     order.getOrderId());
        orderMap.put("totalAmount", order.getTotalAmount());
        orderMap.put("status",      order.getStatus().name());
        orderMap.put("createdAt",   order.getCreatedAt());
        orderMap.put("updatedAt",   order.getUpdatedAt());

        List<OrderItem> items = orderItemRepo.findByOrderId(orderId);
        orderMap.put("orderItems", buildItemMaps(items));

        return orderMap;
    }

    /**
     * Build enriched item maps with product name and image URL.
     */
    private List<Map<String, Object>> buildItemMaps(List<OrderItem> items) {
        List<Map<String, Object>> itemMaps = new ArrayList<>();
        for (OrderItem item : items) {
            Map<String, Object> im = new HashMap<>();
            im.put("productId",    item.getProductId());
            im.put("quantity",     item.getQuantity());
            im.put("pricePerUnit", item.getPricePerUnit());
            im.put("totalPrice",   item.getTotalPrice());

            // Enrich with product name and first image URL
            try {
                Product product = productRepo.findById(item.getProductId()).orElse(null);
                if (product != null) {
                    im.put("productName", product.getName());
                    // Get first image URL for thumbnail
                    String imageUrl = productImageRepo.findFirstImageUrlByProductId(item.getProductId());
                    im.put("imageUrl", imageUrl);
                } else {
                    im.put("productName", "Product #" + item.getProductId());
                    im.put("imageUrl", null);
                }
            } catch (Exception e) {
                im.put("productName", "Product #" + item.getProductId());
                im.put("imageUrl", null);
            }

            itemMaps.add(im);
        }
        return itemMaps;
    }
	
	
	
	
}
































