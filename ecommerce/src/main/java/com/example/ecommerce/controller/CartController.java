package com.example.ecommerce.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dto.CartItemRequest;
import com.example.ecommerce.dto.CartResponseDto;
import com.example.ecommerce.dto.ProductResponseDTO;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
public class CartController {
	
	private final CartService cartService;
	
	private final ProductService productService;
	
	public CartController(CartService cartService,ProductService productService) {
		this.cartService = cartService;
		this.productService = productService;
	}
	
	@PostMapping("/add")
	public ResponseEntity addToCart(@RequestBody CartItemRequest cartItemRequest,@RequestHeader("userId") Long userId) {
		cartItemRequest.setUserId(userId);
		return cartService.addToCart(cartItemRequest);
	}
	
	@GetMapping("/get")
	public ResponseEntity<CartResponseDto> getCart(@RequestHeader("userId") Long userId){
		return ResponseEntity.ok(cartService.getCart(userId));
	}
	
	
	@DeleteMapping("/delete/{cartItemId}")
	public ResponseEntity deleteCartItem(@PathVariable Long cartItemId,@RequestHeader("userId") Long userId) {
		cartService.deleteCartItem(cartItemId,userId);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/update/{cartItemId}")
	public ResponseEntity updateCartItem(@RequestBody @Valid CartItemRequest cartItemRequest) {
		ProductResponseDTO product = productService.getProductById(cartItemRequest.getCartId());
		if(product == null) {
			return ResponseEntity.notFound().build();
		}
		cartService.updateCartItem(cartItemRequest, cartItemRequest.getUserId(),product);
		
		return ResponseEntity.ok().build();
	}

}
