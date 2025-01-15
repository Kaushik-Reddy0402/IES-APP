package com.kaushik.service.impl;

import java.time.LocalDate;

import java.time.Period;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaushik.bindings.EligibilityForm;
import com.kaushik.constants.AppConstants;
import com.kaushik.entities.AppEntity;
import com.kaushik.entities.EligibilityEntity;
import com.kaushik.entities.KidEntity;
import com.kaushik.exceptions.ApplicationNotFound;
import com.kaushik.respository.AppRepo;
import com.kaushik.respository.EligibilityRepo;
import com.kaushik.service.EdService;

@Service
public class EdServiceImpl implements EdService {

    @Autowired
    private EligibilityRepo eligibilityRepo;

    @Autowired
    private AppRepo appRepo;
    
    private final Map<String, PlanHandler> planHandlers;

    public EdServiceImpl() {
        // Initialize the map of plan handlers
        this.planHandlers = Map.of(
            AppConstants.PLAN_SNAP, this::snapPlan,
            AppConstants.PLAN_CCAP, this::ccapPlan,
            AppConstants.PLAN_MEDICAID, this::medicaid,
            AppConstants.PLAN_MEDICARE, this::medicare,
            AppConstants.PLAN_RIW, this::riw
        );
    }

    @Override
    public EligibilityForm save(Long caseNo) {
    	
    	EligibilityForm form = new EligibilityForm();
        // Retrieve the application entity
        AppEntity appEntity = appRepo.findById(caseNo)
                .orElseThrow(() -> new ApplicationNotFound(AppConstants.APP_FOUND_MSG + caseNo));

        EligibilityEntity eligibilityEntity = new EligibilityEntity();
        String selectedPlan = appEntity.getCitizensPlan().getSelectedPlan();
        eligibilityEntity.setPlanName(selectedPlan);
        eligibilityEntity.setCaseNo(appEntity);
        
        // Handle the selected plan dynamically
        PlanHandler handler = planHandlers.get(selectedPlan);
        handler.handle(appEntity, eligibilityEntity, selectedPlan);

        // Save the eligibility entity
        BeanUtils.copyProperties(eligibilityEntity, form);
        eligibilityRepo.save(eligibilityEntity);

        return form;
    }

//  @Override
//  public List<EligibilityForm> fetchEntities(Integer userId) {
//  	UserEntity userEntity = userRepo.findById(userId)
//  			.orElseThrow(() -> {
//  				throw new UserNotFoundException(AppConstants.NOT_FOUND_MSG);
//  			});
//  	return userEntity.getApplications().stream()
//  									   .map(appEntity -> {
//  										   EligibilityForm form = new EligibilityForm();
//  										   BeanUtils.copyProperties(appEntity.getEligibility(), form);
//  										   return form;
//  									   })
//  									   .collect(Collectors.toList());
//  }
    
	private void riw(AppEntity appEntity, EligibilityEntity eligibilityEntity, String selectedPlan) {
		Double monthlySalary = appEntity.getIncome().getMonthlySalary();
		String graduationYear = appEntity.getEducation().getGraduationYear();
		if ((monthlySalary == null || monthlySalary == 0.0) && graduationYear != null && !graduationYear.isEmpty()) {
			setEntity(appEntity, eligibilityEntity, selectedPlan);
			eligibilityEntity.setBenifitAmt(1100.00);
		} else {
			denial(eligibilityEntity, AppConstants.RIW_DENIED_REASON);
		}
	}

	private void medicare(AppEntity appEntity, EligibilityEntity eligibilityEntity, String selectedPlan) {
		LocalDate dob = appEntity.getDob();
		if (65 <= Period.between(dob, LocalDate.now()).getYears()) {
			eligibilityEntity.setBenifitAmt(900.00);
			setEntity(appEntity, eligibilityEntity, selectedPlan);
		} else {
			denial(eligibilityEntity, AppConstants.MEDICARE_DENIED_REASON);
		}
	}

	private void medicaid(AppEntity appEntity, EligibilityEntity eligibilityEntity, String selectedPlan) {
		Double monthlySalary = appEntity.getIncome().getMonthlySalary();
		Double propertyIncome = appEntity.getIncome().getPropertyIncome();
		Double rent = appEntity.getIncome().getRent();
		if (300 >= monthlySalary && (propertyIncome + rent == 0)) {
			setEntity(appEntity, eligibilityEntity, selectedPlan);
			eligibilityEntity.setBenifitAmt(900.00);
		} else {
			denial(eligibilityEntity, AppConstants.MEDICAID_DENIED_REASON);
		}
	}

	private void ccapPlan(AppEntity appEntity, EligibilityEntity eligibilityEntity, String selectedPlan) {
		Double monthlySalary = appEntity.getIncome().getMonthlySalary();
		List<KidEntity> kids = appEntity.getKids();
		if (300 >= monthlySalary && !kids.isEmpty() && kids.stream()
															.allMatch(kid -> kid.getKidAge() <= 16)
															) {
			eligibilityEntity.setBenifitAmt(1500.00);
			setEntity(appEntity, eligibilityEntity, selectedPlan);
		} else {
			denial(eligibilityEntity, AppConstants.CCAP_DENIED_REASON);
		}
	}

	private void snapPlan(AppEntity appEntity, EligibilityEntity eligibilityEntity, String selectedPlan) {
		Double monthlySalary = appEntity.getIncome().getMonthlySalary();
		if (300 >= monthlySalary) {
			eligibilityEntity.setBenifitAmt(1000.00);
			setEntity(appEntity, eligibilityEntity, selectedPlan);
		} else {
			denial(eligibilityEntity, AppConstants.SNAP_DENIED_REASON);
		}
	}

	private void setEntity(AppEntity appEntity, EligibilityEntity eligibilityEntity, String selectedPlan) {
		eligibilityEntity.setPlanStatus(AppConstants.APPROVED_MSG);
		eligibilityEntity.setCreatedDate(LocalDate.now());
		eligibilityEntity.setEndDate(LocalDate.now().plusMonths(6));
	}
	
	private void denial(EligibilityEntity eligibilityEntity, String denialReason) {
		eligibilityEntity.setPlanStatus(AppConstants.DENIED_MSG);
		eligibilityEntity.setDenialReason(denialReason);
	}

}
