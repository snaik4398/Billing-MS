package com.project.receipt.generator.service;

import org.springframework.stereotype.Service;

import com.project.receipt.generator.model.ReceiptRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceiptService {

	private final ReceiptGeneratorFactory receiptGeneratorFactory;

	public byte[] generateReceipt(ReceiptRequest request) {
		return receiptGeneratorFactory.getGenerator(request.getFormat()).generateReceipt(request);
	}
}
