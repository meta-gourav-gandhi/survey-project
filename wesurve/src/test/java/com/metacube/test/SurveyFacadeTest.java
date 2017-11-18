package com.metacube.test;

import static org.junit.Assert.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.metacube.wesurve.facade.SurveyFacade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "test-config.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SurveyFacadeTest {
	
	@Autowired
	SurveyFacade surveyFacade;
	
	static final int SurveyorID = 2; 
	
	@Test
	public void test01_createSurveyInvalidDAta() {
		
	}
	

}
