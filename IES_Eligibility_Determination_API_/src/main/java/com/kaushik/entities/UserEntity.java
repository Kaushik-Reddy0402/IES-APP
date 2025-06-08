package com.kaushik.entities;

import java.time.LocalDate;

import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "IES_USERS")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Integer userId;

	@Column(name = "FULL_NAME", nullable = false)
	private String fullName;

	@Column(name = "USER_EMAIL", nullable = false)
	private String email;

	@Column(name = "USER_PWD")
	private String pwd;

	@Column(name = "USER_PHNO")
	private String phoneNo;

	@Column(name = "USER_GENDER")
	private String gender;

	@Column(name = "USER_DOB")
	private LocalDate dateOfBirth;

	@Column(name = "USER_SSN")
	private String ssn;

	@Column(name = "ACTIVE_SW", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
	private String activeSw;

	@Column(name = "ACC_STATUS", nullable = false, columnDefinition = "VARCHAR(10) DEFAULT 'LOCKED'")
	private String accountStatus;

	@Column(name = "ROLE_ID", nullable = false)
	private Integer roleId;

	@CreationTimestamp
	@Column(name = "CREATE_DATE", updatable = false)
	private LocalDate createDate;

	@UpdateTimestamp
	@Column(name = "UPDATE_DATE")
	private LocalDate updateDate;

	@OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<AppEntity> applications;
	
}
