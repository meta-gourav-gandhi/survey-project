package com.metacube.wesurve.service;

import java.util.Set;

import com.metacube.wesurve.model.Labels;
import com.metacube.wesurve.model.Survey;
import com.metacube.wesurve.model.User;

public interface LabelsService {
	Set<Labels> getAll();
	Labels getLabelByLabelName(String label);
	Set<Survey> getMatchedSurveyByLabelName(User surveyor, String searchString);
}