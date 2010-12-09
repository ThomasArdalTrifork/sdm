package com.trifork.sdm.importer.spoolers;

import static java.lang.String.format;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.trifork.sdm.importer.importers.FileImporter;
import com.trifork.sdm.importer.importers.FileImporterControlledIntervals;
import com.trifork.sdm.importer.importers.FileImporterException;
import com.trifork.sdm.importer.importers.ImportTimeManager;
import com.trifork.sdm.util.DateUtils;


/**
 * FileSpooler. The implementation of the file spooler. Based on the
 * FileSpoolerSetup the spooler is started and a monitor thread is spanned. The
 * FileSpooler continue to monitor the spooler but all activations are made in
 * the newly created thread.
 * 
 * @author Jan Buchholdt
 */
public class FileSpooler extends BasicSpooler {
	private static final Logger logger = Logger.getLogger(FileSpooler.class);

	Calendar stabilizationPeriodEnd; // TODO: These fields should not be public.
										// Not even for testing.

	long inputdirSignature;

	public Collection<String> activeFiles; // TODO: Public?


	public FileSpooler(FileSpoolerSetup setup) {

		this.setup = setup;
		setStatus(Status.INITIATING);

		try {
			importer = setup.getImporterClass().newInstance();
		}

		catch (Exception e) {
			logger.error("Could not instantiate importer of class", e);
			setMessage("Spooler cannot get an instance if importer class. Please change the setup");
			setStatus(Status.ERROR);
			return;
		}

		lastRun = ImportTimeManager.getLastImportTime(setup.getName());
		inputDir = validateWorkDir(setup.getInputPath());
		processingDir = validateWorkDir(setup.getProcessingPath());
		rejectedDir = validateWorkDir(setup.getRejectPath());

		try {
			moveProcessingFilesBackToInput();
		}
		catch (IOException e) {
			throw new RuntimeException("Could not move processing files back to input dir", e);
		}

		setStatus(Status.RUNNING);
		setStatus(Activity.AWAITING);
	}


	/**
	 * Checks if new files are present, and handle them if true
	 */
	@Override
	public void execute() {

		// Check for rejected files
		File[] rejFiles = rejectedDir.listFiles();
		if (rejFiles != null && rejFiles.length > 0) {
			// logger.debug("Rejected files exist for spooler " +
			// setup.getName());
			if (getMessage() == null)
				setMessage("Rejected files exists in: " + getRejectedDir().getAbsolutePath());
			setStatus(Status.ERROR);
			return;
		}
		else {
			setStatus(Status.RUNNING);
		}

		// Check for input files
		File[] inputFiles = inputDir.listFiles();
		if (inputFiles != null && inputFiles.length > 0) {
			processInputFiles(inputFiles);
		}
		else {
			setStatus(Activity.AWAITING);
		}
	}


