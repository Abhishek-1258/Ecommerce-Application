package com.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {

	private Long cartId;
	
	private Long userId;
	
	private Long productId;
	
	private Long quantity;

}
