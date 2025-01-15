package com.kaushik.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaushik.entities.EligibilityEntity;

public interface EligibilityRepo extends JpaRepository<EligibilityEntity, Integer>{

	public EligibilityEntity findByCaseNo(Long caseNo);
}
