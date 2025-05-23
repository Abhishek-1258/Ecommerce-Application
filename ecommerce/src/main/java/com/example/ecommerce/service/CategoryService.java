package com.example.ecommerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.CategoryDTO;
import com.example.ecommerce.exceptions.CategoryAlreadyExistsException;
import com.example.ecommerce.exceptions.CategoryNotFoundException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.repository.CategoryRepository;

import jakarta.validation.Valid;

@Service
public class CategoryService {
	
	private CategoryRepository categoryRepository;
	
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public ResponseEntity<String> createCategory(CategoryDTO categoryDTO) {
		if(categoryRepository.existsByName(categoryDTO.getName())) {
			throw new CategoryAlreadyExistsException("Category already Exists");
		}
		Category category = Category.builder()
				.name(categoryDTO.getName())
				.description(categoryDTO.getDescription())
				.status(categoryDTO.getStatus())
				.build();
		
		categoryRepository.save(category);
		return ResponseEntity.ok("Category Created Successfully");
	}

	public CategoryDTO getCategoryById(Long id) {
		Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category with id : " + id + " was not found"));
		return CategoryDTO.builder()
				.name(category.getName())
				.description(category.getDescription())
				.status(category.getStatus())
				.build();
	}

	public List<CategoryDTO> getAllCategories() {
		List<Category> categories = categoryRepository.findAll();
		if(categories.isEmpty()) {
			throw new CategoryNotFoundException("No Categories Found");
		}
		
		return categories.stream().map(category -> CategoryDTO.builder()
				.name(category.getName())
				.description(category.getDescription())
				.status(category.getStatus()).build())
				.collect(Collectors.toList());
	}

	public ResponseEntity<String> updateCategory(@Valid CategoryDTO categoryDTO) {
		if(categoryRepository.existsByName(categoryDTO.getName())) {
			throw new CategoryAlreadyExistsException("Category already Exists");
		}
		Category category = categoryRepository.findByName(categoryDTO.getName());
		category.setDescription(categoryDTO.getDescription());
		category.setName(categoryDTO.getName());
		category.setStatus(category.getStatus());
		
		categoryRepository.save(category);
		return ResponseEntity.ok("Category Updated Successfully");
	}

	public ResponseEntity<String> deleteCategory(Long id) {
		if(categoryRepository.existsById(id)) {
			throw new CategoryAlreadyExistsException("Category already Exists");
		}
		categoryRepository.deleteById(id);
		return ResponseEntity.ok("Category Deleted Successfully");
	}

}
