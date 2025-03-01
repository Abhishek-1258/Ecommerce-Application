package com.ecommerce.userService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.userService.dto.LoginRequestDTO;
import com.ecommerce.userService.dto.LoginResponseDto;
import com.ecommerce.userService.dto.SignUpDTO;
import com.ecommerce.userService.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@GetMapping("/test")
	public String test() {
		return "Test API is working";
	}
	
	@PostMapping("/signin")
	public LoginResponseDto authenticateUser(@RequestBody @Valid LoginRequestDTO loginDto) {
		return authService.authenticateUser(loginDto);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<Object> signUp(@RequestBody @Valid SignUpDTO signupDto) {
		return authService.signUp(signupDto);
	}
	
}
