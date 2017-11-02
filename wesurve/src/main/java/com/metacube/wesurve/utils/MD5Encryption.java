package com.metacube.wesurve.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Encryption {

	public static String encrypt(String input) throws NoSuchAlgorithmException {

		String output = null;
		if (null == input) {
			return null;
		}
		
		// Create MessageDigest object for MD5
		MessageDigest digest = MessageDigest.getInstance("MD5");
		// Update input string in message digest
		digest.update(input.getBytes(), 0, input.length());
		// Converts message digest value in base 16 (hex)
		output = new BigInteger(1, digest.digest()).toString(16);
		return output;
	}
}