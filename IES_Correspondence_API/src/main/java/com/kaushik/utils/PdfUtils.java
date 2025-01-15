package com.kaushik.utils;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.kaushik.entities.EligibilityEntity;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

@Component
public class PdfUtils {

	private static final String FILE_PATH = "IES_Notice.pdf"; // Path where the PDF will be saved

	public File generatePdf(EligibilityEntity eligEntity, Long caseNo, String title)
			throws FileNotFoundException, DocumentException {
		// Initialize the document and PdfWriter inside try-with-resources to manage
		// resources properly
		try (Document document = new Document(PageSize.A4);
				FileOutputStream fileOutputStream = new FileOutputStream(FILE_PATH)) {

			PdfWriter.getInstance(document, fileOutputStream);
			document.open(); // Open the document for writing

			// Define title and label fonts
			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, getTitleColor(title));
			Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);

			// Title of the document
			Paragraph titleParagraph = new Paragraph(title, titleFont);
			titleParagraph.setAlignment(Paragraph.ALIGN_CENTER);
			titleParagraph.setSpacingAfter(20); // Set spacing after title
			document.add(titleParagraph);

			// Add fields and values to the document in a form-like layout
			addFields(document, eligEntity, caseNo, labelFont);

			// Return the generated file
			return new File(FILE_PATH);
		} catch (DocumentException | IOException e) {
			throw new RuntimeException("Error generating PDF", e);
		}
	}

	private void addFields(Document document, EligibilityEntity eligEntity, Long caseNo, Font labelFont)
			throws DocumentException {
		document.add(new Paragraph("Case Number: " + caseNo, labelFont));
		document.add(new Paragraph("Plan Name: " + getValueOrDefault(eligEntity.getPlanName()), labelFont));
		document.add(new Paragraph("Plan Status: " + getValueOrDefault(eligEntity.getPlanStatus()), labelFont));
		document.add(new Paragraph("Plan Start Date: " + getValueOrDefault(eligEntity.getStartDate()), labelFont));
		document.add(new Paragraph("Plan End Date: " + getValueOrDefault(eligEntity.getEndDate()), labelFont));
		document.add(new Paragraph("Benefit Amount: " + getValueOrDefault(eligEntity.getBenifitAmt()), labelFont));
		document.add(new Paragraph("Denial Reason: " + getValueOrDefault(eligEntity.getDenialReason()), labelFont));
	}

	private String getValueOrDefault(Object value) {
		return value != null ? value.toString() : "N/A";
	}

	private Color getTitleColor(String title) {
		return "DENIED".equals(title) ? Color.RED : Color.GREEN;
	}

	// Methods for generating specific PDFs based on the status
	public File generateDeniedPdf(EligibilityEntity eligEntity, Long caseNo) throws Exception {
		return generatePdf(eligEntity, caseNo, "DENIED");
	}

	public File generateApprovedPdf(EligibilityEntity eligEntity, Long caseNo) throws Exception {
		return generatePdf(eligEntity, caseNo, "APPROVED");
	}
}
