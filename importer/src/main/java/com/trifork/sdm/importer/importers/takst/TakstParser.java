package com.trifork.sdm.importer.importers.takst;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.trifork.sdm.importer.importers.FileParseException;
import com.trifork.sdm.importer.importers.takst.factories.ATCKoderOgTekstFactory;
import com.trifork.sdm.importer.importers.takst.factories.AdministrationsvejParser;
import com.trifork.sdm.importer.importers.takst.factories.BeregningsreglerParser;
import com.trifork.sdm.importer.importers.takst.factories.DivEnhederFactory;
import com.trifork.sdm.importer.importers.takst.factories.DoseringFactory;
import com.trifork.sdm.importer.importers.takst.factories.DoseringskodeFactory;
import com.trifork.sdm.importer.importers.takst.factories.EmballagetypeKoderParser;
import com.trifork.sdm.importer.importers.takst.factories.EnhedspriserFactory;
import com.trifork.sdm.importer.importers.takst.factories.FirmaParser;
import com.trifork.sdm.importer.importers.takst.factories.IndholdsstofferFactory;
import com.trifork.sdm.importer.importers.takst.factories.IndikationFactory;
import com.trifork.sdm.importer.importers.takst.factories.IndikationskodeFactory;
import com.trifork.sdm.importer.importers.takst.factories.KlausuleringFactory;
import com.trifork.sdm.importer.importers.takst.factories.LaegemiddelFactory;
import com.trifork.sdm.importer.importers.takst.factories.LaegemiddelformBetegnelserFactory;
import com.trifork.sdm.importer.importers.takst.factories.LaegemiddelnavnFactory;
import com.trifork.sdm.importer.importers.takst.factories.MedicintilskudFactory;
import com.trifork.sdm.importer.importers.takst.factories.OpbevaringsbetingelserFactory;
import com.trifork.sdm.importer.importers.takst.factories.OplysningerOmDosisdispenseringFactory;
import com.trifork.sdm.importer.importers.takst.factories.PakningFactory;
import com.trifork.sdm.importer.importers.takst.factories.PakningskombinationerFactory;
import com.trifork.sdm.importer.importers.takst.factories.PakningskombinationerUdenPriserFactory;
import com.trifork.sdm.importer.importers.takst.factories.PriserFactory;
import com.trifork.sdm.importer.importers.takst.factories.RekommandationerFactory;
import com.trifork.sdm.importer.importers.takst.factories.SpecialeForNBSFactory;
import com.trifork.sdm.importer.importers.takst.factories.SubstitutionAfLaegemidlerUdenFastPrisFactory;
import com.trifork.sdm.importer.importers.takst.factories.SubstitutionFactory;
import com.trifork.sdm.importer.importers.takst.factories.TilskudsintervallerFactory;
import com.trifork.sdm.importer.importers.takst.factories.TilskudsprisgrupperPakningsniveauFactory;
import com.trifork.sdm.importer.importers.takst.factories.UdgaaedeNavneFactory;
import com.trifork.sdm.importer.importers.takst.factories.UdleveringsbestemmelserFactory;
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
import com.trifork.sdm.models.takst.Takst;
import com.trifork.sdm.models.takst.Tidsenhed;
import com.trifork.sdm.models.takst.unused.Beregningsregler;
import com.trifork.sdm.models.takst.unused.DivEnheder;
import com.trifork.sdm.models.takst.unused.EmballagetypeKoder;
import com.trifork.sdm.models.takst.unused.Enhedspriser;
import com.trifork.sdm.models.takst.unused.Firma;
import com.trifork.sdm.models.takst.unused.Indholdsstoffer;
import com.trifork.sdm.models.takst.unused.Laegemiddelnavn;
import com.trifork.sdm.models.takst.unused.Opbevaringsbetingelser;
import com.trifork.sdm.models.takst.unused.OplysningerOmDosisdispensering;
import com.trifork.sdm.models.takst.unused.Pakningskombinationer;
import com.trifork.sdm.models.takst.unused.PakningskombinationerUdenPriser;
import com.trifork.sdm.models.takst.unused.Priser;
import com.trifork.sdm.models.takst.unused.Rekommandationer;
import com.trifork.sdm.models.takst.unused.SpecialeForNBS;
import com.trifork.sdm.models.takst.unused.Substitution;
import com.trifork.sdm.models.takst.unused.SubstitutionAfLaegemidlerUdenFastPris;
import com.trifork.sdm.models.takst.unused.Tilskudsintervaller;
import com.trifork.sdm.models.takst.unused.TilskudsprisgrupperPakningsniveau;
import com.trifork.sdm.models.takst.unused.UdgaaedeNavne;
import com.trifork.sdm.models.takst.unused.Udleveringsbestemmelser;
import com.trifork.sdm.persistence.Dataset;
import com.trifork.sdm.util.DateUtils;


