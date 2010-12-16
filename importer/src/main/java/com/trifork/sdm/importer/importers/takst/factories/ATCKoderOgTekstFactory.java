package com.trifork.sdm.importer.importers.takst.factories;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.trifork.sdm.models.takst.ATCKoderOgTekst;


public class ATCKoderOgTekstFactory extends AbstractFactory<ATCKoderOgTekst> {

	private static void setFieldValue(ATCKoderOgTekst obj, int fieldNo, String value) {

		if ("".equals(value)) value = null;
		switch (fieldNo) {
		case 0:
			obj.setATCNiveau1(value);
			break;
		case 1:
			obj.setATCNiveau2(value);
			break;
		case 2:
			obj.setATCNiveau3(value);
			break;
		case 3:
			obj.setATCNiveau4(value);
			break;
		case 4:
			obj.setATCNiveau5(value);
			break;
		case 5:
			obj.setTekst(value);
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
			return 2;
		case 2:
			return 4;
		case 3:
			return 5;
		case 4:
			return 6;
		case 5:
			return 8;
		default:
			return -1;
		}
	}


	private static int getLength(int fieldNo) {

		switch (fieldNo) {
		case 0:
			return 2;
		case 1:
			return 2;
		case 2:
			return 1;
		case 3:
			return 1;
		case 4:
			return 2;
		case 5:
			return 72;
		default:
			return -1;
		}
	}


	private static int getNumberOfFields() {

		return 7;
	}


	private static String getLmsName() {

		return "LMS12";
	}


	public Map<Object, ATCKoderOgTekst> read(String rootFolder) throws IOException {

		File f = new File(rootFolder + getLmsName().toLowerCase() + ".txt");

		Map<Object, ATCKoderOgTekst> list = new HashMap<Object, ATCKoderOgTekst>();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "CP865"));
			while (reader.ready()) {
				
				String line = reader.readLine();
				
				if (line.length() > 0) {
					ATCKoderOgTekst codeAndText = parse(line);
					list.put(codeAndText.getKey(), codeAndText);
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


	private static ATCKoderOgTekst parse(String line) {

		ATCKoderOgTekst obj = new ATCKoderOgTekst();
		for (int fieldNo = 0; fieldNo < getNumberOfFields(); fieldNo++) {
			if (getLength(fieldNo) > 0) {
				String value = line.substring(getOffset(fieldNo), getOffset(fieldNo) + getLength(fieldNo)).trim();
				setFieldValue(obj, fieldNo, value);
			}
		}
		return obj;
	}
}