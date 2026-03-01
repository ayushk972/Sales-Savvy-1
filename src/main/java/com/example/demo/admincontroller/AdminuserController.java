package com.example.demo.admincontroller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.adminservice.AdminUserService;
import com.example.demo.entity.User;

@RestController
@RequestMapping("/admin/user")
public class AdminuserController {

	private final AdminUserService adminUserService;
	
	public AdminuserController(AdminUserService adminUserService) {
		this.adminUserService = adminUserService;
	}
	
	@PutMapping("/modify")
	public ResponseEntity<?> modifyUser(@RequestBody Map<String, Object> userRequest) {
		try {
			Integer userid = (Integer) userRequest.get("userid");
			String username = (String) userRequest.get("username");
			String email = (String) userRequest.get("email");
			String role = (String) userRequest.get("role");
			
			User updatedUser = adminUserService.modifyUser(userid, username, email, role);
			Map<String, Object> response = new HashMap<>();
			response.put("userid", updatedUser.getUserid());
			response.put("username", updatedUser.getUsername());
			response.put("email", updatedUser.getEmail());
			response.put("role", updatedUser.getRole().name());
			response.put("createdAt", updatedUser.getCreated_at());
			response.put("updatedAt", updatedUser.getUpdated_at());
			return ResponseEntity.status(HttpStatus.OK).body(response);
			
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went error");
		}
	}
	
//	@GetMapping("/get")
//	public ResponseEntity<?> getUserByID(@RequestBody Map<String, Integer> userRequest) {
//		try {
//			Integer userid = userRequest.get("userid");
//			User user = adminUserService.getUserById(userid);
//			return ResponseEntity.status(HttpStatus.OK).body(user);
//		} catch (IllegalArgumentException e) {
//			// TODO: handle exception
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//		} catch (Exception e) {
//			// TODO: handle exception
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
//		}
//	}
	
	@GetMapping("/get")
	public ResponseEntity<?> getUserByID(@RequestParam Integer userid) {
		try {
			User user = adminUserService.getUserById(userid);
			return ResponseEntity.status(HttpStatus.OK).body(user);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
		}
}
}








