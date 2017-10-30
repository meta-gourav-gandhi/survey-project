package com.metacube.wesurve.facade;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metacube.wesurve.dto.UserDto;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.model.User;
import com.metacube.wesurve.service.UserService;

@Component("userFacade")
public class UserFacadeImplementation implements UserFacade {
	@Autowired
	UserService userService;

	public UserFacadeImplementation() {
	}

	@Override
	public Status createNewUser(UserDto userDto) {
		Status outputStatus = Status.FAILURE;
		if(validateUser(userDto)) {
			User user = convertDtoToModel(userDto);
			if (userService.createNewUser(user) != null) {
				outputStatus = Status.SUCCESS;
			}
		}
		
		return outputStatus;
	}

	private boolean validateUser(UserDto userDto) {
		boolean result = true;
		Pattern validEmailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = validEmailPattern.matcher(userDto.getEmail());
		boolean condition1 = (userDto.getName() == null && userDto.getName().trim().isEmpty()) || 
				(userDto.getPassword() == null && userDto.getPassword().trim().isEmpty()) ||
				(userDto.getEmail() == null && userDto.getEmail().trim().isEmpty()) ||
				(userDto.getGender() == null && userDto.getGender().trim().isEmpty()) ||
				(userDto.getDob() == null && userDto.getDob().trim().isEmpty());
		boolean condition2 = matcher.find();
		boolean condition3 = userDto.getPassword().length() < 8;
		boolean condition4 = userDto.getGender().length() == 1;
		//check for gender.
		boolean condition5 = isDateValid(userDto.getDob(), "yyyy-MM-dd");
		
		if(condition1 == false || condition2 == false || 
				condition3 == false || condition4 == false || condition5 == false) {
			result = false;
		}
		
		return result;
	}

	private boolean isDateValid(String date, String dateFormat) {
		boolean result = true;
		if(date == null){
			result = false;
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		simpleDateFormat.setLenient(false);
		try {
			@SuppressWarnings("unused")
			java.util.Date parsedDate = simpleDateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			result = false;
		}
	
		return result;
	}

	private User convertDtoToModel(UserDto userDto) {
		User user = new User();
		user.setName(userDto.getName());
		user.setDob(convertStringDateToSqlDate(userDto.getDob()));
		user.setEmail(userDto.getEmail());
		user.setGender(userDto.getGender().charAt(0));
		user.setPassword(userDto.getPassword());
		return user;
	}

	private Date convertStringDateToSqlDate(String date) {
		Date sqlDate = null;
		
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date parsedDate = dateFormat.parse(date);
			sqlDate = new Date(parsedDate.getTime());  
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return sqlDate;
	}
	
	public void sendEmail(String from, String password, String to, String subject, String body) {
		Message message;

		Properties properties = new Properties();
		properties.put("mail.smtp.host","smtp.gmail.com");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.auth","true"); 
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.debug", "false");
		Session session = Session.getDefaultInstance(properties, new MailAuthenticator(from, password));
		
		try {
			message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			
			message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
			message.setSubject(subject);
			message.setText(body);
			Transport.send(message);
			System.out.println("Sent Sucessfully");
		}
		catch(MessagingException exception) {
			exception.printStackTrace();
		}
	}
}