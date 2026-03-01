package com.example.demo.adminservice;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.JWTTokenRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminUserService {

	private final UserRepository userRepo;
	private final JWTTokenRepository jwtTokenRepo;
	
	public AdminUserService(UserRepository userRepo, JWTTokenRepository jwtTokenRepo) {
		this.userRepo = userRepo;
		this.jwtTokenRepo = jwtTokenRepo;
	}
	
	@Transactional
	public User modifyUser(Integer userid, String username, String email, String role) {
		// Check if the user exists
		Optional<User> userOptional = userRepo.findById(userid);
		if(userOptional.isEmpty()) {
			throw new IllegalArgumentException("User not found");
		}
		
		User existingUser = userOptional.get();
		
		//Update user feilds 
		if(username != null && !username.isEmpty()) {
			existingUser.setUsername(username);
		}
		
		if(email != null && !email.isEmpty()) {
			existingUser.setEmail(email);
		}
		
		if(role != null && !role.isEmpty()) {
			try {
				existingUser.setRole(Role.valueOf(role));
			} catch (IllegalArgumentException e) {
				// TODO: handle exception
				throw new IllegalArgumentException("Invalid role: " + role);
			}
		}
		
		// Delete associated JWT tokens
		jwtTokenRepo.deleteByUserid(userid);
		
		//Save updated user
		return userRepo.save(existingUser);
			
	}
	
	public User getUserById(Integer userid) {
		return userRepo.findById(userid).orElseThrow(() -> new IllegalArgumentException("User not found"));
	}
}






















