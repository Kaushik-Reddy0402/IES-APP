package com.kaushik.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kaushik.binding.AppForm;
import com.kaushik.binding.CitizenResForm;
import com.kaushik.entities.AppEntity;
import com.kaushik.entities.UserEntity;
import com.kaushik.exceptions.SSAWebException;
import com.kaushik.exceptions.UserNotFoundException;
import com.kaushik.respository.AppRepo;
import com.kaushik.respository.UserRepo;
import com.kaushik.service.ArService;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Service
public class ArServiceImpl implements ArService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private AppRepo appRepo;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public List<CitizenResForm> fetchEntities(Integer userId) {

		UserEntity userEntity = userRepo.findById(userId)
										.orElseThrow(() -> {
											throw new UserNotFoundException();
										});
		
		List<AppEntity> appEntities;
		
		if (1 == userEntity.getRoleId()) {
			appEntities = appRepo.findAll();
		} else {
			appEntities = appRepo.findCwById(userId);
		}
		
		return appEntities.stream()
							.map(entity -> {
								CitizenResForm form = new CitizenResForm();
								BeanUtils.copyProperties(entity, form);
								return form;
							}).collect(Collectors.toList());
	}

	@Override
	public String applicationCreation(AppForm citizenReqForm, Integer useId) {
		
		try {
//			WebClient webClient = WebClient.create(URI_STRING2);

//			CitizenResForm citizenResForm = sendReqSSA(citizenReqForm, webClient);
			CitizenResForm citizenResForm = sendReqSSAUsingRestTemplate(citizenReqForm);
			
			if(citizenResForm == null) {
				return "SSA response is null. Request not sent."; 
			} else {
				if ("Rhode Island".equals(citizenResForm.getState())) {
					AppEntity entity = new AppEntity();

					userRepo.findById(useId).ifPresentOrElse(user -> {
						entity.setCreatedBy(user);
						
					}, () -> {
						throw new UserNotFoundException("UserId not found");
					});
					
					BeanUtils.copyProperties(citizenResForm, entity);
					System.out.println(entity.getEmail());
					System.out.println(entity.getFullName() + " " + entity.getGender());
					appRepo.save(entity);
					
					return "Successfully Application created with case number " + entity.getCaseNo();
				} 
			}
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		    throw new SSAWebException("Error calling SSA API: " + e.getMessage());
		}
		return "Invalid SSN NO ";
		
	}
	
	public CitizenResForm sendReqSSAUsingRestTemplate(AppForm appForm) {
	    String uri = "http://localhost:8081/verify-ssn";

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    HttpEntity<AppForm> requestEntity = new HttpEntity<>(appForm, headers);

	    ResponseEntity<CitizenResForm> response = restTemplate.postForEntity(
	        uri,
	        requestEntity,
	        CitizenResForm.class
	    );

	    return response.getBody();
	}

//	private static CitizenResForm sendReqSSA(AppForm appForm, WebClient webClient) {
//		return webClient.post()
//						.uri(URI_STRING)
//						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//						.bodyValue(appForm)
//						.retrieve()
//						.bodyToMono(CitizenResForm.class)
//						.block();
//	}

}
