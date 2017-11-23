package com.metacube.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.metacube.wesurve.dto.OptionDto;
import com.metacube.wesurve.dto.QuestionDto;
import com.metacube.wesurve.dto.QuestionResponseDto;
import com.metacube.wesurve.dto.ResponderDto;
import com.metacube.wesurve.dto.ResponseDto;
import com.metacube.wesurve.dto.SurveyDto;
import com.metacube.wesurve.dto.SurveyResponseDto;
import com.metacube.wesurve.dto.SurveyResultDto;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.facade.SurveyFacade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "test-config.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SurveyFacadeTest {

	@Autowired
	SurveyFacade surveyFacade;

	static final int SurveyID = 1;
	static final int SurveyTempID = 2;
	static final int SurveyorID = 2;
	static final String SurveyName = "TestSurvey";
	static final String SurveyDescription = "TestSurveyDescription";
	static final String SurveyQuestion = "DemoQuestion";
	static final String SurveyOption = "DemoOption";

	@Test
	public void test01_createSurveyValidData() {
		SurveyDto surveyDto = new SurveyDto();
		surveyDto.setName(SurveyName);
		surveyDto.setDescription(SurveyDescription);
		surveyDto.setId(0);

		OptionDto options = new OptionDto();
		options.setId(0);
		options.setText(SurveyOption);
		Set<OptionDto> optionDtoSet = new HashSet<>();
		optionDtoSet.add(options);

		Set<QuestionDto> QuestionDtoSet = new HashSet<>();
		QuestionDto questions = new QuestionDto();
		questions.setText(SurveyQuestion);
		questions.setId(0);
		questions.setRequired(true);
		questions.setOptions(optionDtoSet);

		QuestionDtoSet.add(questions);
		surveyDto.setQuestions(QuestionDtoSet);

		Set<String> labelsSet = new HashSet<>();
		String curLabel = "TestLabel";
		labelsSet.add(curLabel);

		surveyDto.setLabels(labelsSet);

		ResponseDto<SurveyResponseDto> response = new ResponseDto<>();
		response = surveyFacade.createSurvey(SurveyorID, surveyDto);
		SurveyResponseDto expectedSurvey = new SurveyResponseDto();
		expectedSurvey.setName(SurveyName);

		assertEquals(expectedSurvey.getName(), response.getBody().getName());
		assertEquals(Status.SUCCESS.name(), response.getStatus().name());

	}

	@Test
	public void test02_createSurveyInValidDataName() {
		SurveyDto surveyDto = new SurveyDto();

		surveyDto.setDescription(SurveyDescription);
		surveyDto.setId(0);

		OptionDto options = new OptionDto();
		options.setId(0);
		options.setText(SurveyOption);
		Set<OptionDto> optionDtoSet = new HashSet<>();
		optionDtoSet.add(options);

		Set<QuestionDto> QuestionDtoSet = new HashSet<>();
		QuestionDto questions = new QuestionDto();
		questions.setText(SurveyQuestion);
		questions.setId(0);
		questions.setRequired(true);
		questions.setOptions(optionDtoSet);

		QuestionDtoSet.add(questions);
		surveyDto.setQuestions(QuestionDtoSet);

		Set<String> labelsSet = new HashSet<>();
		String curLabel = "TestLabel";
		labelsSet.add(curLabel);

		surveyDto.setLabels(labelsSet);

		ResponseDto<SurveyResponseDto> response = new ResponseDto<>();
		response = surveyFacade.createSurvey(SurveyorID, surveyDto);
		SurveyResponseDto expectedSurvey = new SurveyResponseDto();
		expectedSurvey.setName(SurveyName);

		assertEquals(Status.INVALID_CONTENT.name(), response.getStatus().name());
	}

	@Test
	public void test02_createSurveyInValidDataNoQuestion() {
		SurveyDto surveyDto = new SurveyDto();
		surveyDto.setName(SurveyName);
		surveyDto.setDescription(SurveyDescription);
		surveyDto.setId(0);

		Set<String> labelsSet = new HashSet<>();
		String curLabel = "TestLabel";
		labelsSet.add(curLabel);

		surveyDto.setLabels(labelsSet);

		ResponseDto<SurveyResponseDto> response = new ResponseDto<>();
		response = surveyFacade.createSurvey(SurveyorID, surveyDto);
		SurveyResponseDto expectedSurvey = new SurveyResponseDto();
		expectedSurvey.setName(SurveyName);

		assertEquals(Status.INVALID_CONTENT.name(), response.getStatus().name());
	}

	@Test
	public void test01_createSurveyValidData2() {
		SurveyDto surveyDto = new SurveyDto();
		surveyDto.setName("Test2");
		surveyDto.setDescription(SurveyDescription);
		surveyDto.setId(0);

		OptionDto options = new OptionDto();
		options.setId(0);
		options.setText(SurveyOption);
		Set<OptionDto> optionDtoSet = new HashSet<>();
		optionDtoSet.add(options);

		Set<QuestionDto> QuestionDtoSet = new HashSet<>();
		QuestionDto questions = new QuestionDto();
		questions.setText(SurveyQuestion + " 2");
		questions.setId(0);
		questions.setRequired(true);
		questions.setOptions(optionDtoSet);

		QuestionDtoSet.add(questions);
		surveyDto.setQuestions(QuestionDtoSet);

		Set<String> labelsSet = new HashSet<>();
		String curLabel = "TestLabel2";
		labelsSet.add(curLabel);

		surveyDto.setLabels(labelsSet);

		ResponseDto<SurveyResponseDto> response = new ResponseDto<>();
		response = surveyFacade.createSurvey(SurveyorID, surveyDto);
		SurveyResponseDto expectedSurvey = new SurveyResponseDto();
		expectedSurvey.setName(SurveyName);

		assertNotEquals(expectedSurvey.getName(), response.getBody().getName());
		assertEquals(Status.SUCCESS.name(), response.getStatus().name());
	}

	@Test
	public void test11_deleteSurveyInvalidSurveyId() {
		ResponseDto<Void> response = surveyFacade.deleteSurvey(SurveyorID, 100);
		assertEquals(Status.NOT_FOUND.name(), response.getStatus().name());
	}

	@Test
	public void test11_deleteSurveyInvalidSurveyorId() {
		ResponseDto<Void> response = surveyFacade.deleteSurvey(100, SurveyTempID);
		assertEquals(Status.NOT_FOUND.name(), response.getStatus().name());
	}

	@Test
	public void test11_deleteSurveyInvalidSurveyOwner() {
		ResponseDto<Void> response = surveyFacade.deleteSurvey(1, SurveyTempID);
		assertEquals(Status.ACCESS_DENIED.name(), response.getStatus().name());
	}

	@Test
	public void test12_deleteSurveyPositive() {
		ResponseDto<Void> response = surveyFacade.deleteSurvey(SurveyorID, SurveyTempID);
		assertEquals(Status.SUCCESS.name(), response.getStatus().name());
	}

	/*@Test
	public void test21_getSurveyInValidSurveyId() {
		ResponseDto<SurveyDto> response = surveyFacade.getSurvey(100);
		assertEquals(Status.NOT_FOUND.name(), response.getStatus().name());
	}

	@Test
	public void test21_getSurveyValidSurveyId() {
		ResponseDto<SurveyDto> response = surveyFacade.getSurvey(SurveyID);
		assertEquals(SurveyID, response.getBody().getId());
		assertEquals(SurveyDescription, response.getBody().getDescription());
		assertEquals(SurveyName, response.getBody().getName());
	}*/

	@Test
	public void test31_editSurveyInValidData() {
		SurveyDto surveyDto = new SurveyDto();
		surveyDto.setName("New Name");
		surveyDto.setDescription("New Description");
		surveyDto.setId(1);

		Set<String> labelsSet = new HashSet<>();
		String curLabel = "TestLabel";
		labelsSet.add(curLabel);
		surveyDto.setLabels(labelsSet);

		ResponseDto<Void> response = surveyFacade.editSurvey(SurveyorID, surveyDto);
		assertEquals(Status.INVALID_CONTENT.name(), response.getStatus().name());
	}

	@Test
	public void test32_editSurveyPositive() {
		SurveyDto surveyDto = new SurveyDto();
		surveyDto.setName("Updated Name");
		surveyDto.setDescription("Updated description");
		surveyDto.setId(1);

		OptionDto options = new OptionDto();
		options.setId(1);
		options.setText(SurveyOption);
		Set<OptionDto> optionDtoSet = new HashSet<>();
		optionDtoSet.add(options);

		Set<QuestionDto> QuestionDtoSet = new HashSet<>();
		QuestionDto questions = new QuestionDto();
		questions.setText(SurveyQuestion);
		questions.setId(1);
		questions.setRequired(true);
		questions.setOptions(optionDtoSet);

		QuestionDtoSet.add(questions);
		surveyDto.setQuestions(QuestionDtoSet);

		Set<String> labelsSet = new HashSet<>();
		String curLabel = "TestLabel";
		labelsSet.add(curLabel);

		surveyDto.setLabels(labelsSet);

		ResponseDto<Void> response = surveyFacade.editSurvey(SurveyorID, surveyDto);

		assertEquals(Status.SUCCESS.name(), response.getStatus().name());
	}

	@Test
	public void test41_changeSurveyStatusNegative() {
		ResponseDto<Void> response = surveyFacade.changeSurveyStatus(SurveyorID, 100);
		assertEquals(Status.NOT_FOUND.name(), response.getStatus().name());
	}

	@Test
	public void test42_changeSurveyStatusInValidOwner() {
		ResponseDto<Void> response = surveyFacade.changeSurveyStatus(3, SurveyID);
		assertEquals(Status.ACCESS_DENIED.name(), response.getStatus().name());
	}

	@Test
	public void test43_changeSurveyStatusPositive() {
		ResponseDto<Void> response = surveyFacade.changeSurveyStatus(SurveyorID, SurveyID);
		assertEquals(Status.SUCCESS.name(), response.getStatus().name());
	}

	@Test
	public void test51_addOrRemoveSurveyViewer() {
		ResponseDto<Void> response = surveyFacade.addOrRemoveSurveyViewer(SurveyorID, SurveyID, 3);
		assertEquals(Status.SUCCESS.name(), response.getStatus().name());
	}

	@Test
	public void test52_addOrRemoveSurveyViewerNegative() {
		ResponseDto<Void> response = surveyFacade.addOrRemoveSurveyViewer(3, SurveyID, 3);
		assertEquals(Status.ACCESS_DENIED.name(), response.getStatus().name());
	}

	@Test
	public void test61_saveResponsesPositive() {
		ResponderDto responderDto = new ResponderDto();
		responderDto.setSurveyId(SurveyID);

		QuestionResponseDto questionResponseDto = new QuestionResponseDto();
		questionResponseDto.setQuesId(1);
		questionResponseDto.setOptionId(1);

		List<QuestionResponseDto> questionResponses = new ArrayList<>();
		questionResponses.add(questionResponseDto);
		responderDto.setQuestionResponses(questionResponses);

		Status status = surveyFacade.saveResponse(2, responderDto);

		assertEquals(Status.SUCCESS.name(), status.name());
	}

	@Test
	public void test71_getSurveyResultPositive() {
		ResponseDto<SurveyResultDto> response = surveyFacade.getSurveyResult(2, SurveyID);

		SurveyResultDto result = new SurveyResultDto();
		result.setSurveyId(SurveyID);
		result.setNumOfResponders(1);

		assertEquals(result.getNumOfResponders(), response.getBody().getNumOfResponders());
		assertEquals(Status.SUCCESS.name(), response.getStatus().name());
	}

	@Test
	public void test72_getSurveyResultNegative() {
		ResponseDto<SurveyResultDto> response = surveyFacade.getSurveyResult(1, SurveyID);
		assertEquals(Status.ACCESS_DENIED.name(), response.getStatus().name());
	}

	@Test
	public void test81_getSurveyResponseForUser() {
		ResponseDto<Map<Integer, Integer>> response = surveyFacade.getSuveyResponse(2, SurveyID);
		assertEquals("DemoOption", response.getBody().get(1));
	}

	@Test
	public void test81_getSurveyResponseForInValidUser() {
		ResponseDto<Map<Integer, Integer>> response = surveyFacade.getSuveyResponse(3, SurveyID);
		assertEquals(Status.ACCESS_DENIED.name(), response.getStatus().name());
	}
}