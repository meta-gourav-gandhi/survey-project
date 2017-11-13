package com.metacube.wesurve.facade.implementation;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metacube.wesurve.dto.OptionDto;
import com.metacube.wesurve.dto.QuestionDto;
import com.metacube.wesurve.dto.QuestionResponseDto;
import com.metacube.wesurve.dto.QuestionResultDto;
import com.metacube.wesurve.dto.ResponderDto;
import com.metacube.wesurve.dto.ResponseDto;
import com.metacube.wesurve.dto.SurveyDto;
import com.metacube.wesurve.dto.SurveyInfoDto;
import com.metacube.wesurve.dto.SurveyResponseDto;
import com.metacube.wesurve.dto.SurveyResultDto;
import com.metacube.wesurve.enums.Role;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.enums.SurveyStatus;
import com.metacube.wesurve.facade.SurveyFacade;
import com.metacube.wesurve.model.Labels;
import com.metacube.wesurve.model.Options;
import com.metacube.wesurve.model.Questions;
import com.metacube.wesurve.model.Survey;
import com.metacube.wesurve.model.User;
import com.metacube.wesurve.model.UserResponses;
import com.metacube.wesurve.service.LabelsService;
import com.metacube.wesurve.service.QuestionsService;
import com.metacube.wesurve.service.SurveyService;
import com.metacube.wesurve.service.UserResponsesService;
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

	@Autowired
	UserResponsesService userResponsesService;

	@Autowired
	QuestionsService questionsService;

	@Override
	public ResponseDto<SurveyResponseDto> createSurvey(int surveyorId, SurveyDto surveyDto) {
		ResponseDto<SurveyResponseDto> response = new ResponseDto<>();
		Status status;
		SurveyResponseDto surveyResponse = null;
		if (validateSurvey(surveyDto)) {
			User user = userService.getUserById(surveyorId);
			Survey survey = convertDtoToModel(surveyDto, new Date());

			survey.setSurveyOwner(user);
			user.getCreatedSurveyList().add(survey);

			// Set surveyor as viewer of survey
			survey.getViewers().add(user);

			Survey surveyResult = surveyService.createSurvey(survey);
			userService.update(user);

			String url = surveyService.getSurveyURL(surveyResult);
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

	private Survey convertDtoToModel(SurveyDto surveyDto, Date createdDate) {
		Survey survey = new Survey();
		if (surveyDto.getId() > 0) {
			survey.setSurveyId(surveyDto.getId());
		}
		survey.setSurveyName(surveyDto.getName());
		survey.setDescription(surveyDto.getDescription());
		survey.setUpdatedDate(new Date());
		survey.setCreatedDate(createdDate);
		survey.setQuestions(convertDtoToModelQuestion(surveyDto.getQuestions(), createdDate));
		survey.setLabels(changeStringSetToLabelsSet(surveyDto.getLabels()));
		return survey;
	}

	private Set<Questions> convertDtoToModelQuestion(Set<QuestionDto> questionsDto, Date createdDate) {
		Set<Questions> questions = new HashSet<>();
		Questions currQuestion;

		for (QuestionDto quesDto : questionsDto) {
			currQuestion = new Questions();
			if (quesDto.getId() > 0) {
				currQuestion.setQuesId(quesDto.getId());
			}

			currQuestion.setQuestion(quesDto.getText());
			currQuestion.setRequired(quesDto.isRequired());
			currQuestion.setOptions(convertDtoToModelOption(quesDto.getOptions(), createdDate));
			currQuestion.setUpdatedDate(new Date());
			currQuestion.setCreatedDate(createdDate);

			questions.add(currQuestion);
		}

		return questions;
	}

	private Set<Options> convertDtoToModelOption(Set<OptionDto> optionsDto, Date createdDate) {
		Set<Options> options = new HashSet<>();
		Options currOption;

		for (OptionDto optionDto : optionsDto) {
			currOption = new Options();
			if (optionDto.getId() > 0) {
				currOption.setOptionId(optionDto.getId());
			}

			currOption.setOptionValue(optionDto.getText());
			currOption.setOptionType(optionDto.getType());
			currOption.setUpdatedDate(new Date());
			currOption.setCreatedDate(createdDate);

			options.add(currOption);
		}

		return options;
	}

	private Set<Labels> changeStringSetToLabelsSet(Set<String> labels) {
		Set<Labels> setOflabels = new HashSet<>();
		Labels curlabel;
		for (String label : labels) {
			curlabel = labelsService.getLabelByLabelName(label);
			if (curlabel == null) {
				curlabel = new Labels();
				curlabel.setLabelName(label);
			}

			setOflabels.add(curlabel);
		}

		return setOflabels;
	}

	private boolean validateSurvey(SurveyDto survey) {
		boolean result = false;
		boolean condition1 = StringUtils.validateString(survey.getName())
				&& StringUtils.validateString(survey.getDescription());

		boolean condition2 = survey.getQuestions() != null && survey.getQuestions().size() != 0;

		if (condition1 && condition2) {
			Iterator<QuestionDto> iter = survey.getQuestions().iterator();
			QuestionDto question;
			boolean tempResult = true;
			while (iter.hasNext()) {
				question = iter.next();
				condition1 = StringUtils.validateString(question.getText());
				condition2 = survey.getQuestions() != null && question.getOptions().size() != 0;

				if (!(condition1 && condition2)) {
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

	@Override
	public ResponseDto<Void> deleteSurvey(String accessToken, int surveyId) {
		ResponseDto<Void> response = new ResponseDto<>();
		Status status;
		User surveyor = userService.getUserByAccessToken(accessToken);
		Survey survey = surveyService.getSurveyById(surveyId);
		if (surveyor != null && survey != null && survey.getSurveyStatus() != SurveyStatus.DELETED) {
			if (surveyor.equals(survey.getSurveyOwner())) {
				status = surveyService.changeSurveyStatus(survey, SurveyStatus.DELETED);
			} else {
				status = Status.ACCESS_DENIED;
			}
		} else {
			status = Status.NOT_FOUND;
		}

		response.setStatus(status);
		return response;
	}

	@Override
	public ResponseDto<SurveyDto> getSurvey(int surveyId) {
		ResponseDto<SurveyDto> response = new ResponseDto<>();

		Status status;
		SurveyDto surveyDto = null;

		Survey survey = surveyService.getSurveyById(surveyId);
		if (survey != null && survey.getSurveyStatus() != SurveyStatus.DELETED) {
			surveyDto = convertModelToDto(survey);
			status = Status.SUCCESS;
		} else {
			status = Status.NOT_FOUND;
		}

		response.setStatus(status);
		response.setBody(surveyDto);

		return response;
	}

	private SurveyDto convertModelToDto(Survey survey) {
		SurveyDto surveyDto = new SurveyDto();

		surveyDto.setId(survey.getSurveyId());
		surveyDto.setName(survey.getSurveyName());
		surveyDto.setDescription(survey.getDescription());
		surveyDto.setLabels(changeLabelsSetToStringSet(survey.getLabels()));
		surveyDto.setQuestions(convertModelToDtoQuestion(survey.getQuestions()));

		return surveyDto;
	}

	private Set<QuestionDto> convertModelToDtoQuestion(Set<Questions> questions) {
		Set<QuestionDto> questionDtos = new HashSet<>();
		QuestionDto currQuestion;

		for (Questions ques : questions) {
			currQuestion = new QuestionDto();
			currQuestion.setId(ques.getQuesId());
			currQuestion.setText(ques.getQuestion());
			currQuestion.setRequired(ques.isRequired());
			currQuestion.setOptions(convertModelToDtoOption(ques.getOptions()));
			questionDtos.add(currQuestion);
		}

		return questionDtos;
	}

	private Set<OptionDto> convertModelToDtoOption(Set<Options> options) {
		Set<OptionDto> optionDtos = new HashSet<>();
		for (Options option : options) {
			optionDtos.add(convertModelToDtoOption(option));
		}

		return optionDtos;
	}

	private OptionDto convertModelToDtoOption(Options option) {
		OptionDto optionDto = new OptionDto();
		optionDto = new OptionDto();
		optionDto.setId(option.getOptionId());
		optionDto.setText(option.getOptionValue());
		optionDto.setType(option.getOptionType());
		return optionDto;
	}

	private Set<String> changeLabelsSetToStringSet(Set<Labels> labels) {
		Set<String> setOflabels = new HashSet<>();
		for (Labels label : labels) {
			setOflabels.add(label.getLabelName());
		}

		return setOflabels;
	}

	@Override
	public ResponseDto<Void> editSurvey(String accessToken, SurveyDto surveyDto) {
		ResponseDto<Void> response = new ResponseDto<>();

		Status status;
		User surveyor = userService.getUserByAccessToken(accessToken);
		Survey survey = surveyService.getSurveyById(surveyDto.getId());
		if (surveyor.getCreatedSurveyList().contains(survey)) {
			if (validateSurvey(surveyDto)) {
				Survey newSurvey = convertDtoToModel(surveyDto, survey.getCreatedDate());
				newSurvey.setSurveyOwner(surveyor);
				status = surveyService.edit(newSurvey);
			} else {
				status = Status.INVALID_CONTENT;
			}
		} else {
			status = Status.ACCESS_DENIED;
		}

		response.setStatus(status);
		return response;
	}

	@Override
	public ResponseDto<Void> changeSurveyStatus(String accessToken, int surveyId) {
		ResponseDto<Void> response = new ResponseDto<>();
		Status status;

		User surveyor = userService.getUserByAccessToken(accessToken);
		Survey survey = surveyService.getSurveyById(surveyId);
		if (surveyor != null && survey != null && survey.getSurveyStatus() != SurveyStatus.DELETED) {
			if (surveyor.equals(survey.getSurveyOwner())) {
				SurveyStatus surveyStatus;
				if (survey.getSurveyStatus() == SurveyStatus.LIVE) {
					surveyStatus = SurveyStatus.NOTLIVE;
				} else {
					surveyStatus = SurveyStatus.LIVE;
				}

				status = surveyService.changeSurveyStatus(survey, surveyStatus);
			} else {
				status = Status.ACCESS_DENIED;
			}
		} else {
			status = Status.NOT_FOUND;
		}

		response.setStatus(status);
		return response;
	}

	@Override
	public ResponseDto<Void> addOrRemoveSurveyViewer(String accessToken, int surveyId, int userId) {
		ResponseDto<Void> response = new ResponseDto<>();
		Status status;

		User surveyor = userService.getUserByAccessToken(accessToken);
		User viewer = userService.getUserById(userId);
		Survey survey = surveyService.getSurveyById(surveyId);
		if (surveyor != null && survey != null && viewer != null && survey.getSurveyStatus() != SurveyStatus.DELETED) {
			if (survey.getSurveyOwner().equals(surveyor)) {
				if (!survey.getViewers().contains(viewer)) {
					status = surveyService.addViewer(survey, viewer);
				} else {
					status = surveyService.removeViewer(survey, viewer);
				}
			} else {
				status = Status.ACCESS_DENIED;
			}
		} else {
			status = Status.NOT_FOUND;
		}

		response.setStatus(status);
		return response;
	}

	@Override
	public Iterable<SurveyInfoDto> searchSurvey(String accessToken, String searchString) {
		User surveyor = userService.getUserByAccessToken(accessToken);
		Set<Survey> surveyListmatchedByName = surveyService.getMatchedSurveys(surveyor, searchString);
		Set<Survey> surveyListmatchedByLabel = labelsService.getMatchedSurveyByLabelName(surveyor, searchString);
		surveyListmatchedByLabel.addAll(surveyListmatchedByName);
		Set<SurveyInfoDto> surveylist = new HashSet<>();
		for (Survey surveyObj : surveyListmatchedByLabel) {
			surveylist.add(convertModeltoDtoSurveyInfo(surveyObj));
		}

		return surveylist;
	}

	private SurveyInfoDto convertModeltoDtoSurveyInfo(Survey curSurvey) {
		SurveyInfoDto surveyInfoDtoObject = new SurveyInfoDto();
		surveyInfoDtoObject.setId(curSurvey.getSurveyId());
		surveyInfoDtoObject.setDescription(curSurvey.getDescription());
		surveyInfoDtoObject.setSurveyName(curSurvey.getSurveyName());
		surveyInfoDtoObject.setStatus(curSurvey.getSurveyStatus());
		surveyInfoDtoObject.setSurveyUrl(surveyService.getSurveyURL(curSurvey));
		return surveyInfoDtoObject;
	}

	@Override
	public Status checkIfSurveyExists(int surveyId) {
		Status status;
		if (surveyService.getSurveyById(surveyId) != null) {
			status = Status.SUCCESS;
		} else {
			status = Status.FAILURE;
		}

		return status;
	}

	@Override
	public Status saveResponse(String accessToken, ResponderDto responderDto) {
		Status status = null;
		if (validateResponse(responderDto)) {
			Survey survey = surveyService.getSurveyById(responderDto.getSurveyId());
			User responder = userService.getUserByAccessToken(accessToken);
			if (survey != null && responder != null) {
				if (!survey.getRespondersList().contains(responder)) {
					int numOfQuestions = responderDto.getQuestionResponses().size();
					for (int index = 0; index < numOfQuestions; index++) {
						UserResponses userResponse = convertDtoToModelUserResponse(responder,
								responderDto.getQuestionResponses().get(index));
						status = userResponsesService.addNewResponse(userResponse);
					}

					responder = userService.getUserByAccessToken(accessToken);
					survey.getRespondersList().add(responder);
					status = surveyService.edit(survey);
				} else {
					status = Status.DUPLICATE;
				}
			} else {
				status = Status.NOT_FOUND;
			}
		} else {
			status = Status.INVALID_CONTENT;
		}

		return status;
	}

	private UserResponses convertDtoToModelUserResponse(User responder, QuestionResponseDto questionResponseDto) {
		UserResponses userResponse = new UserResponses();

		Questions question = questionsService.getQuestionById(questionResponseDto.getQuesId());
		Options option = questionsService.getOptionById(questionResponseDto.getOptionId());

		userResponse.setUser(responder);
		userResponse.setQuestion(question);
		userResponse.setOption(option);

		return userResponse;
	}

	private boolean validateResponse(ResponderDto responderDto) {
		boolean result = true;
		List<QuestionResponseDto> questionResponseList = responderDto.getQuestionResponses();
		int numOfQuestions = questionResponseList.size();
		Questions question;
		Options option;
		for (int index = 0; index < numOfQuestions; index++) {
			question = questionsService.getQuestionById(questionResponseList.get(index).getQuesId());
			option = questionsService.getOptionById(questionResponseList.get(index).getOptionId());
			if (question.isRequired() == true) {
				if (option == null) {
					result = false;
					break;
				}
			}
		}

		return result;
	}

	@Override
	public ResponseDto<SurveyResultDto> getSurveyResult(String accessToken, int surveyId) {
		ResponseDto<SurveyResultDto> response = new ResponseDto<>();
		SurveyResultDto surveyResultDto = new SurveyResultDto();
		Survey survey = surveyService.getSurveyById(surveyId);
		User user = userService.getUserByAccessToken(accessToken);
		if (survey != null) {
			if (survey.getViewers().contains(user)) {
				surveyResultDto.setSurveyId(surveyId);
				Set<Questions> surveyQuestion = survey.getQuestions();
				Set<QuestionResultDto> questionResultSetDto = new HashSet<>();
				for (Questions curQues : surveyQuestion) {
					QuestionResultDto questionResultDto = new QuestionResultDto();
					questionResultDto.setId(curQues.getQuesId());
					Map<Integer, Double> optionValue = new HashMap<>();

					Set<Options> questionOptions = new HashSet<>();
					questionOptions = curQues.getOptions();
					for (Options curOption : questionOptions) {
						Double curOptionResponsesSize = userResponsesService
								.getUserResponsesOfAQuestionAndOption(curQues, curOption);
						optionValue.put(curOption.getOptionId(), curOptionResponsesSize);
					}

					questionResultDto.setOptionData(optionValue);
					questionResultSetDto.add(questionResultDto);
				}

				surveyResultDto.setQuestionResults(questionResultSetDto);
				surveyResultDto.setNumOfResponders(survey.getRespondersList().size());

				response.setBody(surveyResultDto);
				response.setStatus(Status.SUCCESS);

			} else {
				response.setBody(null);
				response.setStatus(Status.ACCESS_DENIED);
			}
		} else {
			response.setBody(null);
			response.setStatus(Status.NOT_FOUND);
		}

		return response;
	}

	@Override
	public ResponseDto<Map<Integer, OptionDto>> getSuveyResponse(String accessToken, int surveyId) {
		ResponseDto<Map<Integer, OptionDto>> response = new ResponseDto<>();
		Status status = null;
		Map<Integer, OptionDto> selectedOptions = null;
		User responder = userService.getUserByAccessToken(accessToken);
		Survey survey = surveyService.getSurveyById(surveyId);
		if (survey != null) {
			if (responder.getFilledSurveyList().contains(survey)) {
				selectedOptions = new HashMap<>();
				for (Questions question : survey.getQuestions()) {
					UserResponses userResponse = userResponsesService.getUserResponseById(responder, question);
					if (userResponse != null) {
						Options option = userResponse.getOption();
						OptionDto optionDto = convertModelToDtoOption(option);
						selectedOptions.put(question.getQuesId(), optionDto);
					} else {
						selectedOptions.put(question.getQuesId(), null);
					}
				}
			} else {
				status = Status.ACCESS_DENIED;
			}
		} else {
			status = Status.NOT_FOUND;
		}

		response.setStatus(status);
		response.setBody(selectedOptions);
		return response;
	}
}