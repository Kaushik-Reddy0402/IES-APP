package com.kaushik.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaushik.entities.AppEntity;
import com.kaushik.entities.EligibilityEntity;

public interface EligibilityRepo extends JpaRepository<EligibilityEntity, Integer>{

	EligibilityEntity findByCaseNo(AppEntity caseNo);


}
