CREATE TABLE sdm_housekeeping.Import (
 importtime DATETIME,
 spoolername VARCHAR(100)
) ENGINE=InnoDB
;

CREATE TABLE sdm_housekeeping.AdresseBeskyttelse (
 CPR VARCHAR(10) NOT NULL,
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
 NavneBeskyttelseStartDato DATETIME,
 NavneBeskyttelseSletteDato DATETIME,
 VejKode BIGINT(12), 
 KommuneKode BIGINT(12),
 UNIQUE INDEX (CPR)
) ENGINE=InnoDB
;
