package com.kaushik.service;

import java.util.List;

import com.kaushik.binding.AppForm;
import com.kaushik.binding.CitizenResForm;

public interface ArService {

	public String applicationCreation(AppForm citizenReqForm, Integer userId);
	
	public List<CitizenResForm> fetchEntities(Integer userId);
	
}
