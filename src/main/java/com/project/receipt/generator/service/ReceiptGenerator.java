package com.project.receipt.generator.service;

import com.project.receipt.generator.model.ReceiptRequest;

public interface ReceiptGenerator {
	byte[] generateReceipt(ReceiptRequest request);
}