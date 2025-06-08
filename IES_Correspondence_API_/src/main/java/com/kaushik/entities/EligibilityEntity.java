package com.kaushik.entities;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ELIGIBILITY_TBL") // Add the table name for the entity
public class EligibilityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ELIGIBILITY_ENTITY_ID") 
    private Integer eligibiltyEntityId;

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
    
    @Column(name = "CREATED_DATE", updatable = false) 
    @CreationTimestamp
    private LocalDate createdDate;

    @OneToOne
    @JoinColumn(name = "CASE_NUM")  
    private AppEntity caseNo;
    
}

