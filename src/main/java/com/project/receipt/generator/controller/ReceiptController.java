package com.project.receipt.generator.controller;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.receipt.generator.model.ReceiptRequest;
import com.project.receipt.generator.service.ReceiptService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptController {

	private final ReceiptService receiptService;

	@PostMapping("/generate")
	public ResponseEntity<byte[]> generateReceipt(@Valid @RequestBody ReceiptRequest request) {
		byte[] receipt = receiptService.generateReceipt(request);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", "receipt." + request.getFormat().toLowerCase());

		if ("PDF".equalsIgnoreCase(request.getFormat())) {
			headers.setContentType(MediaType.APPLICATION_PDF);
		} else {
			headers.setContentType(MediaType.TEXT_PLAIN);
		}

		return new ResponseEntity<>(receipt, headers, HttpStatus.OK);
	}
}