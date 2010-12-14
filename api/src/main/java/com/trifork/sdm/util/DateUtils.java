package com.trifork.sdm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;


// TODO: thb. Should be changed to DateFormatter and injected by Spring/Guice.

// TODO: thb. Why not use a library for these conversions such as org.apache.log4j.helpers.ISO8601DateFormat,
// which is a dependency for this project anyway. 

public class DateUtils {

	private static Logger logger = Logger.getLogger(DateUtils.class);

	// TODO: How do these date combine with java.util.Date?
	public static final Date FUTURE = toDate(2999, 12, 31);
	public static final Date PAST = new Date(0l);


	/**
	 * @return a String representing the ISO 8601 date without time zone.
	 */
	public static String toISO8601date(Calendar cal) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}


	/**
	 * @param long1
	 *            representing a date sing the format: yyyyMMdd.
	 * @return a String representing the ISO 8601 date without time zone.
	 */
	public static String toISO8601date(Long long1) {

		if (long1 == null || long1 == 0) return null;

		SimpleDateFormat informat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat outformat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			return "" + outformat.format(informat.parse("" + long1));
		}
		catch (ParseException e) {
			logger.error("Error converting date to iso 8601 date format. Returning unformated string: '"
					+ long1 + "'");
			return "" + long1;
		}

	}


	public static String toFilenameDatetime(Calendar cal) {

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
	public static Date toDate(int year, int month, int date) {

		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(year, month - 1, date);

		return calendar.getTime();
	}


	public static Date toDate(int year, int month, int date, int hours, int minutes, int secs) {

		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(year, month - 1, date, hours, minutes, secs);

		return calendar.getTime();
	}
}
