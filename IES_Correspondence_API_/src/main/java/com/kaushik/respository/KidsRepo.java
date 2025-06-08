package com.kaushik.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaushik.entities.KidEntity;

public interface KidsRepo extends JpaRepository<KidEntity, Integer>{

}
