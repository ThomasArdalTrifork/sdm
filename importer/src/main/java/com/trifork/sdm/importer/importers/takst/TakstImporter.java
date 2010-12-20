package com.trifork.sdm.importer.importers.takst;

import static java.lang.String.format;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.trifork.sdm.importer.importers.FileImporterControlledIntervals;
import com.trifork.sdm.importer.importers.FileImporterException;
import com.trifork.sdm.importer.importers.FileParseException;
import com.trifork.sdm.importer.importers.takst.parsers.ATCKoderOgTekstParser;
import com.trifork.sdm.importer.importers.takst.parsers.AdministrationsvejParser;
import com.trifork.sdm.importer.importers.takst.parsers.DivEnhederParser;
import com.trifork.sdm.importer.importers.takst.parsers.DoseringParser;
import com.trifork.sdm.importer.importers.takst.parsers.DoseringskodeParser;
import com.trifork.sdm.importer.importers.takst.parsers.IndikationParser;
import com.trifork.sdm.importer.importers.takst.parsers.IndikationskodeParser;
import com.trifork.sdm.importer.importers.takst.parsers.KlausuleringParser;
import com.trifork.sdm.importer.importers.takst.parsers.LaegemiddelParser;
import com.trifork.sdm.importer.importers.takst.parsers.LaegemiddelformBetegnelserParser;
import com.trifork.sdm.importer.importers.takst.parsers.MedicintilskudParser;
import com.trifork.sdm.importer.importers.takst.parsers.PakningParser;
import com.trifork.sdm.importer.persistence.mysql.MySQLConnectionManager;
import com.trifork.sdm.importer.persistence.mysql.MySQLTemporalDao;
import com.trifork.sdm.models.takst.ATCKoderOgTekst;
import com.trifork.sdm.models.takst.Administrationsvej;
import com.trifork.sdm.models.takst.Dosering;
import com.trifork.sdm.models.takst.Doseringskode;
import com.trifork.sdm.models.takst.Indikation;
import com.trifork.sdm.models.takst.Indikationskode;
import com.trifork.sdm.models.takst.Klausulering;
import com.trifork.sdm.models.takst.Laegemiddel;
import com.trifork.sdm.models.takst.LaegemiddelAdministrationsvejRef;
import com.trifork.sdm.models.takst.LaegemiddelformBetegnelser;
import com.trifork.sdm.models.takst.Medicintilskud;
import com.trifork.sdm.models.takst.Pakning;
import com.trifork.sdm.models.takst.Pakningsstoerrelsesenhed;
import com.trifork.sdm.models.takst.Styrkeenhed;
import com.trifork.sdm.models.takst.TakstRelease;
import com.trifork.sdm.models.takst.Tidsenhed;
import com.trifork.sdm.models.takst.unused.DivEnheder;
import com.trifork.sdm.util.DateUtils;


public class TakstImporter implements FileImporterControlledIntervals {

	private static final String SUPPORTED_TAKST_VERSION = "12.0";

	private final Logger logger = Logger.getLogger(getClass());

	public static final String[] requiredFileNames = new String[] { "system.txt", "lms01.txt", "lms02.txt", "lms03.txt", "lms04.txt", "lms05.txt", "lms07.txt", "lms09.txt", "lms10.txt", "lms11.txt", "lms12.txt", "lms13.txt", "lms14.txt", "lms15.txt", "lms16.txt", "lms17.txt", "lms18.txt", "lms19.txt", "lms20.txt", "lms23.txt", "lms24.txt", "lms25.txt", "lms26.txt", "lms27.txt", "lms28.txt" };

	private static final DateTimeFormatter weekFormatter = DateTimeFormat.forPattern("xxxxww").withLocale(new Locale("da", "DK"));


