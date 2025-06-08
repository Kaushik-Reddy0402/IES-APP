package com.kaushik.entities;

import java.time.LocalDate;

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
@Table(name = "NOTICE_TBL")
public class NoticeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer noticeId;

	@Column(name = "PLAN_NAME", nullable = false)
	private String planName;

	@Column(name = "PLAN_STATUS", nullable = false)
	private String planStatus;

	@Column(name = "START_DATE")
	private LocalDate startDate;

	@Column(name = "END_DATE")
	private LocalDate endDate;

	@Column(name = "BENEFIT_AMOUNT")
	private Double benifitAmt;

	@Column(name = "DENIAL_REASON")
	private String denialReason;
	
	@Column(name = "NOTICE_STATUS")
	private String noticeStatus;
	
	@Column(name = "URL")
	private String noticeUrl;
	
	private Long caseNo;
	
	private Integer eligibiltyEntityId;
	
}
