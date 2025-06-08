package com.kaushik.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.kaushik.entities.PlansEntity;

public interface PlansRepo extends JpaRepository<PlansEntity, Integer> {

	@Modifying
	@Transactional
	@Query("UPDATE PlansEntity p SET p.activeSw = :status WHERE p.planId = :planId")
	public int changePlanStatus(@Param("planId") Integer planId, @Param("status") String status);

}