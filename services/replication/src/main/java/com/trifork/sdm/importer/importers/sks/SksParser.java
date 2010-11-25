package com.trifork.sdm.importer.importers.sks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.trifork.sdm.importer.importers.FileParseException;
import com.trifork.sdm.models.sks.Organisation;
import com.trifork.sdm.persistence.Dataset;

public class SksParser {
	private static final Logger logger = Logger.getLogger(SksParser.class);
    /*
     * NOTE KOPIERET FRA PEM:
     * 
     * �ndringer til SKS-registeret (sgh/afd) indl�ses via deltafiler.
     * Ved etablering af registeret anvendes en deltafil der indeholder samtlige sgh/afd
     * dvs. indlæsningen foretages på præcis samme måde hvadenten der indlæses/opdateres et fuld register eller blot ændringer siden sidst (delta)
     * 
     * Eksempel på deltafil-indhold:
     * afd1301011             197901011979010119821231ANÆSTHESIAFD. AN                                                                                                        084                                            
     * afd1301011             198301011983010119941231ANÆSTESIAFD. AN,ANÆSTESIAFSNIT                                                                                          084                                            
     * afd1301011             199501011999112419991231ANÆSTESIAFD. AN,ANÆSTESIAFSNIT                                                                                          084         SKS     3                          
     * afd1301011             200001012004102920031231Anæstesiologisk klinik AN, anæstesiafsnit                                                                               084         SKS     3                          
     * afd1301011             200401012004102925000101Anæstesi-/operationsklinik, ABD                                                                                         084         SKS     1                          
     *
     * Hver række angiver en sgh/afd med nummer, gyldighedperiode, navn samt operationskode (3=opdatering, 1=ny eller 2=sletning) 
     * Der anvendes fastpositionering dvs. værdierne er altid placeret på samme position og der anvendes whitespaces til at "fylde" ud med 
     * 
     * Der er intet krav om at rækkefølgen for hvert nummer skal være kronologisk
     * dvs. der tages højde for at der efter at være indlæst en sgh/afd med gyldighedsperiode
     *  01.01.2008 - 01.01.2500
     * kan optræde en anden record for samme nummer med gyldighedsperiode
     *  01.01.2000 - 31.13.2007
     * Det garanteres dog at der ikke optræder overlap på gyldighedsperioden for samme nummer.
     * 
     * Operationskoden (action) (position 187-188) angiver om recorden skal betragtes som ny, opdatering eller sletning.
     * Med den m�de hvorp� SKS-registeret anvendes i PEM gælder det at alle entries/versioner af hvert nummer skal være placeret
     *  i Organisationshistorik-tabellen (og altså ikke kun gamle versioner i denne tabel) dvs. det er altid muligt heri at finde den gyldige/aktive sgh/afd
     *  for en bestemt dato. I Organisations-tabellen derimod placeres kun den nyeste record for en given sgh/afd dvs. recorden med nyeste gyldighedsdato. 
     * For at sikre denne versionering skal enhver entry (med operationskode 1 eller 3) altid skal indsættes/opdateres i Organisationshistorik-tabellen,
     *  mens kun nyeste entry (med operationskode 1 eller 3) skal indsættes/opdateres i Organisations-tabellen.   
     * Det antages at der aldrig optræder entries med operationskode 2 (sletning) i deltafilerne. Dog checkes for kode 2 og i så fald ignoreres recorden.
     * Det gælder at operationskode/action (1,2,3) kun er angivet for entries nyere end 1995. Da vi kun ønsker at indlæse records nyere end 1995 ignoreres alle records hvor operationskode ikke er angivet.
     * 
     * Flow for indlæsning:
     * 1) For hver record i filen ->
     * 1.1) Check om operationskode er angivet. Hvis kode = 1 eller 3 forts�ttes.
     * 1.2) Afled oplysninger for hver record i filen.
     * 1.3) Opdater inds�t i historik-tabellen. En opdatering foretages udfra nummer og validFrom.
     * 1.4) Check om denne record er den nyeste i filen med det p� g�ldende nummer. Hvis den er nyeste (indtil videre) -> Gem oplysningerne da de evt. skal opdateres/indsættes i Organisations-tabellen.
     * 2) Listen af nyeste sgh/afd gennemløbes.
     * 2.1) For hvert objekt heri indsættes/opdateres i Organisations-tabellen. Det gælder at hvis der ikke kan opdateres (udfra nummer) laves en insert istedet.  
     * 
     */

	public static Dataset<Organisation> parseOrganisationer(File file) throws FileParseException
	{
		Dataset<Organisation> dataset = new Dataset<Organisation>(Organisation.class);
		
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "ISO8859-15"));
			
			while (reader.ready())
			{
				Organisation org = new OrganisationParser(reader.readLine()).getOrganisation();
				if (org != null) dataset.addEntity(org);
			}
		}
		catch (IOException e)
		{
			String message = "Error parsing SKS afdelinger file: " + file.getAbsolutePath();
			logger.error(message);
			
			throw new FileParseException(message, e);
		}
		
		return dataset;
	}

}


