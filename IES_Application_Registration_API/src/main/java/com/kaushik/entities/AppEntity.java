package com.kaushik.entities;

import java.time.LocalDate;

import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "APP_TBL")
public class AppEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CASE_NUM")
	private Long caseNo;

	@Column(name = "FULL_NAME", nullable = false)
	private String fullName;

	@Column(name = "CITIZEN_EMAIL", nullable = false)
	private String email;

	@Column(name = "CITIZEN_PHNO")
	private String phoneNo;

	@Column(name = "CITIZEN_GENDER")
	private String gender;

	@Column(name = "CITIZEN_DOB")
	private LocalDate dob;

	@Column(name = "CITIZEN_SSN")
	private Long ssn;

	@Column(name = "CITIZEN_HOUSENUM")
	private String houseNo;

	@Column(name = "CITY")
	private String city;

	@Column(name = "STATE")
	private String state;

	@CreationTimestamp
	@Column(name = "CREATED_DATE")
	private LocalDate createDate;

	@ManyToOne
    @JoinColumn(name = "CREATED_BY", nullable = true) 
    private UserEntity createdBy;
	
	@OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<KidEntity> kids;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CITIZEN_INCOME")
	private IncomeEntity income;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CITIZEN_EDUCATION")
	private EducationEntity education;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CITIZEN_PLAN")
	private DcPlans citizensPlan;

	@OneToOne(mappedBy = "caseNo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private EligibilityEntity eligibility;
}
