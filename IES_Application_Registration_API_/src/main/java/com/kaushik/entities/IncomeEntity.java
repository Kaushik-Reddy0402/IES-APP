package com.kaushik.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "INCOME_TBL")
public class IncomeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INCOME_ID")
    private Integer incomeId;

    @Column(name = "MONTHLY_SALARY")
    private Double monthlySalary;

    @Column(name = "RENT")
    private Double rent;

    @Column(name = "PROPERTY_INCOME")
    private Double propertyIncome;

    @OneToOne(mappedBy = "income", fetch = FetchType.LAZY)
    private AppEntity application; 
    
}
