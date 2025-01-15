package com.kaushik.entities;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "PLANS_TABLE")
public class PlansEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer planId;
	
	@Column(name = "PLAN_NAME", nullable = false)
	private String planName;

	@Column(name = "PLAN_START_DATE", nullable = false)
	private LocalDate planStartDate;

	@Column(name = "PLAN_END_DATE", nullable = false)
	private LocalDate planEndDate;

	@Column(name = "PLAN_CATEGORY", nullable = false)
	private String planCategory;

	@Column(name = "ACTIVE_SW", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
	private String activeSw;

	@Column(name = "CREATED_DATE")
	@CreationTimestamp
	private LocalDate createdDate;

	@Column(name = "UPDATED_DATE")
	@UpdateTimestamp
	private LocalDate updatedDate;

}
