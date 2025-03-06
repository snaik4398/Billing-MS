package com.project.receipt.generator.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptRequest {
	@NotEmpty(message = "At least one item is required")
	private List<@Valid Item> items;

	private Company company;

	@NotBlank(message = "Format is required (PDF or TXT)")
	private String format;
}