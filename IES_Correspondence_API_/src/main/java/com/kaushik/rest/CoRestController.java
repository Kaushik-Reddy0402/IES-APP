package com.kaushik.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaushik.service.CoService;

@RestController
@RequestMapping("/co")
@CrossOrigin
public class CoRestController {
	
	@Autowired
	private CoService coService;
	
	@GetMapping("/")
	public ResponseEntity<String> prossesRecords() throws Exception {
		coService.processNotices();
		return ResponseEntity.status(HttpStatus.OK).body("");
	}

}
