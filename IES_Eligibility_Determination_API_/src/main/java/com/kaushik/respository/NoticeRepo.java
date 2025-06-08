package com.kaushik.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaushik.entities.NoticeEntity;

public interface NoticeRepo extends JpaRepository<NoticeEntity, Integer>{

	public List<NoticeEntity> findByNoticeStatus(String status);
}
