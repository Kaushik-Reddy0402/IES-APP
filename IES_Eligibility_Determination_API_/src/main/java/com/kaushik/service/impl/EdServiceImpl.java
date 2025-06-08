package com.kaushik.service.impl;

import java.time.LocalDate;
import java.time.Period;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaushik.bindings.EligibilityForm;
import com.kaushik.constants.AppConstants;
import com.kaushik.entities.AppEntity;
import com.kaushik.entities.EligibilityEntity;
import com.kaushik.entities.KidEntity;
import com.kaushik.entities.NoticeEntity;
import com.kaushik.exceptions.ApplicationNotFound;
import com.kaushik.respository.AppRepo;
import com.kaushik.respository.EligibilityRepo;
import com.kaushik.respository.NoticeRepo;
import com.kaushik.service.EdService;

@Service
public class EdServiceImpl implements EdService {

    @Autowired
    private EligibilityRepo eligibilityRepo;
    
    @Autowired
    private NoticeRepo noticeRepo;

    @Autowired
    private AppRepo appRepo;
    
    @Override
    public void generateCorrespondance(EligibilityForm eligForm, Long caseNo) {

        NoticeEntity eNoticeEntity = new NoticeEntity();
        eNoticeEntity.setPlanName(eligForm.getPlanName());
        eNoticeEntity.setPlanStatus(eligForm.getPlanStatus());
        eNoticeEntity.setStartDate(eligForm.getStartDate());
        eNoticeEntity.setEndDate(eligForm.getEndDate());
        eNoticeEntity.setBenifitAmt(eligForm.getBenefitAmt()); 
        eNoticeEntity.setDenialReason(eligForm.getDenialReason());

        eNoticeEntity.setCaseNo(caseNo);
        eNoticeEntity.setNoticeStatus("PENDING");

        noticeRepo.save(eNoticeEntity);
    }


    @Override
    public EligibilityForm save(Long caseNo) {
        EligibilityForm form = new EligibilityForm();

        AppEntity appEntity = appRepo.findById(caseNo)
                .orElseThrow(() -> new ApplicationNotFound("No application found for case number: " + caseNo));

        EligibilityEntity eligibilityEntity = new EligibilityEntity();
        String selectedPlan = appEntity.getCitizensPlan().getSelectedPlan();

        System.out.println(selectedPlan);
        eligibilityEntity.setPlanName(selectedPlan);
        eligibilityEntity.setCaseNo(appEntity);

        if (AppConstants.PLAN_SNAP.equals(selectedPlan)) {
            snapPlan(appEntity, eligibilityEntity);
        } else if (AppConstants.PLAN_CCAP.equals(selectedPlan)) {
            ccapPlan(appEntity, eligibilityEntity);
        } else if (AppConstants.PLAN_MEDICAID.equals(selectedPlan)) {
            medicaid(appEntity, eligibilityEntity);
        } else if (AppConstants.PLAN_MEDICARE.equals(selectedPlan)) {
            medicare(appEntity, eligibilityEntity);
        } else if (AppConstants.PLAN_RIW.equals(selectedPlan)) {
            riw(appEntity, eligibilityEntity);
        } else {
            denial(eligibilityEntity, "Unsupported Plan");
        }

        eligibilityRepo.save(eligibilityEntity);
        BeanUtils.copyProperties(eligibilityEntity, form);
        form.setBenefitAmt(eligibilityEntity.getBenifitAmt());
        return form;
    }

    private void snapPlan(AppEntity appEntity, EligibilityEntity eligibilityEntity) {
        Double salary = safeValue(appEntity.getIncome() != null ? appEntity.getIncome().getMonthlySalary() : null);

        if (salary <= 300) {
            setEntity(appEntity, eligibilityEntity);
            eligibilityEntity.setBenifitAmt(1000.00);
        } else {
            denial(eligibilityEntity, AppConstants.SNAP_DENIED_REASON);
        }
    }

    private void ccapPlan(AppEntity appEntity, EligibilityEntity eligibilityEntity) {
        Double salary = safeValue(appEntity.getIncome() != null ? appEntity.getIncome().getMonthlySalary() : null);
        List<KidEntity> kids = appEntity.getKids();

        if (salary <= 300 && kids != null && !kids.isEmpty() && kids.stream().allMatch(k -> k.getKidAge() <= 16)) {
            setEntity(appEntity, eligibilityEntity);
            eligibilityEntity.setBenifitAmt(1500.00);
        } else {
            denial(eligibilityEntity, AppConstants.CCAP_DENIED_REASON);
        }
    }

    private void medicaid(AppEntity appEntity, EligibilityEntity eligibilityEntity) {
        var income = appEntity.getIncome();
        if (income == null) {
            denial(eligibilityEntity, AppConstants.MEDICAID_DENIED_REASON);
            return;
        }

        Double salary = safeValue(income.getMonthlySalary());
        Double property = safeValue(income.getPropertyIncome());
        Double rent = safeValue(income.getRent());

        if (salary <= 300 && (property + rent == 0)) {
            setEntity(appEntity, eligibilityEntity);
            eligibilityEntity.setBenifitAmt(900.00);
        } else {
            denial(eligibilityEntity, AppConstants.MEDICAID_DENIED_REASON);
        }
    }

    private void medicare(AppEntity appEntity, EligibilityEntity eligibilityEntity) {
        LocalDate dob = appEntity.getDob();
        if (dob != null && Period.between(dob, LocalDate.now()).getYears() >= 65) {
            setEntity(appEntity, eligibilityEntity);
            eligibilityEntity.setBenifitAmt(900.00);
        } else {
            denial(eligibilityEntity, AppConstants.MEDICARE_DENIED_REASON);
        }
    }

    private void riw(AppEntity appEntity, EligibilityEntity eligibilityEntity) {
        var income = appEntity.getIncome();
        var education = appEntity.getEducation();

        Double salary = safeValue(income != null ? income.getMonthlySalary() : null);
        String gradYear = education != null ? education.getGraduationYear() : null;

        if (salary == 0.0 && gradYear != null && !gradYear.isEmpty()) {
            setEntity(appEntity, eligibilityEntity);
            eligibilityEntity.setBenifitAmt(1100.00);
        } else {
            denial(eligibilityEntity, AppConstants.RIW_DENIED_REASON);
        }
    }

    private void setEntity(AppEntity appEntity, EligibilityEntity eligibilityEntity) {
        eligibilityEntity.setPlanStatus(AppConstants.APPROVED_MSG);
        LocalDate now = LocalDate.now();
        eligibilityEntity.setStartDate(now);
        eligibilityEntity.setEndDate(now.plusMonths(6));
        eligibilityEntity.setCreatedDate(now);
    }

    private void denial(EligibilityEntity entity, String reason) {
        entity.setPlanStatus(AppConstants.DENIED_MSG);
        entity.setDenialReason(reason);
        entity.setStartDate(LocalDate.now());
        entity.setCreatedDate(LocalDate.now());
    }

    private Double safeValue(Double val) {
        return val != null ? val : 0.0;
    }


	@Override
	public List<EligibilityForm> fetchEntities() {
		
		List<EligibilityForm> list= new ArrayList<>();
		List<EligibilityEntity> all = eligibilityRepo.findAll();
		all.forEach(entity -> {
			EligibilityForm form = new EligibilityForm();
			BeanUtils.copyProperties(entity, form);
			form.setBenefitAmt(entity.getBenifitAmt());
			list.add(form);
		});
		return list;
	}

}
