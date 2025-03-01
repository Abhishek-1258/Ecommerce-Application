package com.ecommerce.userService.service;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.ecommerce.userService.dto.UserDto;
import com.ecommerce.userService.exceptions.UserNotFoundException;
import com.ecommerce.userService.model.Role;
import com.ecommerce.userService.model.User;
import com.ecommerce.userService.repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public List<UserDto> getAllUsers() {
	    List<User> users = userRepository.findAll();
	    return users.stream()
	        .map(user -> UserDto.builder()
	            .name(user.getName())
	            .username(user.getUsername())
	            .email(user.getEmail())
	            .userType(user.getRoles().stream().findFirst().map(Role::getRoleName).orElse(null)) 
	            .build())
	        .toList();
	}
	
	public UserDto getUserById(Long userId) {
	    User user = userRepository.findById(userId)
	        .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " was not found"));

	    return UserDto.builder()
	        .name(user.getName())
	        .username(user.getUsername())
	        .email(user.getEmail())
	        .userType(user.getRoles().stream()
	            .findFirst()
	            .map(Role::getRoleName)
	            .orElse(null)) 
	        .build();
	}
	
	public User getUserByUserName(@PathVariable String keywords) {
		return userRepository.findByUsername(keywords).orElseThrow(() -> new UserNotFoundException("User with :" + keywords + " was not found"));
	}
	
	public void updateUser(@PathVariable Long userId,@RequestBody UserDto updatedUser) {
		User user = userRepository.findById(userId)
		        .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " was not found"));
		String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
		user.setEmail(updatedUser.getEmail());
		user.setName(updatedUser.getName());
		user.setPassword(encodedPassword);
		user.setUsername(updatedUser.getUsername());
		
		userRepository.save(user);
	}
	
	public void deleteUser(@PathVariable Long userId) {
		if(!userRepository.existsById(userId)) {
			throw new UserNotFoundException("User with id : " + userId + " was not found");
		}
		
		userRepository.deleteById(userId);
	}

}
