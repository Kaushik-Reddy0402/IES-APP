package com.kaushik.rest;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kaushik.binding.AppForm;
import com.kaushik.binding.CitizenResForm;

@RestController
@CrossOrigin
public class SsaRestController {

	private static final Map<String, List<String>> STATE_CITY_MAP = Map.of(
			"Rhode Island", List.of("Providence", "Warwick", "Cranston"),
			"California", List.of("Los Angeles", "San Francisco", "San Diego"),
			"Texas", List.of("Houston", "Austin", "Dallas"),
			"New York", List.of("New York City", "Buffalo", "Rochester")
		);

	private static final Random RANDOM = new Random();

	@PostMapping("/verify-ssn")
	public ResponseEntity<CitizenResForm> verifySSN(@RequestBody AppForm appForm) {

		CitizenResForm response = new CitizenResForm();
		BeanUtils.copyProperties(appForm, response);  // Copies fullName, email, phoneNo, gender, dob, ssn

		response.setHouseNo(String.valueOf(100 + RANDOM.nextInt(900)));

		String state;

		if (appForm.getSsn() % 10 == 9) {
			state = "Rhode Island";
		} else {
			// Select a random state except Rhode Island
			List<String> otherStates = STATE_CITY_MAP.keySet().stream()
					.filter(s -> !s.equals("Rhode Island"))
					.toList();
			state = otherStates.get(RANDOM.nextInt(otherStates.size()));
		}
		response.setState(state);
		
		// Select a random city from that state
		List<String> cities = STATE_CITY_MAP.get(state);
		String city = cities.get(RANDOM.nextInt(cities.size()));
		response.setCity(city);
		
		return ResponseEntity.ok(response);
	}
}

//	private static final List<String> STATES = List.of("Rhode Island");
//	private static final Random RANDOM = new Random();
//
//	@PostMapping("/verify-ssn")
//	public ResponseEntity<CitizenResForm> verifySSN(@RequestBody AppForm appForm) {
//
//			CitizenResForm response = new CitizenResForm();
//
//			response.setFullName(appForm.getFullName());
//			System.out.println(appForm.getEmail());
//			response.setEmail(appForm.getEmail());
//			response.setPhoneNo(appForm.getPhoneNo());
//			response.setGender(appForm.getGender());
//			response.setDob(appForm.getDob());
//			response.setSsn(appForm.getSsn());
//			response.setHouseNo(String.valueOf(100 + RANDOM.nextInt(900)));
//
//			int index = RANDOM.nextInt(STATES.size());
//			response.setState(STATES.get(index));
//
//			return ResponseEntity.ok(response);
//
//	}

