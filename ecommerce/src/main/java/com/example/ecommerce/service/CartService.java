package com.example.ecommerce.service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.ecommerce.dto.CartItemRequest;
import com.example.ecommerce.dto.CartItemResponse;
import com.example.ecommerce.dto.CartResponseDto;
import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.dto.ProductResponseDTO;
import com.example.ecommerce.exceptions.CartItemNotFoundException;
import com.example.ecommerce.exceptions.CartNotFoundException;
import com.example.ecommerce.exceptions.ProductNotFoundException;
import com.example.ecommerce.exceptions.UserNotFoundException;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.model.Cart;

import jakarta.transaction.Transactional;

@Service
public class CartService {

	private final CartRepository cartRepository;

	private final ProductRepository productRepository;

	private final RestTemplate restTemplate;

	private final String userServiceUrl = "http://USER-SERVICE/users/";

	@Autowired
	public CartService(CartRepository cartRepository,ProductRepository productRepository,RestTemplateBuilder restTemplateBuilder) {
		this.cartRepository = cartRepository;
		this.productRepository = productRepository;
		this.restTemplate = restTemplateBuilder.build();
	}

	@Transactional
	public ResponseEntity addToCart(CartItemRequest cartItemRequest) {
		Cart cart = cartRepository.findById(cartItemRequest.getCartId()).orElseThrow(() -> new CartNotFoundException("Cart Id was not found"));

		User user = getUserFromUserService(cartItemRequest.getUserId());

		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
		}

		Product product = productRepository.findById(cartItemRequest.getProductId()).orElseThrow(() -> new ProductNotFoundException("Product was not found"));

		Optional<CartItem> existingItem = cart.getItems().stream().filter(item -> item.getProduct().equals(product)).findFirst();

		if (existingItem.isPresent()) {
			CartItem item = existingItem.get();
			item.setQuantity(item.getQuantity() + cartItemRequest.getQuantity());
			item.setPrice(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
		} else {
			CartItem cartItem = CartItem.builder()
					.cart(cart)
					.product(product)
					.price(product.getPrice().multiply(BigDecimal.valueOf(cartItemRequest.getQuantity())))
					.quantity(cartItemRequest.getQuantity())
					.build();
			cart.getItems().add(cartItem);
		}

		BigDecimal totalPrice = cart.getItems().stream().map(CartItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

		cart.setTotalPrice(totalPrice);
		cartRepository.save(cart);

		return ResponseEntity.ok(cart);
	}

	


	public CartResponseDto getCart(Long userId) {
        User user = getUserFromUserService(userId);
        if (user == null) {
            throw new UserNotFoundException("User Not found");
        }

        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartNotFoundException("Cart was not found"));

        if (cart.getItems().isEmpty()) {
            return null;
        }
        
        CartResponseDto cartResponseDto = CartResponseDto.builder()
        		.totalPrice(cart.getTotalPrice())
        		.quantity((long)cart.getItems().size())
        		.cartItems(cart.getItems())
        		.user(user)
        		.build();

        return cartResponseDto;
    }
	

	public void deleteCartItem(Long cartItemId, Long userId) {
		User user = getUserFromUserService(userId);
        if (user == null) {
            throw new UserNotFoundException("User Not found");
        }
        
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartNotFoundException("Cart was not found"));
        
        Optional<CartItem> optionalCartItem = cart.getItems().stream().filter(item -> item.getId().equals(cartItemId)).findFirst();
        if (optionalCartItem.isEmpty()) {
            throw new CartItemNotFoundException("Cart item not found");
        }
        
        CartItem cartItem = optionalCartItem.get();

        cart.getItems().remove(cartItem);
        
        BigDecimal totalPrice = cart.getItems().stream().map(CartItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);

	}
	
	public User getUserFromUserService(Long userId) {
		try {
			return restTemplate.getForObject(userServiceUrl + userId, User.class);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}


	public void updateCartItem(CartItemRequest cartItemRequest, Long userId, ProductResponseDTO product) {
		Cart cart = cartRepository.findById(cartItemRequest.getCartId()).orElseThrow(() -> new CartNotFoundException("Cart Id was not found"));
		
		if (!cart.getUser().getId().equals(userId)) {
	        throw new UserNotFoundException("User ID mismatch");
	    }
		
		CartItem cartItem = cart.getItems().stream().filter(item -> item.getProduct().getId().equals(cartItemRequest.getProductId()))
                .findFirst().orElseThrow(() -> new CartItemNotFoundException("Cart item not found"));
		
		cartItem.setQuantity(cartItemRequest.getQuantity());
		cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItemRequest.getQuantity())));
		
		BigDecimal totalPrice = cart.getItems().stream().map(CartItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
		cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);
	}

}
