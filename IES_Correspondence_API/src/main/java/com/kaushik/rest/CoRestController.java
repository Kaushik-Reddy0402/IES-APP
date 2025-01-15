package com.kaushik.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.kaushik.service.CoService;

@RestController
@CrossOrigin
public class CoRestController {
	
	@Autowired
	private CoService coService;
	
	public ResponseEntity<String> prossesRecords() throws Exception {
		coService.processNotices();
		return ResponseEntity.status(HttpStatus.OK).body("");
	}

}
