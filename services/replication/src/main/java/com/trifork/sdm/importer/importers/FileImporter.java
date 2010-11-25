package com.trifork.sdm.importer.importers;

import java.io.File;
import java.util.List;


public interface FileImporter
{

	public boolean areRequiredInputFilesPresent(List<File> files);


	public void importFiles(List<File> files) throws FileImporterException;

}
