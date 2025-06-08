package com.kaushik.exception;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ExceptionInfo {

	private String exId;
	
	private String exMsg;
	
	private LocalDateTime when;
	
}
