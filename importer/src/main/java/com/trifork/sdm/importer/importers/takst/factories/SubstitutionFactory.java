package com.trifork.sdm.importer.importers.takst.factories;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.trifork.sdm.models.takst.unused.Substitution;

public class SubstitutionFactory extends AbstractFactory
{

	private static void setFieldValue(Substitution obj, int fieldNo, String value)
	{
		if ("".equals(value)) value = null;
		switch (fieldNo) {
		case 0:
			obj.setSubstitutionsgruppenummer(toLong(value));
			break;
		case 1:
			obj.setReceptensVarenummer(toLong(value));
			break;
		case 2:
			obj.setNumeriskPakningsstoerrelse(toLong(value));
			break;
		case 3:
			obj.setProdAlfabetiskeSekvensplads(value);
			break;
		case 5:
			obj.setSubstitutionskodeForPakning(value);
			break;
		case 6:
			obj.setBilligsteVarenummer(toLong(value));
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
			return 4;
		case 2:
			return 10;
		case 3:
			return 18;
		case 5:
			return 34;
		case 6:
			return 35;
		default:
			return -1;
		}
	}


	private static int getLength(int fieldNo)
	{
		switch (fieldNo) {
		case 0:
			return 4;
		case 1:
			return 6;
		case 2:
			return 8;
		case 3:
			return 9;
		case 5:
			return 1;
		case 6:
			return 6;
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
		return "LMS04";
	}


	public static ArrayList<Substitution> read(String rootFolder) throws IOException
	{

		File f = new File(rootFolder + getLmsName().toLowerCase() + ".txt");

		ArrayList<Substitution> list = new ArrayList<Substitution>();
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


	private static Substitution parse(String line)
	{
		Substitution obj = new Substitution();
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