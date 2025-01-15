package com.kaushik.service.impl;

import java.util.List;
import java.util.Optional;
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
		EducationForm education = Optional.of(appEntity.getEducation())
										  .map(entity -> {
											  EducationForm educationForm = new EducationForm();
											  BeanUtils.copyProperties(entity, educationForm);
											  educationForm.setCaseNo(caseNo);
											  return educationForm;
										  })
										  .orElse(new EducationForm());
		summary.setEducationForm(education);
		
		IncomeForm income = Optional.of(appEntity.getIncome())
				  .map(entity -> {
					  IncomeForm incomeForm = new IncomeForm();
					  BeanUtils.copyProperties(entity, incomeForm);
					  incomeForm.setCaseNo(caseNo);
					  return incomeForm;
				  })
				  .orElse(new IncomeForm());
		summary.setIncomeForm(income);
		
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
		appRepo.findById(planSelectionForm.getCaseNo())
			   .ifPresentOrElse(application -> {
				   dcPlan.setCaseNum(application);
				   BeanUtils.copyProperties(planSelectionForm, dcPlan);
			   }, () -> {
				   throw new RuntimeException();
			   });
		
		dcPlansRepo.save(dcPlan);
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

		appRepo.findById(educationForm.getCaseNo())
			   .ifPresentOrElse(application -> {
				   entity.setApplication(application);
				   BeanUtils.copyProperties(educationForm, entity);
			   }, () -> {
				   throw new RuntimeException();
			   });
		
		educationRepo.save(entity);
		return true;
		
	}

	@Override
	public boolean saveIncomeDetails(IncomeForm incomeForm) {

		IncomeEntity entity = new IncomeEntity();

		appRepo.findById(incomeForm.getCaseNo())
			   .ifPresentOrElse(application -> {
				   entity.setApplication(application);
				   BeanUtils.copyProperties(incomeForm, entity);
			   }, () -> {
				   throw new RuntimeException();
			   });
		
		incomeRepo.save(entity);
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
