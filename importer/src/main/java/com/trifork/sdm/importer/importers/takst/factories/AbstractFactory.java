package com.trifork.sdm.importer.importers.takst.factories;

import org.apache.log4j.Logger;

public abstract class AbstractFactory<T> {
	
	protected static Logger logger = Logger.getLogger(AbstractFactory.class);
	
	// TODO: These should be part of a helper, well maybe?
	
	static Double toDouble(String s) {

		if (s == null || s.trim().length() == 0) return null;
		return new Double(s);
	}


	static Long toLong(String s) {

		if (s == null || s.trim().length() == 0) return null;
		return new Long(s);
	}
}