public class TakstParser {
	
	private static final String SUPPORTED_TAKST_VERSION = "12.0";

	static Logger logger = Logger.getLogger(TakstParser.class);


	public Takst parseTakst(List<File> files) throws FileParseException {

		String rootFolder = "";

		for (File file : files) {

			// TODO: This is really ugly.
			
			if (file.getName().endsWith(TakstImporter.requiredFileNames[0])) {

				rootFolder = file.getParent() + "/";
				break;
			}
		}

		if ("".equals(rootFolder)) throw new FileParseException("Cannot extract root folder for takst parsing");

		return parseTakst(rootFolder);
	}


	public Takst parseTakst(String rootFolder) throws FileParseException {

		Takst takst;

		try {
			// Parse required meta information first

			String systemline = getSystemLine(rootFolder);
			String version = getVersion(systemline);

			if (!SUPPORTED_TAKST_VERSION.equals(version)) {

				logger.warn("Trying to parse unknown version: '" + version + "' of the takst! Only known version is: '" + SUPPORTED_TAKST_VERSION + "'");
			}

			Date fromDate = getValidFromDate(systemline);

			logger.debug("Parsing takst version: '" + version + "' validFrom '" + fromDate.toString());

			takst = new Takst(fromDate, DateUtils.FUTURE);

			// Add the takst itself to the takst as a "meta entity" to represent
			// in DB that the takst was loaded.

			takst.setValidityWeekNumber(getValidWeek(systemline));
			takst.setValidityYear(getValidYear(systemline));
		}
		catch (Exception e) {

			throw new FileParseException("An error occured while reading takst metadata", e);
		}

		try {
			// There are dependencies among the parsed files.
			// Therefore we have to parse dependencies first.
			
			// Drug Form Specification (Optional).
			
			Map<Object, LaegemiddelformBetegnelser> betegnelser = new HashMap<Object, LaegemiddelformBetegnelser>();
			
			try {
				Map<Object, LaegemiddelformBetegnelser> parsedBetegnelser = new LaegemiddelformBetegnelserFactory().read(rootFolder);
				betegnelser.putAll(parsedBetegnelser);
			}
			catch (IOException e) {
				// TODO: Use check for file exists instead.
				logger.debug(LaegemiddelformBetegnelserFactory.getLmsName() + " could not be read. Ignoring as it is not required");
			}
			
			// ATC Texts (Required).
			
			Map<Object, ATCKoderOgTekst> codesAndText = new ATCKoderOgTekstFactory().read(rootFolder);

			// Drugs (Required).
			
			Map<Object, Laegemiddel> drugs = new LaegemiddelFactory(betegnelser, codesAndText).read(rootFolder);
			
			// Drug Packagings (Required).

			takst.packaging = new PakningFactory(drugs).read(rootFolder);

			// Prices (Required).
			
			takst.prices = new HashSet<Priser>(PriserFactory.read(rootFolder));

			// Substitutions for fixed price drugs (Required).
			
			takst.substitutions = new HashSet<Substitution>(SubstitutionFactory.read(rootFolder));

			// Substitutions for non-fixed price drugs (Required).
			
			takst.substitutionAfLaegemidlerUdenFastPris = new HashSet<SubstitutionAfLaegemidlerUdenFastPris>(SubstitutionAfLaegemidlerUdenFastPrisFactory.read(rootFolder));

			// Levels.
			
			//takst.tilskudsprisgrupperPakningsniveau = new HashSet<TilskudsprisgrupperPakningsniveau>(TilskudsprisgrupperPakningsniveauFactory.read(rootFolder));

			// Drug companies.
			
			//takst.firmaer = new FirmaParser().read(rootFolder);

			// Drugs that are not continued.
			
			//takst.udgaaedeNavne = new HashSet<UdgaaedeNavne>(UdgaaedeNavneFactory.read(rootFolder));
			
			// Application.
			
			takst.administrationsveje = new AdministrationsvejParser().read(rootFolder);
			
			//takst.beregningsregler = new BeregningsreglerParser().read(rootFolder);

			//takst.emballagetypeKoder = new EmballagetypeKoderParser().read(rootFolder);

			//takst.divEnheder = new DivEnhederFactory().read(rootFolder);

			takst.medicintilskud = new MedicintilskudFactory().read(rootFolder);

			takst.klausulering = new KlausuleringFactory().read(rootFolder);

			//takst.udleveringsbestemmelser = new UdleveringsbestemmelserFactory().read(rootFolder);

			//takst.specialeForNBS = new SpecialeForNBSFactory().read(rootFolder);

			//takst.opbevaringsbetingelser = new OpbevaringsbetingelserFactory().read(rootFolder);

			//takst.tilskudsintervaller = new TilskudsintervallerFactory().read(rootFolder);

			//takst.oplysningerOmDosisdispensering = new OplysningerOmDosisdispenseringFactory().read(rootFolder);

			takst.indikationskoder = new IndikationskodeFactory().read(rootFolder);

			takst.indikation = new IndikationFactory().read(rootFolder);

			takst.doseringskode = new DoseringskodeFactory().read(rootFolder);

			takst.dosering = new DoseringFactory().read(rootFolder);
		}
		catch (Exception e) {
			throw new FileParseException("An error occured while reading takst data", e);
		}

		// Now parse optional files one at a time.

		/*
		try {
			takst.laegemiddelnavne = new LaegemiddelnavnFactory().read(rootFolder);
		}
		catch (IOException e) {
			logger.debug(LaegemiddelFactory.getLmsName() + " could not be read. Ignoring as it is not required");
		}
		
		try {
			takst.rekommandationer = new RekommandationerFactory().read(rootFolder);
		}
		catch (IOException e) {
			logger.debug(RekommandationerFactory.getLmsName() + " could not be read. Ignoring as it is not required");
		}
		

		try {
			takst.indholdsstoffer = new IndholdsstofferFactory().read(rootFolder);
		}
		catch (IOException e) {
			logger.debug(IndholdsstofferFactory.getLmsName() + " could not be read. Ignoring as it is not required");
		}
		
		
		try {
			takst.enhedspriser = new EnhedspriserFactory().read(rootFolder);
		}
		catch (IOException e) {
			logger.debug(EnhedspriserFactory.getLmsName() + " could not be read. Ignoring as it is not required");
		}
		

		try {
			takst.pakningskombinationer = new PakningskombinationerFactory().read(rootFolder);
			takst.pakningskombinationerUdenPriser = new PakningskombinationerUdenPriserFactory().read(rootFolder);
		}
		catch (IOException e) {
			logger.debug(PakningskombinationerFactory.getLmsName() + " or " + PakningskombinationerUdenPriserFactory.getLmsName() + " could not be read. Ignoring as they are not required");
		}
		
		*/

		try {
			// Post process.

			addTypedDivEnheder(takst);
			addLaegemiddelAdministrationsvejRefs(takst);
			filterOutVetDrugs(takst);

			return takst;
		}
		catch (Exception e) {
			throw new FileParseException("An error occured while post-processing takst", e);
		}
	}


