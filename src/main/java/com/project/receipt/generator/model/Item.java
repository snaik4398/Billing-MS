
// Item.java
package com.project.receipt.generator.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
	@NotNull(message = "Item number is required")
	private Integer itemNumber;

	@NotBlank(message = "Item name is required")
	private String itemName;

	@NotNull(message = "Item price is required")
	@Positive(message = "Item price must be positive")
	private Double price;
}