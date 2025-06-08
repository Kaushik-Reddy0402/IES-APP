package com.kaushik.service;

import java.util.List;

import com.kaushik.bindings.EducationForm;
import com.kaushik.bindings.IncomeForm;
import com.kaushik.bindings.KidsForm;
import com.kaushik.bindings.PlanSelectionForm;
import com.kaushik.bindings.SummaryScreen;

public interface DcService {

	public List<String> fetchPlans();
	
	public boolean savePlan(PlanSelectionForm planSelectionForm);
	
	public boolean saveEducationDetails(EducationForm educationForm);
	
	public boolean saveIncomeDetails(IncomeForm incomeForm);
	
	public boolean saveKidsDetails(KidsForm kidsForm);
	
	public SummaryScreen summary(Long caseNo);
	
}
