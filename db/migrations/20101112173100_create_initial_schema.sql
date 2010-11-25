CREATE TABLE Administrationsvej
(
	AdministrationsvejPID BIGINT(15) NOT NULL AUTO_INCREMENT,
	
	AdministrationsvejKode CHAR(2) NOT NULL,
	AdministrationsvejTekst VARCHAR(50) NOT NULL,
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200),
	CreatedDate DATETIME,
	
	INDEX (ValidFrom, ValidTo),

	PRIMARY KEY (AdministrationsvejPID)
	
) ENGINE=InnoDB
;



CREATE TABLE Apotek
(
	ApotekPID BIGINT(15) NOT NULL AUTO_INCREMENT,
	
	SorNummer BIGINT(20) NOT NULL,
	
	ApotekNummer BIGINT(15),
	FilialNummer BIGINT(15),
	
	EanLokationsnummer BIGINT(20),
	
	cvr BIGINT(15),
	pcvr BIGINT(15),
	
	Navn VARCHAR(256),
	
	Telefon VARCHAR(20),
	Vejnavn VARCHAR(100),
	
	Postnummer VARCHAR(10),
	Bynavn VARCHAR(30),
	
	Email VARCHAR(100),
	Www VARCHAR(100),
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	PRIMARY KEY (ApotekPID),
	
	INDEX (ValidFrom, ValidTo)

) ENGINE=InnoDB
;



CREATE TABLE ATC
(
	ATCPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	ATC VARCHAR(10) NOT NULL,
	ATCTekst VARCHAR(72) NOT NULL,
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo)

) ENGINE=InnoDB
;



CREATE TABLE Autorisation
(
	AutorisationPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	Autorisationsnummer VARCHAR(10) NOT NULL,
	
	cpr VARCHAR(10) NOT NULL,
	
	Fornavn VARCHAR(100) NOT NULL,
	Efternavn VARCHAR(100) NOT NULL,
	
	UddannelsesKode INT(4),
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo)

) ENGINE=InnoDB
;



CREATE TABLE BarnRelation (
	BarnRelationPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	Id VARCHAR(21) NOT NULL,
	
	CPR VARCHAR(10) NOT NULL,
	BarnCPR VARCHAR(10) NOT NULL,
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo),
	
	CONSTRAINT UC_Person_1 UNIQUE (Id, ValidFrom)
) ENGINE=InnoDB
;



CREATE TABLE Dosering (
	DoseringPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	DoseringKode BIGINT(12) NOT NULL,
	DoseringTekst VARCHAR(100) NOT NULL,
	
	AntalEnhederPrDoegn FLOAT(10) NOT NULL,
	Aktiv BOOLEAN,
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo)
) ENGINE=InnoDB
;



CREATE TABLE ForaeldreMyndighedRelation (
	ForaeldreMyndighedRelationPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	Id VARCHAR(21) NOT NULL,
	
	CPR CHAR(10) NOT NULL,
	
	TypeKode VARCHAR(4) NOT NULL,
	TypeTekst VARCHAR(50) NOT NULL,
	
	RelationCpr CHAR(10),
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo),
	CONSTRAINT UC_Person_1 UNIQUE (Id, ValidFrom)
) ENGINE=InnoDB
;



CREATE TABLE Formbetegnelse
(
	FormbetegnelsePID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	Kode VARCHAR(10) NOT NULL,
	
	Tekst VARCHAR(150) NOT NULL,
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo)
) ENGINE=InnoDB
;



CREATE TABLE Indikation
(
	IndikationPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	IndikationKode BIGINT(15),
	IndikationTekst VARCHAR(100),
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo)
)
ENGINE=InnoDB
;



CREATE TABLE IndikationATCRef
(
	IndikationATCRefPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	CID VARCHAR(22) NOT NULL,
	
	IndikationKode BIGINT(15) NOT NULL,
	ATC VARCHAR(10) NOT NULL,
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo, IndikationKode, ATC)
) ENGINE=InnoDB
;



CREATE TABLE Kommune
(
	KommunePID BIGINT(15) NOT NULL PRIMARY KEY,
	
	Nummer VARCHAR(12) NOT NULL,
	
	Navn VARCHAR(100) NOT NULL,
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo)
) ENGINE=InnoDB
;



CREATE TABLE Laegemiddel
(
	LaegemiddelPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	DrugID BIGINT(12) NOT NULL,
	DrugName VARCHAR(30) NOT NULL,
	
	FormKode VARCHAR(10),
	FormTekst VARCHAR(150),
	
	ATCKode VARCHAR(10),
	ATCTekst VARCHAR(100),
	
	StyrkeNumerisk DECIMAL(10,3),
	StyrkeEnhed VARCHAR(100),
	StyrkeTekst VARCHAR(30),
	
	Dosisdispenserbar BOOLEAN,
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo)
) ENGINE=InnoDB
;


CREATE TABLE LaegemiddelAdministrationsvejRef
(
	LaegemiddelAdministrationsvejRefPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	CID VARCHAR(22) NOT NULL,
	DrugID BIGINT(12) NOT NULL,
	
	AdministrationsvejKode CHAR(2) NOT NULL,
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo)
) ENGINE=InnoDB
;


