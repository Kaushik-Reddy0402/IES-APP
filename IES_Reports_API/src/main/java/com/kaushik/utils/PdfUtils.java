package com.kaushik.utils;

import java.awt.Color;

import java.io.File;
import java.io.FileOutputStream;

import java.util.List;

import org.springframework.stereotype.Component;

import com.kaushik.bindings.EntitiesResponseForm;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Component
public class PdfUtils {

	public File generatedPdf(List<EntitiesResponseForm> responseForm, File file) throws Exception {
		Document document = new Document(PageSize.A4);
	    PdfWriter.getInstance(document, new FileOutputStream(file));
	    document.open();

	    // Font for the title
	    Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
	    titleFont.setSize(18);
	    titleFont.setColor(Color.BLACK);

	    Paragraph p = new Paragraph("Citizens Applications", titleFont);
	    p.setAlignment(Paragraph.ALIGN_CENTER);
	    document.add(p);

	    // Font for the table header
	    Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
	    headerFont.setColor(Color.WHITE);

	    PdfPTable table = new PdfPTable(8);
	    table.setSpacingBefore(10);
	    table.setWidthPercentage(100); // Set table width to fill the page

	    // Set background color and style for the header cells
	    PdfPCell headerCell;

	    headerCell = new PdfPCell(new Paragraph("Name", headerFont));
	    headerCell.setBackgroundColor(Color.GRAY);
	    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.addCell(headerCell);

	    headerCell = new PdfPCell(new Paragraph("Email", headerFont));
	    headerCell.setBackgroundColor(Color.GRAY);
	    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.addCell(headerCell);

	    headerCell = new PdfPCell(new Paragraph("Gender", headerFont));
	    headerCell.setBackgroundColor(Color.GRAY);
	    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.addCell(headerCell);

	    headerCell = new PdfPCell(new Paragraph("Mobile Number", headerFont));
	    headerCell.setBackgroundColor(Color.GRAY);
	    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.addCell(headerCell);

	    headerCell = new PdfPCell(new Paragraph("SSN", headerFont));
	    headerCell.setBackgroundColor(Color.GRAY);
	    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.addCell(headerCell);

	    // Adding data rows
	    for (EntitiesResponseForm form : responseForm) {
	        table.addCell(form.getFullName());
	        table.addCell(form.getEmail());
	        table.addCell(form.getGender());
	        table.addCell(form.getPhoneNo());
	        table.addCell(form.getSsn() + "");
	    }

	    document.add(table);
	    document.close();
		return file;
	}
	
}
