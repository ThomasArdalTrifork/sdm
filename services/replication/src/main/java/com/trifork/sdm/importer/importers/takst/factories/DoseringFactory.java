package com.trifork.sdm.importer.importers.takst.factories;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.trifork.sdm.models.takst.Dosering;

public class DoseringFactory extends AbstractFactory
{

	private static void setFieldValue(Dosering obj, int fieldNo, String value)
	{
		if ("".equals(value)) value = null;
		switch (fieldNo) {
		case 0:
			obj.setDoseringskode(toLong(value));
			break;
		case 1:
			obj.setDoseringKortTekst(value);
			break;
		case 2:
			obj.setDoseringstekstTotal(value);
			break;
		case 3:
			obj.setAntalEnhDoegn(toLong(value));
			break;
		case 4:
			obj.setDoseringstekstLinie1(value);
			break;
		case 5:
			obj.setDoseringstekstLinie2(value);
			break;
		case 6:
			obj.setDoseringstekstLinie3(value);
			break;
		case 7:
			obj.setAktivInaktiv(value);
			break;
		default:
			break;
		}
	}


	private static int getOffset(int fieldNo)
	{
		switch (fieldNo) {
		case 0:
			return 0;
		case 1:
			return 7;
		case 2:
			return 17;
		case 3:
			return 95;
		case 4:
			return 104;
		case 5:
			return 130;
		case 6:
			return 156;
		case 7:
			return 182;
		default:
			return -1;
		}
	}


	private static int getLength(int fieldNo)
	{
		switch (fieldNo) {
		case 0:
			return 7;
		case 1:
			return 10;
		case 2:
			return 78;
		case 3:
			return 9;
		case 4:
			return 26;
		case 5:
			return 26;
		case 6:
			return 26;
		case 7:
			return 1;
		default:
			return -1;
		}
	}


	private static int getNumberOfFields()
	{
		return 8;
	}


	private static String getLmsName()
	{
		return "LMS28";
	}


	public static ArrayList<Dosering> read(String rootFolder) throws IOException
	{

		File f = new File(rootFolder + getLmsName().toLowerCase() + ".txt");

		ArrayList<Dosering> list = new ArrayList<Dosering>();
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "CP865"));
			while (reader.ready())
			{
				String line = reader.readLine();
				if (line.length() > 0)
				{
					list.add(parse(line));
				}
			}
			return list;
		}
		finally
		{
			try
			{
				if (reader != null)
				{
					reader.close();
				}
			}
			catch (Exception e)
			{
				logger.warn("Could not close FileReader");
			}
		}
	}


	private static Dosering parse(String line)
	{
		Dosering obj = new Dosering();
		for (int fieldNo = 0; fieldNo < getNumberOfFields(); fieldNo++)
		{
			if (getLength(fieldNo) > 0)
			{
				// System.out.print("Getting field "+fieldNo+" from"+getOffset(fieldNo)+" to "+(getOffset(fieldNo)+getLength(fieldNo)));
				String value = line.substring(getOffset(fieldNo),
						getOffset(fieldNo) + getLength(fieldNo)).trim();
				// System.out.println(": "+value);
				setFieldValue(obj, fieldNo, value);
			}
		}
		return obj;
	}
}