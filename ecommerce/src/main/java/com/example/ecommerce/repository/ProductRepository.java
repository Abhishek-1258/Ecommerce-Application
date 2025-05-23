package com.example.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ecommerce.model.Product;


public interface ProductRepository extends JpaRepository<Product, Long>{
	
	@Query("SELECT p FROM Product p WHERE p.category.id = :id")
	public List<Product> getProductsByCategoryId(@Param("id") Long id);
	
	@Query("SELECT p FROM Product p WHERE p.category.name = :categoryName")
	public List<Product> getProductsByCategoryName(@Param("categoryName") String categoryName);
	
	public boolean existsById(Long id);

}
