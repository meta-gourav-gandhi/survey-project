package com.metacube.wesurve.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	/**
	 * This method return a random string of the given length
	 * @param length
	 * @return random string
	 */
	public static String randomAlphanumeric(int length) {
		String string1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
		StringBuilder string2 = new StringBuilder();
		Random random = new Random();
		while (string2.length() < length) {
			int index = (int) (random.nextFloat() * string1.length());
			string2.append(string1.charAt(index));
		}

		return string2.toString();
	}
	
	/**
	 * This method checks if string is not null and empty.
	 * @param string
	 * @return
	 */
	public static boolean validateString(String string) {
		return !(string == null || string.trim().isEmpty());
	}

	/**
	 * @param email to to check if the email is in proper format
	 * @return boolean function to validate the email of the user
	 */
	public static boolean validateEmail(String email) {
		boolean result = false;
		if (validateString(email)) {
			Pattern validEmailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = validEmailPattern.matcher(email);
			result = matcher.find();
		}

		return result;
	}
	
	/**
	*
	* @param password
	* @return
	*/
	public static boolean validatePassword(String password) {
		return validateString(password) && password.length() >= 8;
	}

	/**
	 * @param email to append in the access token
	 * @return the random access token when user login function to generate the access token
	 * @throws NoSuchAlgorithmException
	 */
	public static String generateAccessToken(String email) throws NoSuchAlgorithmException {
		SecureRandom random = new SecureRandom();
		long longToken = Math.abs(random.nextLong());
		String token = Long.toString(longToken, 16);
		token += email;
		return MD5Encryption.encrypt(token);
	}
}