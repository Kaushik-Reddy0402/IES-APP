package com.kaushik.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.kaushik.bindings.EntitiesResponseForm;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Component
public class ExcelUtils {

    public File generatedFile(List<EntitiesResponseForm> responseForm, File file) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Citizens Applications");

        // Create title row
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Citizens Applications");
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 18);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 4));

        // Create header row
        Row headerRow = sheet.createRow(1);
        String[] headers = {"Name", "Email", "Gender", "Mobile Number", "SSN"};
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Populate data rows
        int rowIdx = 2;
        for (EntitiesResponseForm form : responseForm) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(form.getFullName());
            row.createCell(1).setCellValue(form.getEmail());
            row.createCell(2).setCellValue(form.getGender());
            row.createCell(3).setCellValue(form.getPhoneNo());
            row.createCell(4).setCellValue(form.getSsn());
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the file to disk
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        } finally {
            workbook.close();
        }

        return file;
    }
}

