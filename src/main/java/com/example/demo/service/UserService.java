package com.example.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.UserRegisterResponse;

@Service
public class UserService {

	private UserRepository userRepositry;
	
	private BCryptPasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepositry, BCryptPasswordEncoder passwordEncoder) {
		this.userRepositry = userRepositry;
		this.passwordEncoder = passwordEncoder;
	}
	
	public UserRegisterResponse<User> registerUser(User user) {

		String message = validateUser(user);
		
		if(!message.equals("valid")) {
			return new UserRegisterResponse<>(false, message, null);
		}
		
		// Encrypt the password and save
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User savedUser = userRepositry.save(user);
		
		return new UserRegisterResponse<User>(true, "User registered sucessfully", savedUser);
		
	}
	
	private String validateUser(User user) {

	    if (user == null) return "Please provide valid data.";

	    String username = user.getUsername();
	    String email = user.getEmail();
	    String password = user.getPassword();

	    if (username == null || username.length() < 5 || username.length() > 50)
	        return "Please provide valid username.";

	    if (userRepositry.findByUsername(username).isPresent())
	        return "Username already exists. Please choose another.";

	    if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
	        return "Please provide valid email.";

	    if (userRepositry.findByEmail(email).isPresent())
	        return "Email is already registered. Try logging in.";

	    if (password == null || password.length() < 8)
	        return "Please provide valid password.";

	    boolean hasUpper = password.matches(".*[A-Z].*");
	    boolean hasLower = password.matches(".*[a-z].*");
	    boolean hasDigit = password.matches(".*\\d.*");
	    boolean hasSpecial = password.matches(".*[@#$%^&+=!].*");

	    if (!(hasUpper && hasLower && hasDigit && hasSpecial))
	        return "Please provide valid password.";

	    return "valid";
	}
}