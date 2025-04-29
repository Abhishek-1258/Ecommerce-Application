package com.example.ecommerce.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.dto.ProductResponseDTO;
import com.example.ecommerce.dto.ProductUpdateDTO;
import com.example.ecommerce.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	private final ProductService productService;
	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ProductResponseDTO> getAllProducts(){
		return productService.getAllProducts();
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ProductResponseDTO getProductById(@PathVariable Long id) {
		return productService.getProductById(id);
	}
	
	@GetMapping("/categoryId")
	@ResponseStatus(HttpStatus.OK)
	public List<ProductResponseDTO> getProductsByCategory(@PathVariable Long categoryId){
		return productService.getProductsByCategory(categoryId);
	}
	
	@GetMapping("/categoryName")
	@ResponseStatus(HttpStatus.OK)
	public List<ProductResponseDTO> getProductsByCategoryName(@PathVariable String categoryName){
		return productService.getProductsByCategoryName(categoryName);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> createProduct(@Valid @RequestBody ProductDTO productDTO) {
		return productService.createProduct(productDTO);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> updateProduct(@Valid @RequestBody ProductUpdateDTO productDTO){
		return productService.updateProduct(productDTO);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> deleteProduct(@PathVariable Long id){
		return productService.deleteProduct(id);
	}

}
