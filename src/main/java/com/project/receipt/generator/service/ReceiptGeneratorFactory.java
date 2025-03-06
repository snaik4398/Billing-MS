package com.project.receipt.generator.service;

import org.springframework.stereotype.Component;

import com.project.receipt.generator.exception.ReceiptGenerationException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReceiptGeneratorFactory {

	private final PdfReceiptGenerator pdfReceiptGenerator;
	private final TxtReceiptGenerator txtReceiptGenerator;

	public ReceiptGenerator getGenerator(String format) {
		if ("PDF".equalsIgnoreCase(format)) {
			return pdfReceiptGenerator;
		} else if ("TXT".equalsIgnoreCase(format)) {
			return txtReceiptGenerator;
		} else {
			throw new ReceiptGenerationException("Unsupported format: " + format + ". Only PDF and TXT are supported");
		}
	}
}
