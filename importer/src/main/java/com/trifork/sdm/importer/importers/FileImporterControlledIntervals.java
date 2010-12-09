package com.trifork.sdm.importer.importers;

import java.util.Date;


public interface FileImporterControlledIntervals extends FileImporter {
	
	Date getNextImportExpectedBefore(Date lastImport);
}
