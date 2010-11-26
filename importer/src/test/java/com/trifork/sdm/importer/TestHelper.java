package com.trifork.sdm.importer;

import java.io.File;
import java.net.URL;


public class TestHelper {

	public static File getFile(String path) {

		File file = null;

		try {
			URL fileURL = TestHelper.class.getClassLoader().getResource(path);
			file = new File(fileURL.toURI());
		}
		catch (Throwable t) {

			final String message = String.format("Could not load file '%s'.", path);
			throw new RuntimeException(message, t);
		}

		return file;
	}
}
