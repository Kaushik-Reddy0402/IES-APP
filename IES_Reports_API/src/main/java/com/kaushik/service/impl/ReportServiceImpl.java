package com.kaushik.service.impl;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.kaushik.bindings.EntitiesResponseForm;
import com.kaushik.bindings.ReportsForm;
import com.kaushik.entities.AppEntity;
import com.kaushik.entities.EligibilityEntity;
import com.kaushik.respository.AppRepo;
import com.kaushik.service.ReportService;
import com.kaushik.utils.ExcelUtils;
import com.kaushik.utils.PdfUtils;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private PdfUtils pdfUtils;

	@Autowired
	private ExcelUtils excelUtils;

	@Autowired
	private AppRepo appRepo;

	private List<AppEntity> getFilteredRecords(ReportsForm form) {

		AppEntity probe = new AppEntity();

		// Set dynamic query conditions
		if (form.getPlanName() != null && !form.getPlanName().isEmpty()) {
			EligibilityEntity eligibility = new EligibilityEntity();
			eligibility.setPlanName(form.getPlanName());
			probe.setEligibility(eligibility);
		}

		if (form.getPlanStatus() != null && !form.getPlanStatus().isEmpty()) {
			EligibilityEntity eligibility = probe.getEligibility();
			if (eligibility == null) {
				eligibility = new EligibilityEntity();
			}
			eligibility.setPlanStatus(form.getPlanStatus());
			probe.setEligibility(eligibility);
		}

		if (form.getGender() != null && !form.getGender().isEmpty()) {
			probe.setGender(form.getGender());
		}

		// Querying the database using Example.of()
		List<AppEntity> results = appRepo.findAll(Example.of(probe));

		// Filtering results based on date range
		return results.stream().filter(app -> {
			EligibilityEntity eligibility = app.getEligibility();
			if (eligibility == null)
				return false;

			return (form.getStartDate() == null || (eligibility.getStartDate() != null
					&& !eligibility.getStartDate().isBefore(form.getStartDate())))
					&& (form.getEndDate() == null || (eligibility.getEndDate() != null
							&& !eligibility.getEndDate().isAfter(form.getEndDate())));
		}).collect(Collectors.toList());
	}

	@Override
	public List<EntitiesResponseForm> fetchRecords(ReportsForm form) {
		// Fetch filtered records and map them to EntitiesResponseForm
		return getFilteredRecords(form).stream().map(entity -> {
			EntitiesResponseForm responseForm = new EntitiesResponseForm();
			BeanUtils.copyProperties(entity, responseForm); // Map entity to response form
			return responseForm;
		}).collect(Collectors.toList());
	}

	@Override
	public File generatePdf(ReportsForm form) throws Exception {
		// Fetch filtered records once
		List<AppEntity> records = getFilteredRecords(form);

		// Mapping them to EntitiesResponseForm for PDF generation
		List<EntitiesResponseForm> responseForms = records.stream().map(entity -> {
			EntitiesResponseForm responseForm = new EntitiesResponseForm();
			BeanUtils.copyProperties(entity, responseForm); // Map entity to response form
			return responseForm;
		}).collect(Collectors.toList());

		// Generating the PDF using PdfUtils
		return pdfUtils.generatedPdf(responseForms, new File("Applications"));
	}

	

	@Override
	public File generateExcel(ReportsForm form) {
		List<AppEntity> records = getFilteredRecords(form);

		// Mapping them to EntitiesResponseForm for PDF generation
		List<EntitiesResponseForm> responseForms = records.stream().map(entity -> {
			EntitiesResponseForm responseForm = new EntitiesResponseForm();
			BeanUtils.copyProperties(entity, responseForm); // Map entity to response form
			return responseForm;
		}).collect(Collectors.toList());

		try {
			return excelUtils.generatedFile(responseForms, null);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

}
