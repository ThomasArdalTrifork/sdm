package com.trifork.sdm.importer.importers;

public class FileImporterException extends Exception {
	
	private static final long serialVersionUID = 1L;


	public FileImporterException(String message, Throwable cause) {

		super(message, cause);
	}


	public FileImporterException(String message) {

		super(message);
	}
}
