package com.kaushik.binding;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AppForm {

	private String fullName;
	
	private String email;
	
	private String phoneNo;
	
	private String gender;
	
	private LocalDate dob;
	
	private Long ssn;
	
}