CREATE TABLE LaegemiddelDoseringRef (
	LaegemiddelDoseringRefPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	CID VARCHAR(22) NOT NULL,
	
	DrugID BIGINT(12) NOT NULL,
	DoseringKode BIGINT(12) NOT NULL,
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo)
) ENGINE=InnoDB
;



CREATE TABLE Klausulering
(
	KlausuleringPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	Kode VARCHAR(10) NOT NULL,
	KortTekst  VARCHAR(60),
	
	Tekst VARCHAR(600),
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo)
) ENGINE=InnoDB
;



CREATE TABLE Medicintilskud
(
	MedicintilskudPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	Kode VARCHAR(10) NOT NULL,
	KortTekst VARCHAR(20),
	
	Tekst VARCHAR(60),
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo)
) ENGINE=InnoDB
;



CREATE TABLE Organisation (
	organisationPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	Nummer VARCHAR(30) NOT NULL,
	Navn VARCHAR(256),
	
	Organisationstype VARCHAR(30) NOT NULL,
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo)
) ENGINE=InnoDB
;



CREATE TABLE Pakning
(
	PakningPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	Varenummer BIGINT(12) NOT NULL,
	VarenummerDelpakning BIGINT(12),
	
	DrugID DECIMAL(12) NOT NULL,
	
	PakningsstoerrelseNumerisk DECIMAL(10,2),
	Pakningsstoerrelsesenhed VARCHAR(10),
	PakningsstoerrelseTekst VARCHAR(30),
	
	EmballageTypeKode VARCHAR(10),
	Dosisdispenserbar BOOL,
	MedicintilskudsKode VARCHAR(10),
	KlausuleringsKode VARCHAR(10),
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	ValidFrom DATETIME,
	ValidTo DATETIME,
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	INDEX (ValidFrom, ValidTo)
) ENGINE=InnoDB
;


CREATE TABLE Pakningsstoerrelsesenhed (
	PakningsstoerrelsesenhedPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	PakningsstoerrelsesenhedKode VARCHAR(10) NOT NULL,
	PakningsstoerrelsesenhedTekst VARCHAR(50) NOT NULL,
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	ValidFrom DATETIME,
	ValidTo DATETIME,
	CreatedBy VARCHAR(200),
	CreatedDate DATETIME,
	INDEX (ValidFrom, ValidTo),
	CONSTRAINT UC_Pakningsstoerrelsesenhed_1 UNIQUE (PakningsstoerrelsesEnhedKode, ValidTo)
) ENGINE=InnoDB
;


CREATE TABLE Person (
	PersonPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	CPR VARCHAR(10) NOT NULL,
	
	Koen VARCHAR(1) NOT NULL,
	
	Fornavn VARCHAR(60),
	Mellemnavn VARCHAR(60),
	Efternavn VARCHAR(60),
	
	CoNavn VARCHAR(50),
	
	Lokalitet VARCHAR(50),
	
	Vejnavn VARCHAR(30),
	
	Bygningsnummer VARCHAR(10),
	Husnummer VARCHAR(10),
	
	Etage VARCHAR(10),
	
	SideDoerNummer VARCHAR(10),
	
	Bynavn VARCHAR(30),
	Postnummer BIGINT(12),
	PostDistrikt VARCHAR(30),
	
	Status VARCHAR(2),
	
	NavneBeskyttelseStartDato DATETIME,
	NavneBeskyttelseSletteDato DATETIME,
	
	GaeldendeCPR VARCHAR(10),
	
	Foedselsdato DATETIME NOT NULL,
	
	Stilling VARCHAR(50),
	VejKode BIGINT(12), 
	KommuneKode BIGINT(12),
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo),
	
	CONSTRAINT UC_Person_1 UNIQUE (CPR, ValidFrom)
) ENGINE=InnoDB
;


CREATE TABLE PersonIkraft
(
	PersonIkraftPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY	,
	IkraftDato DATETIME NOT NULL
) ENGINE=InnoDB
;


CREATE TABLE Praksis
(
	praksisPID BIGINT(20) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	SorNummer BIGINT(20) NOT NULL,
	
	EanLokationsnummer BIGINT(20),
	RegionCode BIGINT(12),
	
	Navn VARCHAR(256),
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo)
) ENGINE=InnoDB
;


CREATE TABLE Styrkeenhed
(
	StyrkeenhedPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	StyrkeenhedKode VARCHAR(10) NOT NULL,
	StyrkeenhedTekst VARCHAR(50) NOT NULL,
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200),
	CreatedDate DATETIME,
	
	INDEX (ValidFrom, ValidTo),
	
	CONSTRAINT UC_Styrkeenhed_1 UNIQUE (StyrkeenhedKode, ValidTo)
) ENGINE=InnoDB
;


CREATE TABLE Sygehus
(
	SygeHusPID BIGINT(20) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	SorNummer BIGINT(20) NOT NULL,
	
	EanLokationsnummer BIGINT(20),
	
	Nummer VARCHAR(30),
	Telefon VARCHAR(20),
	
	Navn VARCHAR(256),
	Vejnavn VARCHAR(100),
	Postnummer VARCHAR(10),
	Bynavn VARCHAR(30),
	
	Email VARCHAR(100),
	Www VARCHAR(100),
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo)
) ENGINE=InnoDB
;


