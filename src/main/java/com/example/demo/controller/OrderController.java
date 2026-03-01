package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.repository.JWTTokenRepository;
import com.example.demo.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:5174", allowCredentials = "true")
@RequestMapping("/api/orders")
public class OrderController {

    private final JWTTokenRepository JWTTokenRepository;

	@Autowired
	private OrderService orderService;

    OrderController(JWTTokenRepository JWTTokenRepository) {
        this.JWTTokenRepository = JWTTokenRepository;
    }
	
	
//	public ResponseEntity<Map<String, Object>> getOrderForUser(HttpServletRequest request) {
//		try {
//			//Retrive the authenticated user from the request
//			User authenticatedUser = (User) request.getAttribute("authenticatedUser");
//			
//			//Handle unauthenticated requests
//			if(authenticatedUser == null) {
//				return ResponseEntity.status(401).body(Map.of("error", "User not authenticated"));
//			}
//			
//			//fetch orders for the user via the service layer
//			Map<String, Object> response = orderService.getOrdersForUser(authenticatedUser);
//			
//			//Return the responsee with HTTP 200 ok
//			return ResponseEntity.ok(response);
//			
//			
//			
//			
//			
//			
//		} catch (IllegalArgumentException e) {
//			//Handlee cases where user details are invalid or missing
//			return  ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
//		}
//		catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			return ResponseEntity.status(500).body(Map.of("error", "An unexpected error occured"));
//		}
//	}
	
	
	
	
	/**
     * GET /api/orders/myorders
     * Returns all orders for the currently logged-in user.
     * Authentication filter sets "authenticatedUser" attribute.
     */
    @GetMapping("/myorders")
    public ResponseEntity<?> getMyOrders(HttpServletRequest request) {
        try {
            User user = (User) request.getAttribute("authenticatedUser");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Unauthorized");
            }
            List<Map<String, Object>> orders = orderService.getOrdersForUser(user.getUserid());
            return ResponseEntity.ok(orders);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch orders: " + e.getMessage());
        }
    }

    /**
     * GET /api/orders/{orderId}
     * Returns full detail of a single order including all items.
     * User can only access their own orders.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable String orderId,
                                          HttpServletRequest request) {
        try {
            User user = (User) request.getAttribute("authenticatedUser");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Unauthorized");
            }
            Map<String, Object> order = orderService.getOrderById(orderId, user.getUserid());
            return ResponseEntity.ok(order);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch order: " + e.getMessage());
        }
    }
}








