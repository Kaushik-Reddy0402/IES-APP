package com.kaushik.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kaushik.entities.UserEntity;

import jakarta.transaction.Transactional;

public interface UserRepo extends JpaRepository<UserEntity, Integer> {

	@Modifying
	@Transactional
	@Query("UPDATE UserEntity u SET u.activeSw = :status WHERE u.userId = :userId")
	int changeAccountStatus(@Param("userId") Integer userId, @Param("status") String status);

	public UserEntity findByEmail(String emailId);
	
	public UserEntity findByEmailAndPwd(String email, String pwd);
}