	/**
	 * Extracts the administrationsveje and adds them to the release.
	 */
	private void addLaegemiddelAdministrationsvejRefs(Takst takst) {

		Set<LaegemiddelAdministrationsvejRef> lars = new HashSet<LaegemiddelAdministrationsvejRef>();
		
		for (Laegemiddel lm : takst.drugs) {
			/*
			for (Administrationsvej av : lm.getAdministrationsveje()) {
				
				lars.add(new LaegemiddelAdministrationsvejRef(lm, av));
			}
			*/
		}
		
		takst.laegemiddelAdministrationsvejRef = lars;
	}


	private String getVersion(String systemline) {

		return systemline.substring(2, 7).trim();
	}


	/**
	 * Sorterer DivEnheder ud på stærke(re) typede entiteter for at matche fmk
	 * stamtabel skemaet
	 * 
	 * @param takst
	 */
	private void addTypedDivEnheder(Takst takst) {

		Set<Tidsenhed> tidsenheder = new HashSet<Tidsenhed>();
		Set<Pakningsstoerrelsesenhed> pakEnheder = new HashSet<Pakningsstoerrelsesenhed>();
		Set<Styrkeenhed> styrkeEnheder = new HashSet<Styrkeenhed>();

		for (DivEnheder enhed : takst.divEnheder) {
			
			if (enhed.isEnhedstypeTid()) {
				
				tidsenheder.add(new Tidsenhed(enhed));
			}
			else if (enhed.isEnhedstypePakning()) {
				
				pakEnheder.add(new Pakningsstoerrelsesenhed(enhed));
			}
			else if (enhed.isEnhedstypeStyrke()) {
				
				styrkeEnheder.add(new Styrkeenhed(enhed));
			}
		}

		takst.tidsenheder = tidsenheder;
		takst.pakningsstoerrelsesenheder = pakEnheder;
		takst.styrkeenheder = styrkeEnheder;
	}