	public void importFiles(List<File> files) throws FileImporterException {

		Connection connection = MySQLConnectionManager.getConnection();
		
		try {
			MySQLTemporalDao persister = new MySQLTemporalDao(connection);
			
			File rootDir = getRootDir(files);

			// Store meta information about the takst release itself.
			
			File systemFile = new File(rootDir, "system.txt");
			TakstRelease release = getMetadata(systemFile);
			
			persister.persist(release);

			// There are dependencies among the parsed files.
			// Therefore we have to parse dependencies first.

			// Drug Form Specification (Optional).
			// These are not persisted in their own table.

			Map<Object, LaegemiddelformBetegnelser> specifications;

			File file22 = new File(rootDir, "lms22.txt");

			// The Specification file might not exist in which case
			// we simply create an empty set of specifications for later use.
			
			if (file22.exists()) {
				specifications = new LaegemiddelformBetegnelserParser().read(file22);
			}
			else {
				specifications = new HashMap<Object, LaegemiddelformBetegnelser>();
			}
					
			
			// ATC Texts (Required).
			
			File file12 = new File(rootDir, "lms12.txt");
			Map<Object, ATCKoderOgTekst> codesAndText = new ATCKoderOgTekstParser().read(file12);
			
			persister.persist(codesAndText.values());
			
			// Drugs (Required).

			File file01 = new File(rootDir, "lms01.txt");
			Map<Object, Laegemiddel> drugs = new LaegemiddelParser(specifications, codesAndText).read(file01);

			persister.persist(drugs.values());
			
			// Drug Packagings (Required).
			
			File file02 = new File(rootDir, "lms02.txt");
			Set<Pakning> drugPackagings = new PakningParser(drugs).read(file02);
			
			persister.persist(drugPackagings);
			
			// Administration Types (Required).
			
			File file11 = new File(rootDir, "lms11.txt");
			Map<Object, Administrationsvej> administrations = new AdministrationsvejParser().read(file11);
			
			persister.persist(administrations.values());
			
			// Populate the join table between Drugs and Administrations.

			Set<LaegemiddelAdministrationsvejRef> refs = new HashSet<LaegemiddelAdministrationsvejRef>();

			for (Laegemiddel drug : drugs.values()) {

				refs.addAll(getAdministrationsveje(drug, administrations));
			}
			
			persister.persist(refs);
			
			// Drug Supplements

			File file16 = new File(rootDir, "lms16.txt");
			Set<Medicintilskud> medicintilskud = new MedicintilskudParser().read(file16);
			
			persister.persist(medicintilskud);
			
			// Clauses
			
			File file17 = new File(rootDir, "lms17.txt");
			Set<Klausulering> klausulering = new KlausuleringParser().read(file17);
			
			persister.persist(klausulering);
			
			// Indications

			File file25 = new File(rootDir, "lms25.txt");
			Set<Indikationskode> indikationskoder = new IndikationskodeParser().read(file25);
			
			persister.persist(indikationskoder);
			
			File file26 = new File(rootDir, "lms26.txt");
			Set<Indikation> indications = new IndikationParser().read(file26);

			persister.persist(indications);
			
			// Dosages
			
			File file27 = new File(rootDir, "lms27.txt");
			Set<Doseringskode> doseringskode = new DoseringskodeParser().read(file27);
			
			persister.persist(doseringskode);
			
			File file28 = new File(rootDir, "lms28.txt");		
			Set<Dosering> dosages = new DoseringParser().read(file28);
			
			persister.persist(dosages);
			
			// Units (Expiration Dates, Package Sizes and Drug Strength).
			
			File file15 = new File(rootDir, "lms15.txt");
			Set<DivEnheder> divEnheder = new DivEnhederParser().read(file15);
			
			// Subdivide the units into their respective categories.
			
			Set<Tidsenhed> timeUnits = new HashSet<Tidsenhed>();
			Set<Pakningsstoerrelsesenhed> packageUnits = new HashSet<Pakningsstoerrelsesenhed>();
			Set<Styrkeenhed> strengthUnits = new HashSet<Styrkeenhed>();

			for (DivEnheder enhed : divEnheder) {

				if (enhed.isEnhedstypeTid()) {

					timeUnits.add(new Tidsenhed(enhed));
				}
				else if (enhed.isEnhedstypePakning()) {

					packageUnits.add(new Pakningsstoerrelsesenhed(enhed));
				}
				else if (enhed.isEnhedstypeStyrke()) {

					strengthUnits.add(new Styrkeenhed(enhed));
				}
			}

			persister.persist(timeUnits);
			persister.persist(packageUnits);
			persister.persist(strengthUnits);
			
			
			// TODO: Filter out veterinary meds.
			
			
			// Commit the entities when all has gone well.
			
			connection.commit();
		}
		catch (Throwable t) {
			
			try {
				connection.rollback();
			}
			catch (Throwable t2) {
				// Nothing we can do.
			}
				
			throw new FileImporterException("En error occured durring import of 'takst'.", t);
		}
		finally {
			
			MySQLConnectionManager.close(connection);
		}
	}
	
	
	public Set<LaegemiddelAdministrationsvejRef> getAdministrationsveje(Laegemiddel drug, Map<Object, Administrationsvej> administrationsveje) {

		// A list of administrations is stored as a 8 char long string per drug
		// in the file LMS01. Each administration code is 2 chars are refers to
		// the texts in the file LMS11.

		// Here we lookup the texts are store them with the drugs.

		Set<LaegemiddelAdministrationsvejRef> refs = new HashSet<LaegemiddelAdministrationsvejRef>();

		for (String code : drug.getAdministrationCodes()) {

			Administrationsvej administration = administrationsveje.get(code);

			if (administration == null) {

				logger.warn(format("Administration code '%s' refers to a administration that was not found.", code));
			}
			else {
				refs.add(new LaegemiddelAdministrationsvejRef(drug, administration));
			}
		}
		
		return refs;
	}


