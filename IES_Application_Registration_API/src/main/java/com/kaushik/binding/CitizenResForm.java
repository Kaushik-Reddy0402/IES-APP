package com.kaushik.binding;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CitizenResForm {

	private String fullName;

	private String email;

	private String phoneNo;

	private String gender;

	private LocalDate dob;

	private Long ssn;
	
	private String houseNo;
	
	private String city;
	
	private String state;

}
