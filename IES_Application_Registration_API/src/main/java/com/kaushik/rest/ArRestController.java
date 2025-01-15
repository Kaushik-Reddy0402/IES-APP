package com.kaushik.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kaushik.binding.AppForm;
import com.kaushik.binding.CitizenResForm;
import com.kaushik.service.ArService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@CrossOrigin
public class ArRestController {

	private ArService arService;
	
	@GetMapping("/apps")
	public ResponseEntity<List<CitizenResForm>> citizens(@RequestParam Integer userId) {
		
		List<CitizenResForm> fetchEntities = arService.fetchEntities(userId);
		
		return ResponseEntity.status(HttpStatus.OK)
							.body(fetchEntities);
	}
	
	@PostMapping("/save")
	public ResponseEntity<String> validateCitizen(@RequestBody AppForm form , @RequestParam Integer userId) {
		
		String status = arService.applicationCreation(form, userId);
		
		return ResponseEntity.status(HttpStatus.OK)
							.body(status);
	}
}
