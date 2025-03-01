package com.ecommerce.userService.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.userService.dto.UserDto;
import com.ecommerce.userService.model.User;
import com.ecommerce.userService.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.OK)
	public List<UserDto> getAllUsers(){
		return userService.getAllUsers();
	}
	
	@GetMapping("/{userId}")
	@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.OK)
	public UserDto getUserById(@PathVariable Long userId) {
		return userService.getUserById(userId);
	}
	
	@GetMapping("/search/{keywords}")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.OK)
	public User getUserByUserName(@PathVariable String keywords) {
		return userService.getUserByUserName(keywords);
	}
	
	@PutMapping("/{userId}")
	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.OK)
	public void updateUser(@PathVariable Long userId,@RequestBody UserDto user) {
		userService.updateUser(userId, user);
	}
	
	@DeleteMapping("/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.OK)
	public void deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
	}

}
