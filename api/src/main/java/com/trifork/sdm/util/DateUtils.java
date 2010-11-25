package com.trifork.sdm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;


// TODO: thb. Should be changed to DateFormatter and injected by Spring/Guice.

// TODO: thb. Why not use a library for these conversions such as org.apache.log4j.helpers.ISO8601DateFormat,
// which is a dependency for this project anyway. 

public class DateUtils
{
	private static Logger logger = Logger.getLogger(DateUtils.class);

	public static final Calendar FUTURE = toCalendar(2999, 12, 31);
	public static final Calendar PAST = toCalendar(1950, 1, 1);

	public static final String MYSQL_FUTURE_DATE = toMySQLdate(FUTURE);


	/**
	 * @return a String representing the ISO 8601 date without time zone.
	 */
	public static String toISO8601date(Calendar cal)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}


	/**
	 * @param long1
	 *            representing a date sing the format: yyyyMMdd.
	 * @return a String representing the ISO 8601 date without time zone.
	 */
	public static String toISO8601date(Long long1)
	{
		if (long1 == null || long1 == 0) return null;

		SimpleDateFormat informat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat outformat = new SimpleDateFormat("yyyy-MM-dd");

		try
		{
			return "" + outformat.format(informat.parse("" + long1));
		}
		catch (ParseException e)
		{
			logger.error("Error converting date to iso 8601 date format. Returning unformated string: '" + long1 + "'");
			return "" + long1;
		}

	}


	public static String toFilenameDatetime(Calendar cal)
	{
		SimpleDateFormat outformat = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss");
		return outformat.format(cal.getTime());
	}


	/**
	 * Helper method that allows you to specify calendar values as you would
	 * expect.
	 * 
	 * Normally Java's calendar implementation months start from 0.
	 * 
	 * @param month
	 *            (1-12)
	 * @param date
	 *            (1-31)
	 */
	public static Calendar toCalendar(int year, int month, int date)
	{
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month - 1, date);

		return cal;
	}


	public static Calendar toCalendar(int year, int month, int date, int hours, int minutes, int secs)
	{
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month - 1, date, hours, minutes, secs);
		return cal;
	}


	public static Calendar toCalendar(java.sql.Date date)
	{

		if (date == null) return null;
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTimeInMillis(date.getTime());
		return cal;
	}


	public static Calendar toCalendar(java.util.Date date)
	{
		if (date == null) return null;
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTimeInMillis(date.getTime());
		return cal;
	}


	public static String toMySQLdate(Calendar date)
	{
		if (date == null)
		{
			logger.warn("Cannot convert null to mysqldate");
			return null;
		}
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return dateFormatter.format(date.getTime());
	}
}
