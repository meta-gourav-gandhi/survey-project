package com.metacube.wesurve.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Factory {
	
	static ApplicationContext applicationContext;

	static {
		applicationContext = new ClassPathXmlApplicationContext("WEB-INF/applicationContext.xml");
	
	}

	static public ApplicationContext getApplicationContext()
	{
		return applicationContext;
	
	}
}