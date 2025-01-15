package com.kaushik.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import com.kaushik.binding.AppForm;
import com.kaushik.binding.CitizenResForm;
import com.kaushik.entities.AppEntity;
import com.kaushik.entities.UserEntity;
import com.kaushik.exceptions.SSAWebException;
import com.kaushik.exceptions.UserNotFoundException;
import com.kaushik.respository.AppRepo;
import com.kaushik.respository.UserRepo;
import com.kaushik.service.ArService;

public class ArServiceImpl implements ArService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private AppRepo appRepo;

	private static final String URI_STRING = "https://localhost:9090";
	
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
			WebClient webClient = WebClient.create(URI_STRING);

			CitizenResForm citizenResForm = sendReqSSA(citizenReqForm, webClient);

			if ("Rhode Island".equals(citizenResForm.getState())) {
				AppEntity entity = new AppEntity();

				userRepo.findById(useId).ifPresentOrElse(user -> {
					entity.setCreatedBy(user);
				}, () -> {
					throw new UserNotFoundException("UserId not found");
				});
				
				BeanUtils.copyProperties(citizenResForm, entity);
				appRepo.save(entity);
				
				return "Successfully Application created with case number " + entity.getCaseNo();
			} 
			
		} catch (Exception e) {
			
			throw new SSAWebException();
			
		}
		return "Invalid SSN NO ";
		
	}

	private CitizenResForm sendReqSSA(AppForm appForm, WebClient webClient) {
		return webClient.post()
						.uri("/state/{ssn}", appForm.getSsn())
						.bodyValue(appForm)
						.retrieve()
						.bodyToMono(CitizenResForm.class)
						.block();
	}

}
