package com.kaushik.service;

import java.util.List;

import com.kaushik.bindings.EligibilityForm;

public interface EdService {

	public EligibilityForm save(Long caseNo);
	
	public void generateCorrespondance(EligibilityForm eligForm, Long caseNo);

	public List<EligibilityForm> fetchEntities();
	
	
}
