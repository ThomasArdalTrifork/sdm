package com.trifork.sdm.importer.importers.takst.factories;

import java.io.IOException;
import java.util.Set;

import org.apache.log4j.Logger;

public abstract class AbstractFactory<T> {
	
	protected static Logger logger = Logger.getLogger(AbstractFactory.class);
	
	public abstract Set<T> read(String rootFolder) throws IOException;
	
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
