package com.kaushik.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kaushik.entities.AppEntity;

public interface AppRepo extends JpaRepository<AppEntity, Long> {

	 @Query("from AppEntity a where a.createdBy.id = :userId")
	 List<AppEntity> findCwById(@Param("userId") Integer userId);
	
}
