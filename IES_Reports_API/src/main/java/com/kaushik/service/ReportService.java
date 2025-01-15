package com.kaushik.service;

import java.io.File;
import java.util.List;

import com.kaushik.bindings.EntitiesResponseForm;
import com.kaushik.bindings.ReportsForm;

public interface ReportService {

	public File generatePdf(ReportsForm form) throws Exception;
	
	public File generateExcel(ReportsForm form);
	
	public List<EntitiesResponseForm> fetchRecords(ReportsForm form);
	
}
