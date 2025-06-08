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

import com.kaushik.binding.AppForm;
import com.kaushik.binding.CitizenResForm;
import com.kaushik.service.ArService;

@RestController
@RequestMapping("/ar")
@CrossOrigin
public class ArRestController {

	@Autowired
	private ArService arService;
	
	@GetMapping("/apps/{userId}")
	public ResponseEntity<List<CitizenResForm>> citizens(@PathVariable Integer userId) {
		
		List<CitizenResForm> fetchEntities = arService.fetchEntities(userId);
		
		return ResponseEntity.status(HttpStatus.OK)
							.body(fetchEntities);
	}
	
	@PostMapping("/register/{userId}")
	public ResponseEntity<String> validateCitizen(@RequestBody AppForm form , @PathVariable Integer userId) {
		
		String status = arService.applicationCreation(form, userId);
		
		return ResponseEntity.status(HttpStatus.OK)
							.body(status);
	}
}
