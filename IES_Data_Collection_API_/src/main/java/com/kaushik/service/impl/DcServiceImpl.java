package com.kaushik.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaushik.bindings.EducationForm;
import com.kaushik.bindings.IncomeForm;
import com.kaushik.bindings.KidForm;
import com.kaushik.bindings.KidsForm;
import com.kaushik.bindings.PlanSelectionForm;
import com.kaushik.bindings.SummaryScreen;
import com.kaushik.entities.AppEntity;
import com.kaushik.entities.DcPlans;
import com.kaushik.entities.EducationEntity;
import com.kaushik.entities.IncomeEntity;
import com.kaushik.entities.KidEntity;
import com.kaushik.entities.PlansEntity;
import com.kaushik.respository.AppRepo;
import com.kaushik.respository.DcPlansRepo;
import com.kaushik.respository.EducationRepo;
import com.kaushik.respository.IncomeRepo;
import com.kaushik.respository.KidsRepo;
import com.kaushik.respository.PlansRepo;
import com.kaushik.service.DcService;

@Service
public class DcServiceImpl implements DcService {

	@Autowired
	private AppRepo appRepo;

	@Autowired
	private PlansRepo planRepo;

	@Autowired
	private EducationRepo educationRepo;

	@Autowired
	private IncomeRepo incomeRepo;

	@Autowired
	private KidsRepo kidsRepo;

	@Autowired
	private DcPlansRepo dcPlansRepo;
	
	@Override
	public SummaryScreen summary(Long caseNo) {
		SummaryScreen summary = new SummaryScreen();
		AppEntity appEntity = appRepo.findById(caseNo)
									 .orElseThrow(() -> {
										 throw new RuntimeException();
									 });
		EducationForm educationForm = new EducationForm();
		if (appEntity.getEducation() != null) {
		    BeanUtils.copyProperties(appEntity.getEducation(), educationForm);
		}
		educationForm.setCaseNo(caseNo);
		summary.setEducationForm(educationForm);

		IncomeForm incomeForm = new IncomeForm();
		if (appEntity.getIncome() != null) {
		    BeanUtils.copyProperties(appEntity.getIncome(), incomeForm);
		}
		incomeForm.setCaseNo(caseNo);
		summary.setIncomeForm(incomeForm);

		
		KidsForm kidsForm = new KidsForm();
		List<KidForm> kids = appEntity.getKids().stream()
							.map(kid -> {
								KidForm form = new KidForm();
								BeanUtils.copyProperties(kid, form);
								return form;
							})
							.collect(Collectors.toList());
		kidsForm.setCaseNo(caseNo);
		kidsForm.setKids(kids);
		summary.setKidsForm(kidsForm);
		
		return summary;
	}

	@Override
	public boolean savePlan(PlanSelectionForm planSelectionForm) {
		
		DcPlans dcPlan = new DcPlans();
		dcPlan.setSelectedPlan(planSelectionForm.getSelectedPlan());
		appRepo.findById(planSelectionForm.getCaseNo())
			   .ifPresentOrElse(application -> {
				   DcPlans savedPlan = dcPlansRepo.save(dcPlan);
				   application.setCitizensPlan(savedPlan);
				   appRepo.save(application);
				   
			   }, () -> {
				   throw new RuntimeException();
			   });
		return true;
	}

	@Override
	public List<String> fetchPlans() {

		return planRepo.findAll().stream()
								 .map(PlansEntity::getPlanName)
								 .collect(Collectors.toList());
	}

	@Override
	public boolean saveEducationDetails(EducationForm educationForm) {

		EducationEntity entity = new EducationEntity();
	    BeanUtils.copyProperties(educationForm, entity);

	    appRepo.findById(educationForm.getCaseNo())
	           .ifPresentOrElse(application -> {
	               // Save education first
	               EducationEntity savedEducation = educationRepo.save(entity);

	               // Set it in the application and save app entity
	               application.setEducation(savedEducation);
	               appRepo.save(application);
	           }, () -> {
	               throw new RuntimeException("Application not found for caseNo: " + educationForm.getCaseNo());
	           });
	    return true;
	}

	@Override
	public boolean saveIncomeDetails(IncomeForm incomeForm) {

		IncomeEntity entity = new IncomeEntity();
		BeanUtils.copyProperties(incomeForm, entity);
		
		appRepo.findById(incomeForm.getCaseNo())
			   .ifPresentOrElse(application -> {
				   IncomeEntity savedEntity = incomeRepo.save(entity);
				   application.setIncome(savedEntity);
				   appRepo.save(application);
			   }, () -> {
				   throw new RuntimeException();
			   });
		
		return true;
		
	}

	@Override
	public boolean saveKidsDetails(KidsForm kidsForm) {
		
		AppEntity application = appRepo.findById(kidsForm.getCaseNo())
				.orElseThrow(() -> new IllegalArgumentException());
		
		List<KidEntity> kidsEntities = kidsForm.getKids().stream()
				.map(kid -> {
					KidEntity entity = new KidEntity();
					BeanUtils.copyProperties(kid, entity);
					entity.setApplication(application); 
					return entity;
				}).collect(Collectors.toList());
		
		kidsRepo.saveAll(kidsEntities); 
		return true;
		
	}
	
}
