package com.kaushik.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "KIDS_TBL")
public class KidEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "KID_ID")
    private Integer kidId;

    @Column(name = "KID_NAME")
    private String kidName;

    @Column(name = "KID_AGE")
    private Integer kidAge;

    @Column(name = "KID_SSN")
    private Long ssn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CASE_NUM") 
    private AppEntity application; 
}

