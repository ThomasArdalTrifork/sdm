package com.trifork.sdm.importer.importers.takst.factories;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import com.trifork.sdm.models.takst.unused.Firma;


public class FirmaParser extends AbstractFactory<Firma> {
	private static final int NUM_FIELDS = 4;


	private static void setFieldValue(Firma obj, int fieldNo, String value) {

		if ("".equals(value)) value = null;
		switch (fieldNo) {
		case 0:
			obj.setFirmanummer(toLong(value));
			break;
		case 1:
			obj.setFirmamaerkeKort(value);
			break;
		case 2:
			obj.setFirmamaerkeLangtNavn(value);
			break;
		case 3:
			obj.setParallelimportoerKode(value);
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
			return 6;
		case 2:
			return 26;
		case 3:
			return 58;
		default:
			return -1;
		}
	}


	private static int getLength(int fieldNo) {

		switch (fieldNo) {
		case 0:
			return 6;
		case 1:
			return 20;
		case 2:
			return 32;
		case 3:
			return 2;
		default:
			return -1;
		}
	}


	@Override
	public Set<Firma> read(String rootFolder) throws IOException {

		File f = new File(rootFolder + "lms09.txt");

		Set<Firma> list = new HashSet<Firma>();
		BufferedReader reader = null;

		try {

			reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "CP865"));

			while (reader.ready()) {

				String line = reader.readLine();

				if (line.length() > 0) {
					list.add(parse(line));
				}
			}
		}
		finally {

			try {
				if (reader != null) reader.close();
			}
			catch (Exception e) {
				logger.warn("Could not close FileReader");
			}
		}

		return list;
	}


	private static Firma parse(String line) {

		Firma obj = new Firma();

		for (int fieldNo = 0; fieldNo < NUM_FIELDS; fieldNo++) {
			
			 if (getLength(fieldNo) > 0) {
				
				String value = line.substring(getOffset(fieldNo), getOffset(fieldNo) + getLength(fieldNo)).trim();
				setFieldValue(obj, fieldNo, value);
			}
		}

		return obj;
	}
}