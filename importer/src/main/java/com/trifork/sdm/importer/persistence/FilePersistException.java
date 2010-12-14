package com.trifork.sdm.importer.persistence;

import com.trifork.sdm.importer.importers.FileImporterException;

public class FilePersistException extends FileImporterException
{
	private static final long serialVersionUID = 1L;


	public FilePersistException(String message)
	{
		super(message);
	}


	public FilePersistException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
