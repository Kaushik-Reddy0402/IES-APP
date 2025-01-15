package com.kaushik.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.kaushik.bindings.EligibilityForm;
import com.kaushik.service.EdService;
@RestController
@CrossOrigin
public class EdRestController {

	@Autowired
	private EdService service;

	@GetMapping("/determination/{caseNo}")
	public ResponseEntity<EligibilityForm> determineStatus(@PathVariable Long caseNo) {
		
		EligibilityForm form = service.save(caseNo);
		return ResponseEntity.status(HttpStatus.OK).body(form);
	}
	
//	@GetMapping("/forms/{userId}")
//	public ResponseEntity<List<EligibilityForm>> fetchEntities(@PathVariable Integer userId) {
//
//		List<EligibilityForm> fetchEntities = service.fetchEntities(userId);
//		return ResponseEntity.status(HttpStatus.OK).body(fetchEntities);
//
//	}
	
	
}
