package com.project.receipt.generator.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.project.receipt.generator.exception.ReceiptGenerationException;
import com.project.receipt.generator.model.Company;
import com.project.receipt.generator.model.Item;
import com.project.receipt.generator.model.ReceiptRequest;

@Component
public class PdfReceiptGenerator implements ReceiptGenerator {

	@Override
	public byte[] generateReceipt(ReceiptRequest request) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			Document document = new Document();
			PdfWriter.getInstance(document, baos);
			document.open();

			addCompanyDetails(document, request.getCompany());
			addReceiptHeader(document);
			addItemsTable(document, request.getItems());
			addTotal(document, request.getItems());
			addFooter(document);

			document.close();
			return baos.toByteArray();
		} catch (Exception e) {
			throw new ReceiptGenerationException("Error generating PDF receipt: " + e.getMessage(), e);
		}
	}

	private void addCompanyDetails(Document document, Company company) throws DocumentException, IOException {
		if (company != null) {
			if (company.getLogoPath() != null && !company.getLogoPath().isEmpty()) {
				try {
					Image logo = Image.getInstance(new ClassPathResource(company.getLogoPath()).getURL());
					logo.scaleToFit(100, 100);
					logo.setAlignment(Element.ALIGN_CENTER);
					document.add(logo);
				} catch (Exception e) {
					// If logo can't be loaded, continue without it
					document.add(new Paragraph("Logo not available"));
				}
			}

			Font companyNameFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

			Paragraph companyName = new Paragraph(company.getName().toLowerCase(), companyNameFont);
			companyName.setAlignment(Element.ALIGN_CENTER);
			document.add(companyName);

			if (company.getGstNumber() != null && !company.getGstNumber().isEmpty()) {
				Paragraph gstNumber = new Paragraph("GST Number: " + company.getGstNumber());
				gstNumber.setAlignment(Element.ALIGN_CENTER);
				document.add(gstNumber);
			}

			document.add(new Paragraph(" ")); // Add some space
		}
	}

	private void addReceiptHeader(Document document) throws DocumentException {
		Font headerFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
		Paragraph header = new Paragraph("RECEIPT", headerFont);
		header.setAlignment(Element.ALIGN_CENTER);
		document.add(header);

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String currentDate = dateFormat.format(new Date());

		Paragraph date = new Paragraph("Date: " + currentDate);
		date.setAlignment(Element.ALIGN_RIGHT);
		document.add(date);

		document.add(new Paragraph(" ")); // Add some space
	}

	private void addItemsTable(Document document, List<Item> items) throws DocumentException {
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100);
		table.setWidths(new float[] { 1, 3, 2, 2 });

		// Add table headers
		Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
		addTableCell(table, "No.", headerFont);
		addTableCell(table, "Item Name", headerFont);
		addTableCell(table, "Price", headerFont);
		addTableCell(table, "Amount", headerFont);

		// Add item rows
		Font itemFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
		for (Item item : items) {
			addTableCell(table, String.valueOf(item.getItemNumber()), itemFont);
			addTableCell(table, item.getItemName(), itemFont);
			addTableCell(table, String.format("$%.2f", item.getPrice()), itemFont);
			addTableCell(table, String.format("$%.2f", item.getPrice()), itemFont);
		}

		document.add(table);
	}

	private void addTableCell(PdfPTable table, String text, Font font) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setPadding(5);
		table.addCell(cell);
	}

	private void addTotal(Document document, List<Item> items) throws DocumentException {
		double total = items.stream().mapToDouble(Item::getPrice).sum();

		document.add(new Paragraph(" ")); // Add some space

		PdfPTable totalTable = new PdfPTable(2);
		totalTable.setWidthPercentage(50);
		totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

		Font totalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

		PdfPCell labelCell = new PdfPCell(new Phrase("Total", totalFont));
		labelCell.setPadding(5);
		totalTable.addCell(labelCell);

		PdfPCell valueCell = new PdfPCell(new Phrase(String.format("$%.2f", total), totalFont));
		valueCell.setPadding(5);
		totalTable.addCell(valueCell);

		document.add(totalTable);
	}

	private void addFooter(Document document) throws DocumentException {
		document.add(new Paragraph(" ")); // Add some space

		Font footerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
		Paragraph footer = new Paragraph("Thank you for your business!", footerFont);
		footer.setAlignment(Element.ALIGN_CENTER);
		document.add(footer);
	}
}