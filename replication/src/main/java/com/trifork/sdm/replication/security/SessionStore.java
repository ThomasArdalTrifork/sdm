package com.trifork.sdm.replication.security;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.inject.Named;
import javax.inject.Singleton;

import com.trifork.sdm.replication.security.AuthorizationBuilder.Authorization;


/**
 * Sessions are allocated on a per resource basis.
 */
@Singleton
public class SessionStore {

	private SecureRandom random = new SecureRandom();
	private HashMap<String, Date> storedTokens = new HashMap<String, Date>();
	
	private int tokenLength;
	private int tokenTTL;


	public SessionStore(@Named("TokenSize") int tokenSize, @Named("TokenWindow") int tokenTTL) {
		
		assert tokenSize > 0;
		assert tokenTTL > 0;
		
		this.tokenLength = tokenSize;
		this.tokenTTL = tokenTTL;
	}


	String createToken(Authorization authorization) {
		
		String token = null;
		
		boolean isUniqueToken = false;
		
		while (!isUniqueToken) {
			
			synchronized(storedTokens) {
			
				final int RADIX = 32;
				
				token = new BigInteger(tokenLength, random).toString(RADIX);
				
				if (!storedTokens.containsKey(token)) {
					
					// We have found a unique token.
					
					isUniqueToken = true;
					
					// Set when the token should expire.
					
					Calendar ttl = Calendar.getInstance();
					ttl.add(Calendar.MINUTE, tokenTTL);
					
					storedTokens.put(token, ttl.getTime());
				}
			}
		}

		return token;
	}
}
