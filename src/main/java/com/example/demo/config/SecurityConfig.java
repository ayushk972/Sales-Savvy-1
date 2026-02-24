package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // this disable only csrf token you can login without any csrf toekn  
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // this give to permit to go any end point without any permission

//        http.formLogin(form -> form.disable()).csrf(csrf -> csrf.disable());    
//        this will also work this tell that is remove login html page from security and also disable csrf token 
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}