	private void processInputFiles(File[] inputFiles) {

		setActiveFiles(inputFiles);
		logger.debug("spooler: " + setup.getName() + " - " + inputFiles.length + " input files detected");
		if (inputdirSignature != getDirSignature(inputDir)) {
			logger.debug("spooler: " + setup.getName()
					+ "files changed in input dir. Starting stabilization period");
			startStabilizationPeriod();
		}
		if (Calendar.getInstance().after(stabilizationPeriodEnd)) {
			// logger.debug("spooler: " + setup.getName() +
			// " - Stabilizing wait complete");
			boolean allRequiredPresent = false;
			try {
				allRequiredPresent = importer.areRequiredInputFilesPresent(Arrays.asList(inputFiles));
			}
			catch (Exception e) {
				logger.debug("importer.areRequiredInputFilesPresent threw exception. Moving files to rejected");
				try {
					moveToRejected(inputFiles);

				}
				catch (IOException e1) {
					throw new RuntimeException(e1);
				}
				throw new RuntimeException(e);
			}
			if (!allRequiredPresent) {
				try {
					moveToRejected(inputFiles);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
				String message = "Didn't get all required files";
				setMessage(message);
				throw new RuntimeException(message);
			}
			else {
				File fileSet;
				try {
					fileSet = moveToProcessingFileset(inputFiles);
					doImport(fileSet);
					clearActiveFiles();
				}
				catch (IOException e) {
					String message = "could not move files to processing";
					setMessage(message + " " + e.getMessage());
					logger.error(message, e);
					setStatus(Status.ERROR);
				}
			}
		}
		else
			logger.debug("spooler: "
					+ setup.getName()
					+ " - Waiting for inputFiles to stabilize in "
					+ inputDir.getAbsolutePath()
					+ " in "
					+ (stabilizationPeriodEnd.getTime().getTime() - Calendar.getInstance().getTime()
							.getTime()) / 1000.0 + " seconds");

	}


	private void moveToRejected(File[] inputFiles) throws IOException {

		// Move the files to rejected
		File p = moveToProcessingFileset(inputFiles);
		moveFilesetToRejected(p);
	}


	/**
	 * Sets the files which will be displayed as active. Only for display
	 * purposes.
	 * 
	 * @param files
	 */
	private void setActiveFiles(File[] files) {

		clearActiveFiles();
		for (File file : files) {
			activeFiles.add(file.getAbsolutePath() + "\n");
		}
	}


	/**
	 * Returns a signature of the dir's contained files.
	 * 
	 * @param inputDir
	 * @return
	 */
	static long getDirSignature(File inputDir) {

		long hash = 0;
		for (File file : inputDir.listFiles())
			hash += file.getName().hashCode() * (file.lastModified() + file.length());
		return hash;
	}


	private void startStabilizationPeriod() {

		setStatus(Activity.STABILIZING);
		Calendar end = Calendar.getInstance();
		end.add(Calendar.SECOND, setup.getStableSeconds());
		stabilizationPeriodEnd = end;
		inputdirSignature = getDirSignature(inputDir);
	}


	private File moveToProcessingFileset(File[] files) throws IOException {

		setActiveFiles(files);
		File fileSet = new File(processingDir.getAbsolutePath() + "/"
				+ DateUtils.toFilenameDatetime(Calendar.getInstance()) + "/");
		fileSet.mkdir();
		for (int i = 0; i < files.length; i++) {
			File processingFile = new File(fileSet.getAbsolutePath() + "/" + files[i].getName());
			FileUtils.moveFile(files[i], processingFile);
			files[i] = processingFile;
		}
		logger.debug("returning fileset containing: " + fileSet.listFiles().length + " files");
		return fileSet;
	}


	private void doImport(File fileSet) {

		logger.debug("spooler: " + setup.getName() + " -  starting import...");

		try {
			setStatus(Activity.IMPORTING);

			importer.importFiles(Arrays.asList(fileSet.listFiles()));

			logger.debug("spooler: " + setup.getName() + " - import complete");

			setStatus(Activity.AWAITING);

			lastRun = new Date();
			ImportTimeManager.setImportTime(setup.getName(), lastRun);

			for (File file : fileSet.listFiles()) {
				file.delete();
			}

			boolean deleted = fileSet.delete();

			if (!deleted) {
				logger.error("Couldn't delete fileset with files: " + fileSet.getAbsolutePath());
				setStatus(Status.ERROR);
			}
		}
		catch (FileImporterException e) {

			logger.error(
					format("Spooler: %s - Error during import of fileset ", setup.getName(),
							fileSet.getAbsolutePath()), e);
			File rejectedFileSet = null;
			try {
				rejectedFileSet = moveFilesetToRejected(fileSet);
			}
			catch (IOException e1) {
				throw new RuntimeException("could not move files to rejected", e);
			}
			printErrorMessageToRejectFile(e, rejectedFileSet);
			setMessage(e.getMessage());
			setStatus(Status.ERROR);
		}
	}


	private File moveFilesetToRejected(File fileSet) throws IOException {

		clearActiveFiles();

		File rejectedFileSet = new File(rejectedDir + "/" + fileSet.getName());
		FileUtils.moveDirectory(fileSet, rejectedFileSet);

		return rejectedFileSet;
	}


	private void clearActiveFiles() {

		activeFiles = new ArrayList<String>();
	}


	private final FileSpoolerSetup setup;

	private File inputDir = null;
	private File processingDir = null;
	private File rejectedDir = null;

	private Date lastRun;

	FileImporter importer;


	/**
	 * Check if the processing directory is empty. If not move the files back to
	 * inputdir
	 */
	void moveProcessingFilesBackToInput() throws IOException {

		if (processingDir.listFiles().length > 0) {
			// Recursive move all files back
			for (File folder : processingDir.listFiles()) {
				if (folder.isDirectory()) {
					// All children should be folders
					for (File f : folder.listFiles()) {
						File inputFile = new File(inputDir + "/" + f.getName());
						FileUtils.moveFile(f, inputFile);
					}

					folder.delete();
				}
			}
		}
	}


	File validateWorkDir(String path) {

		File dir = new File(path);

		if (!dir.exists() && !dir.mkdirs())
			throw new RuntimeException("Spooler dir " + path + " cannot be created!");
		if (!dir.canRead())
			throw new RuntimeException("Spooler dir " + path + " is not readable. Please change permissions");
		if (!dir.canWrite())
			throw new RuntimeException("Spooler dir " + path + " is not writable. Please change permissions");
		if (!dir.isDirectory()) throw new RuntimeException("Spooler dir " + path + " is not a directory!");

		return dir;
	}


	public boolean isRejectedDirEmpty() {

		return rejectedDir.listFiles().length == 0;
	}


	public FileSpoolerSetup getSetup() {

		return setup;
	}


	File getInputDir() {

		return inputDir;
	}


	File getProcessingDir() {

		return processingDir;
	}


	File getRejectedDir() {

		return rejectedDir;
	}


	public FileImporter getImporter() {

		return importer;
	}


	public String getNextImportExpectedBeforeFormatted() {

		if (getImporter() instanceof FileImporterControlledIntervals) {

			FileImporterControlledIntervals importer = (FileImporterControlledIntervals) getImporter();

			return importer.getNextImportExpectedBefore(lastRun).toString();
		}
		else
			return "<no expectation defined>";
	}


	public String getLastImportFormatted() {

		Date lastImport = ImportTimeManager.getLastImportTime(setup.getName());

		if (lastImport == null)
			return "Never";
		else
			return lastImport.toString();
	}


	public Date getLastRun() {

		return lastRun;
	}


	public boolean isOverdue() {

		if (!(getImporter() instanceof FileImporterControlledIntervals)) {

			return false;
		}

		FileImporterControlledIntervals importer = (FileImporterControlledIntervals) getImporter();

		return importer.getNextImportExpectedBefore(lastRun).getTime() > System.currentTimeMillis();
	}


	/**
	 * Print the exception to the rejectfile
	 * 
	 * @param e
	 * @param fileSet
	 *            The file path to put the RejectReason into.
	 */
	void printErrorMessageToRejectFile(FileImporterException e, File fileSet) {

		try {
			File rejFile = new File(fileSet.getAbsolutePath() + "/RejectReason");
			rejFile.createNewFile();
			BufferedWriter br = new BufferedWriter(new FileWriter(rejFile));
			PrintWriter pr = new PrintWriter(br);
			pr.println(DateUtils.toFilenameDatetime(GregorianCalendar.getInstance()));
			String importerClass = "";
			if (getImporter() != null) importerClass += getImporter().getClass();
			pr.println("Fileset rejected because FileImporterException was thrown. Importer class: "
					+ importerClass);
			pr.println("******************************** Message ******************************");
			pr.println(e.getMessage());
			pr.println("******************************** Stack Trace ******************************");
			e.printStackTrace(pr);
			pr.close();
		}
		catch (Exception e1) {
			logger.error(
					"Exception when creating RejectReason file in reject dir " + fileSet.getAbsolutePath(),
					e1);
		}
	}


	public Collection<String> getActiveFiles() {

		return activeFiles;
	}


	@Override
	public String getName() {

		return setup.getName();
	}

}
