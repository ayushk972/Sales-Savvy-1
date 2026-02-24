package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.response.UserRegisterResponse;


@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

	UserService userServ;
	
	public UserController(UserService userServ) {
		this.userServ = userServ;
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		UserRegisterResponse<User> response = userServ.registerUser(user);
		
		if(!response.isSuccess()) {
			return ResponseEntity.badRequest().body(Map.of("error", response.getMessage()));
		}
		
		User registerUser = response.getData();
		return ResponseEntity.ok(Map.of("message", response.getMessage(),"data", registerUser));
		
	}
}
