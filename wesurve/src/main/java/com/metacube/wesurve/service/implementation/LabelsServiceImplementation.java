package com.metacube.wesurve.service.implementation;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.metacube.wesurve.dao.labels.LabelsDao;
import com.metacube.wesurve.enums.SurveyStatus;
import com.metacube.wesurve.model.Labels;
import com.metacube.wesurve.model.Survey;
import com.metacube.wesurve.model.User;
import com.metacube.wesurve.service.LabelsService;

@Service("labelService")
public class LabelsServiceImplementation implements LabelsService {

	@Resource(name = "hibernateLabelsDaoImplementation")
	LabelsDao labelsDao;
	
	@Override
	public Set<Labels> getAll() {
		Set<Labels> labels;
		try {
			labels = (Set<Labels>) labelsDao.findAll();
		} catch (Exception exception) {
			exception.printStackTrace();
			labels = null;
		}
		
		return labels;
	}

	@Override
	public Labels getLabelByLabelName(String label) {
		Labels labelModel;
		try {
			labelModel = labelsDao.getByLabelName(label);
		} catch (Exception exception) {
			labelModel = null;
			exception.printStackTrace();
		}
	
		return labelModel;
	}
	
	@Override
    public Set<Survey> getMatchedSurveyByLabelName(User surveyor, String searchString) {
   	  Iterable<Labels> labelList =labelsDao.getLabelsByNameAlike(searchString);
   	 
   	  Set<Survey> surveyMatchedBytheLabel = new HashSet<>();
   	 
   	  if(labelList != null) {
   		  for( Labels curLabel : labelList ) {
   			  Set<Survey> surveyObj = curLabel.getSurvey();
   			  if(surveyObj != null) {
   				  for(Survey curSurvey : surveyObj) {
   					  if(curSurvey.getSurveyStatus() != SurveyStatus.DELETED && curSurvey.getSurveyOwner().getUserId() == surveyor.getUserId()) {
   						  surveyMatchedBytheLabel.add(curSurvey);
   					  }
   				  }
   			  }
   		  }
   	  }
   	 return surveyMatchedBytheLabel;
    }
}