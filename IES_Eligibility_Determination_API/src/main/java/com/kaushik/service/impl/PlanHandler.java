package com.kaushik.service.impl;

import com.kaushik.entities.AppEntity;
import com.kaushik.entities.EligibilityEntity;

@FunctionalInterface
public interface PlanHandler {
	void handle(AppEntity appEntity, EligibilityEntity eligibilityEntity, String selectedPlan);
}
