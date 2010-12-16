package com.trifork.sdm.importer.importers.takst.factories;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import com.trifork.sdm.models.takst.LaegemiddelformBetegnelser;


public class LaegemiddelformBetegnelserFactory extends AbstractFactory<LaegemiddelformBetegnelser> {

	private static void setFieldValue(LaegemiddelformBetegnelser obj, int fieldNo, String value) {

		if ("".equals(value)) value = null;
		switch (fieldNo) {
		case 0:
			obj.setKode(value);
			break;
		case 1:
			obj.setTekst(value);
			break;
		case 2:
			obj.setAktivInaktiv(value);
			break;
		default:
			break;
		}
	}


	private static int getOffset(int fieldNo) {

		switch (fieldNo) {
		case 0:
			return 0;
		case 1:
			return 7;
		case 2:
			return 107;
		default:
			return -1;
		}
	}


	private static int getLength(int fieldNo) {

		switch (fieldNo) {
		case 0:
			return 7;
		case 1:
			return 100;
		case 2:
			return 1;
		default:
			return -1;
		}
	}


	private static int getNumberOfFields() {

		return 3;
	}


	public static String getLmsName() {

		return "LMS22";
	}


	public Set<LaegemiddelformBetegnelser> read(String rootFolder) throws IOException {

		File f = new File(rootFolder + getLmsName().toLowerCase() + ".txt");

		Set<LaegemiddelformBetegnelser> list = new HashSet<LaegemiddelformBetegnelser>();
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "CP865"));
			while (reader.ready()) {
				String line = reader.readLine();
				if (line.length() > 0) {
					list.add(parse(line));
				}
			}
			return list;
		}
		finally {
			try {
				if (reader != null) {
					reader.close();
				}
			}
			catch (Exception e) {
				logger.warn("Could not close FileReader");
			}
		}
	}


	private static LaegemiddelformBetegnelser parse(String line) {

		LaegemiddelformBetegnelser obj = new LaegemiddelformBetegnelser();
		
		for (int fieldNo = 0; fieldNo < getNumberOfFields(); fieldNo++) {
			
			if (getLength(fieldNo) > 0) {
				
				String value = line.substring(getOffset(fieldNo), getOffset(fieldNo) + getLength(fieldNo)).trim();
				setFieldValue(obj, fieldNo, value);
			}
		}
		
		return obj;
	}
}