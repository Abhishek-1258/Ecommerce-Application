package com.example.ecommerce.dto;

import java.math.BigDecimal;

import com.example.ecommerce.model.Category;
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
public class ProductResponseDTO {
	
	private String name;
	
	private BigDecimal price;
	
	private String description;
	
	private String category;

}