CREATE TABLE SygehusAfdeling
(
	SygeHusAfdelingPID BIGINT(20) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	SorNummer BIGINT(20) NOT NULL,
	EanLokationsnummer BIGINT(20),
	Nummer VARCHAR(30),
	
	Navn VARCHAR(256),
	
	SygehusSorNummer BIGINT(20),
	OverAfdelingSorNummer BIGINT(20),
	UnderlagtSygehusSorNummer BIGINT(20),
	
	AfdelingTypeKode BIGINT(20),
	AfdelingTypeTekst VARCHAR(50),
	
	HovedSpecialeKode VARCHAR(20),
	HovedSpecialeTekst VARCHAR(40),
	
	Telefon VARCHAR(20),
	Vejnavn VARCHAR(100),
	Postnummer VARCHAR(10),
	Bynavn VARCHAR(30),
	
	Email VARCHAR(100),
	Www VARCHAR(100),
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo)
) ENGINE=InnoDB
;


CREATE TABLE TakstVersion
(
	TakstVersionPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	TakstUge VARCHAR(8) NOT NULL,
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo)
) ENGINE=InnoDB
;


CREATE TABLE Tidsenhed
(
	TidsenhedPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	TidsenhedKode VARCHAR(10) NOT NULL,
	TidsenhedTekst VARCHAR(50) NOT NULL,
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200),
	CreatedDate DATETIME,
	
	INDEX (ValidFrom, ValidTo),
	
	CONSTRAINT UC_Tidsenhed_1 UNIQUE (TidsenhedKode, ValidTo)
) ENGINE=InnoDB
;


CREATE TABLE UmyndiggoerelseVaergeRelation
(
	ForaeldreMyndighedRelationPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	Id VARCHAR(21) NOT NULL,
	CPR VARCHAR(10) NOT NULL,
	
	TypeKode VARCHAR(4) NOT NULL,
	TypeTekst VARCHAR(50) NOT NULL,
	
	RelationCpr VARCHAR(10),
	RelationCprStartDato DATETIME,
	
	VaergesNavn VARCHAR(50),
	VaergesNavnStartDato DATETIME,
	
	RelationsTekst1 VARCHAR(50),
	RelationsTekst2 VARCHAR(50),
	RelationsTekst3 VARCHAR(50),
	RelationsTekst4 VARCHAR(50),
	RelationsTekst5 VARCHAR(50),
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo),
	
	CONSTRAINT UC_Person_1 UNIQUE (Id, ValidFrom)
) ENGINE=InnoDB
;


CREATE TABLE Yder
(
	YderPID BIGINT(20) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	Nummer VARCHAR(30),
	SorNummer BIGINT(20) NOT NULL,
	
	PraksisSorNummer BIGINT(20) NOT NULL,
	EanLokationsnummer BIGINT(20),
	
	Telefon VARCHAR(20),
	Navn VARCHAR(256),
	Vejnavn VARCHAR(100),
	Postnummer VARCHAR(10),
	Bynavn VARCHAR(30),
	
	Email VARCHAR(100),
	Www VARCHAR(100),
	
	HovedSpecialeKode VARCHAR(20),
	HovedSpecialeTekst VARCHAR(40),
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo)
) ENGINE=InnoDB
;


CREATE TABLE YderLoebenummer (
	YderLoebenummerPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY	,
	Loebenummer BIGINT(12) NOT NULL
) ENGINE=InnoDB
;


CREATE TABLE Yderregister (
	YderregisterPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,
	
	Nummer VARCHAR(30) NOT NULL,
	
	Telefon VARCHAR(10),
	
	Navn VARCHAR(256),
	
	Vejnavn VARCHAR(100),
	
	Postnummer VARCHAR(10),
	Bynavn VARCHAR(30),
	AmtNummer BIGINT(12),
	
	Email VARCHAR(100),
	Www VARCHAR(100),
	
	HovedSpecialeKode VARCHAR(100),
	HovedSpecialeTekst VARCHAR(100),
	
	HistID VARCHAR(100),
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo)
) ENGINE=InnoDB
;


CREATE TABLE YderregisterPerson
(
	YderregisterPersonPID BIGINT(15) AUTO_INCREMENT NOT NULL PRIMARY KEY,

	Id VARCHAR(20) NOT NULL,
	
	Nummer VARCHAR(30) NOT NULL,
	
	CPR VARCHAR(10),
	
	personrolleKode BIGINT(20),
	personrolleTxt VARCHAR(200),
	
	HistIDPerson VARCHAR(100),
	
	ModifiedBy VARCHAR(200) NOT NULL,
	ModifiedDate DATETIME NOT NULL,
	
	ValidFrom DATETIME,
	ValidTo DATETIME,
	
	CreatedBy VARCHAR(200) NOT NULL,
	CreatedDate DATETIME NOT NULL,
	
	INDEX (ValidFrom, ValidTo)
) ENGINE=InnoDB
;
