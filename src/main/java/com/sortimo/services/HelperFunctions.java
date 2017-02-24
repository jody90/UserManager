package com.sortimo.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

@Component
public class HelperFunctions {

	/**
	 * Erzeugt aus einem String einen md5Hash
	 * 
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public String md5Hash(String password) throws NoSuchAlgorithmException {
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.reset();
		m.update(password.getBytes());
		byte[] digest = m.digest();
		BigInteger bigInt = new BigInteger(1,digest);
		String hashtext = bigInt.toString(16);
		
		while(hashtext.length() < 32 ){
			hashtext = "0" + hashtext;
		}
		
		return hashtext;
	}
	
}
