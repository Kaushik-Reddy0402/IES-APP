package com.kaushik.rest;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaushik.bindings.EntitiesResponseForm;
import com.kaushik.bindings.ReportsForm;
import com.kaushik.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportRest {

    @Autowired
    private ReportService reportService;

    @PostMapping("/fetch-records")
    public ResponseEntity<List<EntitiesResponseForm>> fetchRecords(@RequestBody ReportsForm form) {
        try {
            List<EntitiesResponseForm> records = reportService.fetchRecords(form);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/generate-pdf")
    public ResponseEntity<?> generatePdf(@RequestBody ReportsForm form) {
        try {
            File pdfFile = reportService.generatePdf(form);

            // Returning the file as a downloadable response
            InputStreamResource resource = new InputStreamResource(new FileInputStream(pdfFile));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFile.getName())
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdfFile.length())
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating PDF: " + e.getMessage());
        }
    }

    @PostMapping("/generate-excel")
    public ResponseEntity<?> generateExcel(@RequestBody ReportsForm form) {
        try {
            File excelFile = reportService.generateExcel(form);

            // Returning the file as a downloadable response
            InputStreamResource resource = new InputStreamResource(new FileInputStream(excelFile));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + excelFile.getName())
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .contentLength(excelFile.length())
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating Excel: " + e.getMessage());
        }
    }
}
