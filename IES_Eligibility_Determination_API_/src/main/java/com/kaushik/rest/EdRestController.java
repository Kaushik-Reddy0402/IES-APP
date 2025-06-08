package com.kaushik.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaushik.bindings.EligibilityForm;
import com.kaushik.service.EdService;

@RestController
@RequestMapping("/ed")
@CrossOrigin
public class EdRestController {

	@Autowired
	private EdService service;

	@GetMapping("/determination/{caseNo}")
	public ResponseEntity<EligibilityForm> determineStatus(@PathVariable Long caseNo) {
		
		EligibilityForm form = service.save(caseNo);
		return ResponseEntity.status(HttpStatus.OK).body(form);
	}
	
	@PostMapping("/generate/{caseNo}")
	public ResponseEntity<String> correspondence(@RequestBody EligibilityForm form, @PathVariable Long caseNo) {
		service.generateCorrespondance(form, caseNo);
		return ResponseEntity.status(HttpStatus.OK).body("Correspondence Generated");
	}
	
	@GetMapping("/forms/{userId}")
	public ResponseEntity<List<EligibilityForm>> fetchEntities() {

		List<EligibilityForm> fetchEntities = service.fetchEntities();
		return ResponseEntity.status(HttpStatus.OK).body(fetchEntities);

	}
	
	
}
