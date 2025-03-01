package com.ecommerce.userService.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.userService.dto.LoginRequestDTO;
import com.ecommerce.userService.dto.LoginResponseDto;
import com.ecommerce.userService.dto.SignUpDTO;
import com.ecommerce.userService.jwt.JwtAuthenticationHelper;
import com.ecommerce.userService.model.Role;
import com.ecommerce.userService.model.User;
import com.ecommerce.userService.repository.UserRepository;
import com.ecommerce.userService.security.CustomUserDetailsService;



@Service
public class AuthService {

	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	JwtAuthenticationHelper jwtAuthenticationHelper;
	
	

	public AuthService(PasswordEncoder passwordEncoder,UserRepository userRepository) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
	}
	
	public LoginResponseDto authenticateUser(LoginRequestDTO loginDto) {
	    Authentication authentication = doAuthenticate(loginDto.getUsernameOrEmail(), loginDto.getPassword());

	    User user = (User) authentication.getPrincipal();  

	    String token = jwtAuthenticationHelper.generateToken(user);

	    return LoginResponseDto.builder()
	            .name(user.getName())
	            .username(user.getUsername())
	            .email(user.getEmail())
	            .token(token)
	            .build();
	}

	
	private Authentication doAuthenticate(String username,String password) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		try {
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			return  authentication;
		}
		catch (BadCredentialsException e) {
			throw new BadCredentialsException("Invalid Credentials");
		}
	}

	public ResponseEntity<Object> signUp(SignUpDTO signupDto) {
		if(userRepository.existsByUsername(signupDto.getUsername())){
			return new ResponseEntity<>("User Name Already Exists",HttpStatus.BAD_REQUEST);
		}
		if(userRepository.existsByEmail(signupDto.getEmail())) {
			return new ResponseEntity<>("Email Already Exists",HttpStatus.BAD_REQUEST);
		}
		String encodedPassword = passwordEncoder.encode(signupDto.getPassword());
		User user = User.builder()
				.name(signupDto.getName())
				.username(signupDto.getUsername())
				.email(signupDto.getEmail())
				.password(encodedPassword).build();

		Role role = new Role();
		Set<Role> roles = new HashSet<>();
		if(signupDto.getUserType() != null) {
			if(signupDto.getUserType().equalsIgnoreCase("ADMIN")) {
				role.setRoleName("ROLE_ADMIN");
			}
			else {
				role.setRoleName("ROLE_USER");
			}
			roles.add(role);
			user.setRoles(roles);
		}
		userRepository.save(user);
		return ResponseEntity.ok("User Created Successfully");

	}

}
