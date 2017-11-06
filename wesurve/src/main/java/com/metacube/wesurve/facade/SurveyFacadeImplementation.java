package com.metacube.wesurve.facade;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metacube.wesurve.dto.OptionDto;
import com.metacube.wesurve.dto.QuestionDto;
import com.metacube.wesurve.dto.ResponseDto;
import com.metacube.wesurve.dto.SurveyDto;
import com.metacube.wesurve.dto.SurveyResponseDto;
import com.metacube.wesurve.enums.Role;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.model.Labels;
import com.metacube.wesurve.model.Options;
import com.metacube.wesurve.model.Questions;
import com.metacube.wesurve.model.Survey;
import com.metacube.wesurve.model.User;
import com.metacube.wesurve.service.LabelsService;
import com.metacube.wesurve.service.SurveyService;
import com.metacube.wesurve.service.UserService;
import com.metacube.wesurve.utils.StringUtils;

@Component("surveyFacade")
public class SurveyFacadeImplementation implements SurveyFacade {

	@Autowired
	UserService userService;
	
	@Autowired
	SurveyService surveyService;
	
	@Autowired
	LabelsService labelsService;
	
	@Override
	public ResponseDto<SurveyResponseDto> createSurvey(int surveyorId, SurveyDto surveyDto) {
		ResponseDto<SurveyResponseDto> response = new ResponseDto<>();
		Status status;
		SurveyResponseDto surveyResponse = null;
		if(validateSurvey(surveyDto)) {
			User user = userService.getUserById(surveyorId);
			Survey survey = convertDtoToModel(surveyDto);
			
			user.getCreatedSurveyList().add(survey);
			
			//Set surveyor as viewer of survey
			survey.getViewers().add(user);
			
			Survey surveyResult = surveyService.createSurvey(survey);
			userService.update(user);
			
			String url = "http://172.16.33.117/survey/" + surveyResult.getSurveyId() + "/";
			surveyResponse = new SurveyResponseDto();
			surveyResponse.setId(surveyResult.getSurveyId());
			surveyResponse.setName(surveyResult.getSurveyName());
			surveyResponse.setUrl(url);
			status = Status.SUCCESS;
		} else {
			status = Status.INVALID_CONTENT;
		}
		
		response.setStatus(status);
		response.setBody(surveyResponse);
		return response;
	}

	private Survey convertDtoToModel(SurveyDto surveyDto) {
		Survey survey = new Survey();
		survey.setSurveyName(surveyDto.getText());
		survey.setDescription(surveyDto.getDescription());
		survey.setUpdatedDate(new Date());
		if(surveyDto.getId() <= 0) {
			survey.setCreatedDate(new Date());	
		}
		
		survey.setQuestions(convertDtoToModelQuestion(surveyDto.getQuestions()));
		survey.setLabels(changeTosetOfLabels(surveyDto.getLabels()));
		return survey;
	}

	private Set<Questions> convertDtoToModelQuestion(Set<QuestionDto> questionsDto) {
		Set<Questions> questions = new HashSet<>();
		Questions currQuestion;
		
		for(QuestionDto quesDto : questionsDto) {
			currQuestion = new Questions();
			currQuestion.setQuestion(quesDto.getText());
			currQuestion.setRequired(quesDto.isRequired());
			currQuestion.setOptions(convertDtoToModelOption(quesDto.getOptions()));
			currQuestion.setUpdatedDate(new Date());
			if(currQuestion.getQuesId() <= 0) {
				currQuestion.setCreatedDate(new Date());	
			}
			
			questions.add(currQuestion);
		}
		
		return questions;
	}

	private Set<Options> convertDtoToModelOption(Set<OptionDto> optionsDto) {
		Set<Options> options = new HashSet<>();
		Options currOption;
		
		for(OptionDto optionDto : optionsDto) {
			currOption = new Options();
			currOption.setOptionValue(optionDto.getText());
			currOption.setOptionType(optionDto.getType());
			currOption.setUpdatedDate(new Date());
			if(currOption.getOptionId() <= 0) {
				currOption.setCreatedDate(new Date());	
			}
			
			options.add(currOption);
		}
		
		return options;
	}

	private Set<Labels> changeTosetOfLabels(Set<String> labels) {
		Set<Labels> setOflabels = new HashSet<>();
		Labels curlabel; 
		for(String label : labels) {
			curlabel = new Labels();
			curlabel.setLabelName(label);
			setOflabels.add(curlabel);
		}
		
		return setOflabels;
	}

	private boolean validateSurvey(SurveyDto survey) {
		boolean result = false;
		boolean condition1 = StringUtils.validateString(survey.getText()) &&
				StringUtils.validateString(survey.getDescription());
				
		boolean condition2 = survey.getQuestions() != null && survey.getQuestions().size() != 0;
		
		if(condition1 && condition2) {
			Iterator<QuestionDto> iter = survey.getQuestions().iterator();
			QuestionDto question;
			boolean tempResult = true;
			while(iter.hasNext()) {
				question = iter.next();
				condition1 = StringUtils.validateString(question.getText());
				condition2 = survey.getQuestions() != null && question.getOptions().size() != 0;
				
				if(!(condition1 && condition2)) {
					tempResult = false;
					break;
				}
			}
			
			result = tempResult;
		}
				
		return result;
	}

	@Override
	public Role checkAuthorization(String accessToken) {
		return userService.checkAuthorization(accessToken);
	}
}