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

import com.kaushik.bindings.EducationForm;
import com.kaushik.bindings.IncomeForm;
import com.kaushik.bindings.KidsForm;
import com.kaushik.bindings.PlanSelectionForm;
import com.kaushik.bindings.SummaryScreen;
import com.kaushik.service.DcService;

@RestController
@RequestMapping("/dc")
@CrossOrigin
public class DcRestController {

	@Autowired
	private DcService service;
	
	@GetMapping("/summary/{caseNo}")
	public ResponseEntity<SummaryScreen> summary (@PathVariable Long caseNo) {
		SummaryScreen summary = service.summary(caseNo);
		return ResponseEntity.status(HttpStatus.OK).body(summary);
	}
	
	
	@GetMapping("/all-plans")
	public ResponseEntity<List<String>> getPlans() {
		List<String> fetchPlans = service.fetchPlans();
		return ResponseEntity.status(HttpStatus.OK)
								.body(fetchPlans);
	}
	
	@PostMapping("/plan")
	public ResponseEntity<String> savePlan(@RequestBody PlanSelectionForm form) {
		
		service.savePlan(form);
		return  ResponseEntity.status(HttpStatus.OK).body("Success");
	}

	@PostMapping("/education")
	public ResponseEntity<String> saveEducation(@RequestBody EducationForm form) {

		service.saveEducationDetails(form);
		return ResponseEntity.status(HttpStatus.OK).body("Success");
	}

	@PostMapping("/income")
	public ResponseEntity<String> saveIncomeDetails(@RequestBody IncomeForm form) {
		service.saveIncomeDetails(form);
		return ResponseEntity.status(HttpStatus.OK).body("Success");
	}

	@PostMapping("/kids")
	public ResponseEntity<String> saveKids(@RequestBody KidsForm form) {
		service.saveKidsDetails(form);
		return ResponseEntity.status(HttpStatus.OK).body("Success");
	}
	
}
