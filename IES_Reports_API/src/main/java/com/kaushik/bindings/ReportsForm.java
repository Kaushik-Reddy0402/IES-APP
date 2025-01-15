package com.kaushik.bindings;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReportsForm {

	private String planName;
	
	private String planStatus;
	
	private String gender;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
}
