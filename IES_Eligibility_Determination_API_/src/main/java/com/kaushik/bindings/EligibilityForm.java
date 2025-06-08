package com.kaushik.bindings;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EligibilityForm {
	
	private String planName;
	
	private String planStatus;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private Double benefitAmt;
	
	private String denialReason;

}
