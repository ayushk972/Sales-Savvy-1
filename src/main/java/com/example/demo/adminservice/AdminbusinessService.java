package com.example.demo.adminservice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.OrderStatus;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class AdminbusinessService {

	private final OrderRepository orderRepo;
	private final OrderItemRepository orderItemRepo;
	private final ProductRepository productRepo;
	
	public AdminbusinessService(OrderRepository orderRepo, OrderItemRepository orderItemRepo, ProductRepository productRepo) {
		this.orderRepo = orderRepo;
		this.orderItemRepo = orderItemRepo;
		this.productRepo = productRepo;
	}
	
	public Map<String, Object> calculateMonthlyBusiness(int month, int years) {
		List<Order> successfulOrders = orderRepo.findSuccessfulOrdersByMonthAndYear(month, years);
		return calcalculateBusinessMetrics(successfulOrders);
	}
	
	public Map<String, Object> calculateDailyBusiness(LocalDate date) {
		List<Order> successfulOrders = orderRepo.findSuccessfulOrdersByDate(date);
		return calcalculateBusinessMetrics(successfulOrders);
	}
	
	public Map<String, Object> calculateYearlyBusiness(int year) {
		List<Order> successfulOrders = orderRepo.findSuccessOrderByYear(year);
		return calcalculateBusinessMetrics(successfulOrders);
	}
	
	public Map<String, Object> calculateOverallBusiness() {
		List<Order> successfulOrders = orderRepo.findByStatus(OrderStatus.SUCCESS);
		BigDecimal totalBusiness = orderRepo.calculateOverallBusiness();
		Map<String, Object> response = calcalculateBusinessMetrics(successfulOrders);
		response.put("totalBusiness", totalBusiness.doubleValue());
		return response;
	}
	
	private Map<String, Object> calcalculateBusinessMetrics(List<Order> orders) {
		double totalRevenue = 0.0;
		Map<String, Integer> categorySales = new HashMap<>();
		
		for(Order order : orders) {
			totalRevenue += order.getTotalAmount().doubleValue();
			
			List<OrderItem> items = orderItemRepo.findByOrderId(order.getOrderId());
			for(OrderItem item : items) {
				String categoryName = productRepo.findCategorynameByProductId(item.getProductId());
				categorySales.put(categoryName, categorySales.getOrDefault(categoryName, 0) + item.getQuantity());
			}
		}
		
		Map<String, Object> metrics = new HashMap<>();
		metrics.put("totalRevenue", totalRevenue);
		metrics.put("categorySales", categorySales);
		return metrics;
	}
}


















