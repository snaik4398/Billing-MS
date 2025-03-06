package com.project.receipt.generator.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.project.receipt.generator.exception.ReceiptGenerationException;
import com.project.receipt.generator.model.Company;
import com.project.receipt.generator.model.Item;
import com.project.receipt.generator.model.ReceiptRequest;

@Component
public class TxtReceiptGenerator implements ReceiptGenerator {

	@Override
	public byte[] generateReceipt(ReceiptRequest request) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); PrintWriter writer = new PrintWriter(baos)) {

			addCompanyDetails(writer, request.getCompany());
			writer.println("==================================");
			writer.println("             RECEIPT              ");
			writer.println("==================================");

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String currentDate = dateFormat.format(new Date());
			writer.println("Date: " + currentDate);
			writer.println();

			addItemsTable(writer, request.getItems());
			writer.println();

			double total = request.getItems().stream().mapToDouble(Item::getPrice).sum();
			
			writer.println(String.format("TOTAL: $%.2f", total));
			writer.println();
			writer.println("Thank you for your business!");

			writer.flush();
			return baos.toByteArray();
		} catch (Exception e) {
			throw new ReceiptGenerationException("Error generating TXT receipt: " + e.getMessage(), e);
		}
	}

	private void addCompanyDetails(PrintWriter writer, Company company) {
		if (company != null) {
			if (company.getName() != null && !company.getName().isEmpty()) {
				writer.println(company.getName().toLowerCase());
			}

			if (company.getGstNumber() != null && !company.getGstNumber().isEmpty()) {
				writer.println("GST Number: " + company.getGstNumber());
			}

			writer.println();
		}
	}

	private void addItemsTable(PrintWriter writer, List<Item> items) {
		writer.println(String.format("%-5s %-25s %-10s %-10s", "No.", "Item Name", "Price", "Amount"));
		writer.println("--------------------------------------------------");

		for (Item item : items) {
			writer.println(String.format("%-5d %-25s $%-9.2f $%-9.2f", item.getItemNumber(),
					truncate(item.getItemName(), 25), item.getPrice(), item.getPrice()));
		}
		writer.println("--------------------------------------------------");

	}

	private String truncate(String text, int maxLength) {
		return text.length() <= maxLength ? text : text.substring(0, maxLength - 3) + "...";
	}
}