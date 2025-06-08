package com.kaushik.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaushik.entities.IncomeEntity;

public interface IncomeRepo extends JpaRepository<IncomeEntity, Integer>{

}
