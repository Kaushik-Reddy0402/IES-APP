package com.kaushik.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DcPlans {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer citizenPlanId;
	
	private String selectedPlan;
	
	@OneToOne(mappedBy = "citizensPlan", fetch = FetchType.LAZY)
	private AppEntity caseNum;
	
}
