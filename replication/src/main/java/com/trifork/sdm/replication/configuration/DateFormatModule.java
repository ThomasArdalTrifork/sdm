package com.trifork.sdm.replication.configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class DateFormatModule extends AbstractModule {

	public final static String RFC1123 = "EEE, dd MMM yyyyy HH:mm:ss z";
	
	@Override
	protected void configure() {
		
	}
	
	@Provides
	public DateFormat provideDateFormat() {
		
		return new SimpleDateFormat(RFC1123);
	}
}
