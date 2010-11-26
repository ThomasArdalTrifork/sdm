package com.trifork.sdm.importer.importers;

import java.util.Calendar;

public interface FileImporterControlledIntervals extends FileImporter
{
	public Calendar getNextImportExpectedBefore(Calendar lastImport);
}
