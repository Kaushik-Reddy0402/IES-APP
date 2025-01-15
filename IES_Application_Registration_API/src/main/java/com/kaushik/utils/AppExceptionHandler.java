package com.kaushik.utils;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kaushik.binding.ExceptionInfo;
import com.kaushik.exceptions.SSAWebException;
import com.kaushik.exceptions.UserNotFoundException;

@RestControllerAdvice
public class AppExceptionHandler {

	@ExceptionHandler(value = SSAWebException.class)
	public ResponseEntity<ExceptionInfo> handleException(SSAWebException exception) {
		ExceptionInfo info = new ExceptionInfo();
		
		info.setExId("EX001");
		info.setExMsg(exception.getMessage());
		info.setWhen(LocalDateTime.now());
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body(info);
	}
	
	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<ExceptionInfo> handleUNFException(UserNotFoundException exception) {
		ExceptionInfo info = new ExceptionInfo();
		
		info.setExId("EX002");
		info.setExMsg(exception.getMessage());
		info.setWhen(LocalDateTime.now());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							 .body(info);
	}
	
}
