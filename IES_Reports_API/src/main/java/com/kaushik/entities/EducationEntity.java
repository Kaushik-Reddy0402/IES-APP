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
@Table(name = "EDUCATION_TBL")
public class EducationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EDUCATION_ID")
    private Integer educationId;

    @Column(name = "HIGHEST_DEGREE")
    private String highestDegree;

    @Column(name = "GRADUATION_YEAR")
    private String graduationYear;

    @Column(name = "UNIVERSITY_NAME")
    private String universityName;

    @OneToOne(mappedBy = "education", fetch = FetchType.LAZY)
    private AppEntity application; // Renamed from `caseNum` for clarity
}
