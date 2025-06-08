package com.kaushik.utils;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kaushik.exceptions.ExceptionInfo;
import com.kaushik.exceptions.UserNotFoundException;

@RestControllerAdvice
public class HandleException {

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ExceptionInfo> handleExc(Exception ex) {
		ExceptionInfo info = new ExceptionInfo();
		
		info.setExId("EX001");
		info.setExMsg(ex.getMessage());
		info.setWhen(LocalDateTime.now());
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body(info);
	}
	
	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<ExceptionInfo> handleUNF(UserNotFoundException ex) {
		ExceptionInfo info = new ExceptionInfo();
		
		info.setExId("EX002");
		info.setExMsg(ex.getMessage());
		info.setWhen(LocalDateTime.now());
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body(info);
	}
	
}
