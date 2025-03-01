package com.ecommerce.userService.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {

	private String name;

	private String username;

	private String email;
	
	private String token;

}
