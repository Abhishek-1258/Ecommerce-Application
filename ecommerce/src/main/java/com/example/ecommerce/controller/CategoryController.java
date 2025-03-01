package com.example.ecommerce.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dto.CategoryDTO;
import com.example.ecommerce.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {
	
	private CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@PostMapping
	public ResponseEntity<String> createCategory(@Valid CategoryDTO categoryDTO){
		return categoryService.createCategory(categoryDTO);
	}
	
	@GetMapping("/{id}")
	public CategoryDTO getCategoryById(@PathVariable Long id) {
		return categoryService.getCategoryById(id);
	}
	
	@GetMapping
	public List<CategoryDTO> getAllCategories() {
		return categoryService.getAllCategories();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> updateCategory(@Valid CategoryDTO categoryDTO) {
		return categoryService.updateCategory(categoryDTO);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long id){
		return categoryService.deleteCategory(id);
	}
}