	/**
	 * Filter out veterinære entities
	 * 
	 * @param takst
	 */
	static void filterOutVetDrugs(Takst takst) {

		logger.debug("Filtering input.");
		
		List<Pakning> pakningerToBeRemoved = new ArrayList<Pakning>();

		Dataset<Pakning> pakninger = takst.getDatasetOfType(Pakning.class);

		if (pakninger != null) {
			for (Pakning pakning : pakninger.getEntities()) {
				if (!pakning.isTilHumanAnvendelse()) pakningerToBeRemoved.add(pakning);
			}
			
			pakninger.removeRecords(pakningerToBeRemoved);
		}

		logger.debug("Number of entities after filtering pakninger: " + takst.getAllRecords().size());

		Dataset<Laegemiddel> lmr = takst.getDatasetOfType(Laegemiddel.class);

		List<Laegemiddel> laegemidlerToBeRemoved = new ArrayList<Laegemiddel>();
		if (lmr != null) {
			for (Laegemiddel lm : lmr.getEntities()) {
				if (!lm.isTilHumanAnvendelse()) laegemidlerToBeRemoved.add(lm);
			}
			lmr.removeRecords(laegemidlerToBeRemoved);
		}

		logger.debug("Number of entities after filtering lmr: " + takst.getAllRecords().size());

		Dataset<ATCKoderOgTekst> atckoder = takst.getDatasetOfType(ATCKoderOgTekst.class);

		List<ATCKoderOgTekst> atcToBeRemoved = new ArrayList<ATCKoderOgTekst>();

		if (atckoder != null) {
			for (ATCKoderOgTekst atc : atckoder.getEntities()) {
				if (!atc.isTilHumanAnvendelse()) atcToBeRemoved.add(atc);
			}
			atckoder.removeRecords(atcToBeRemoved);
		}

		logger.debug("Number of entities after filtering atc: " + takst.getAllRecords().size());
	}


	private int getValidYear(String line) {

		return Integer.parseInt(line.substring(87, 91));
	}


	private int getValidWeek(String line) {

		return Integer.parseInt(line.substring(91, 93));
	}


	public Date getValidFromDate(String line) {

		try {

			String dateline = line.substring(47, 55);
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date d = df.parse(dateline);
			System.out.println(dateline);
			Calendar ikraftdato = GregorianCalendar.getInstance();
			ikraftdato.setTime(d);

			return ikraftdato.getTime();
		}
		catch (ParseException e) {

			logger.error("getValidFromDate(" + line + ")", e);
			return null;
		}
	}


	private String getSystemLine(String rootFolder) throws FileParseException {

		try {
			BufferedReader br = new BufferedReader(new FileReader(rootFolder + "/system.txt"));
			return br.readLine();
		}
		catch (Exception e) {
			throw new FileParseException("Error parsing takst: Could not read from " + rootFolder + "/system.txt", e);
		}
	}
}