	public boolean areRequiredInputFilesPresent(List<File> files) {

		// HACK: There is no need to check this since the parser will complain
		// otherwise.

		return true;
	}

	
	public Date getNextImportExpectedBefore(Date lastImport) {

		// There are two different types of releases: Normal and occasional releases.
		// Occasional releases arrive on ad-hoc basis and we cannot foresee when we can expect them.
		
		Connection connection = null;
		Statement statement = null;
		Calendar ordinaryTakst = null;

		try {
			connection = MySQLConnectionManager.getAutoCommitConnection();

			// Check to see if we have any releases at all.

			statement = connection.createStatement();
			ResultSet countResult = statement.executeQuery("SELECT COUNT(*) AS RowCount FROM TakstVersion");
			countResult.next();
			int count = countResult.getInt("RowCount");
			countResult.close();
			statement.close();

			if (count != 0) {
				ResultSet rs = connection.createStatement().executeQuery("SELECT MAX(TakstUge) FROM TakstVersion");

				if (rs.next()) {

					String lastWeek = rs.getString(1);

					logger.info(lastWeek);

					ordinaryTakst = weekFormatter.parseDateTime(lastWeek).toGregorianCalendar();

					// Next ordinary release expected in 14 days.
					ordinaryTakst.add(Calendar.DATE, 14);

					// We want the ordinary release to be imported 36 hours
					// before it is suppose to be in effect.
					ordinaryTakst.add(Calendar.HOUR, -36);
				}
			}
		}
		catch (Exception e) {

			logger.error("Cannot get greatest TakstVersion from database.", e);
		}
		finally {

			MySQLConnectionManager.close(statement, connection);
		}

		if (ordinaryTakst == null) {
			// Something failed. Raise an alarm by setting the expected next
			// import to past time.
			// FIXME (thb): Should this not be done more explicitly?

			ordinaryTakst = Calendar.getInstance();
			ordinaryTakst.add(Calendar.HOUR, -1);
		}

		return ordinaryTakst.getTime();
	}


	private TakstRelease getMetadata(File systemFile) throws FileParseException, ParseException {

		String metadata = null;
		
		try {

			BufferedReader reader = new BufferedReader(new FileReader(systemFile));
			metadata = reader.readLine();
		}
		catch (Exception e) {

			throw new FileParseException(format("Could not read from '%s'.", systemFile.getAbsoluteFile()), e);
		}

		// Check the takst version.

		String version = metadata.substring(2, 7).trim();

		if (!SUPPORTED_TAKST_VERSION.equals(version)) {

			throw new FileParseException(format("Unsupported takst version: '%s'", version));
		}

		// Figure out from which date this release is valid.

		String dateString = metadata.substring(47, 55);
		Date validFrom = new SimpleDateFormat("yyyyMMdd").parse(dateString);

		int releaseYear = Integer.parseInt(metadata.substring(87, 91));
		int releaseWeek = Integer.parseInt(metadata.substring(91, 93));

		return new TakstRelease(validFrom, DateUtils.FUTURE, releaseYear, releaseWeek);
	}


	private File getRootDir(List<File> files) throws FileParseException {

		File rootFolder = null;

		for (File file : files) {
			// FIXME: Do this in a smarter way. This is ugly.

			if (file.getName().endsWith(TakstImporter.requiredFileNames[0])) {

				rootFolder = new File(file.getParent());
				break;
			}
		}

		if (rootFolder == null) throw new FileParseException("Cannot extract root folder for takst parsing");

		return rootFolder;
	}
}
