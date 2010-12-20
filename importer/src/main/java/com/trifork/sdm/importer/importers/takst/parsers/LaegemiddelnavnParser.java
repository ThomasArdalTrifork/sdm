package com.trifork.sdm.importer.importers.takst.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import com.trifork.sdm.models.takst.unused.Laegemiddelnavn;


public class LaegemiddelnavnParser extends AbstractParser<Laegemiddelnavn> {

	private static void setFieldValue(Laegemiddelnavn obj, int fieldNo, String value) {

		if ("".equals(value)) value = null;
		
		switch (fieldNo) {
		case 0:
			obj.setDrugid(toLong(value));
			break;
		case 1:
			obj.setLaegemidletsUforkortedeNavn(value);
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
			return 11;
		default:
			return -1;
		}
	}


	private static int getLength(int fieldNo) {

		switch (fieldNo) {
		case 0:
			return 11;
		case 1:
			return 60;
		default:
			return -1;
		}
	}


	private static int getNumberOfFields() {

		return 2;
	}


	private static String getLmsName() {

		return "LMS21";
	}


	public Set<Laegemiddelnavn> read(String rootFolder) throws IOException {

		File f = new File(rootFolder + getLmsName().toLowerCase() + ".txt");

		Set<Laegemiddelnavn> list = new HashSet<Laegemiddelnavn>();
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


	private static Laegemiddelnavn parse(String line) {

		Laegemiddelnavn obj = new Laegemiddelnavn();
		for (int fieldNo = 0; fieldNo < getNumberOfFields(); fieldNo++) {
			if (getLength(fieldNo) > 0) {
				String value = line.substring(getOffset(fieldNo), getOffset(fieldNo) + getLength(fieldNo)).trim();
				setFieldValue(obj, fieldNo, value);
			}
		}
		return obj;
	}
}