package com.example.ecommerce.dto;

import java.math.BigDecimal;
import java.util.List;

import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDto {
	
	private User user;
	
	private BigDecimal totalPrice;

	
	private List<CartItem> cartItems;
	
	private Long quantity;

}
