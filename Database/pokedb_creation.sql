CREATE TABLE IF NOT EXISTS Pokemon (
	ID INT(3) PRIMARY KEY,
	Nome VARCHAR(15),
	Descrizione (500)
);

CREATE TABLE IF NOT EXISTS Tipo (
	Tipo VARCHAR(10) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS Fortezza (
	Fortezza VARCHAR(10) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS Debolezza (
	Debolezza VARCHAR(10) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS Utente (
	Email VARCHAR(50) PRIMARY KEY,
	Nome VARCHAR(20),
	Cognome VARCHAR(20),
	Username VARCHAR(20) UNIQUE,
	Password VARCHAR (16),
	Indirizzo VARCHAR(50),
	CAP INT(5),
	Citta VARCHAR(50),
	Provincia VARCHAR(2)
);

CREATE TABLE IF NOT EXISTS Smartkedex (
	Nome VARCHAR(20),
	Email VARCHAR(50),
	PRIMARY KEY (Nome, Email),
	FOREIGN KEY (Email) REFERENCES Utente (Email)
);

CREATE TABLE IF NOT EXISTS TipiPokemon (
	Nome VARCHAR(15),
	Tipo VARCHAR(10),
	PRIMARY KEY (Nome, Tipo),
	FOREIGN KEY (Nome) REFERENCES Pokemon (Nome),
	FOREIGN KEY (Tipo) REFERENCES Tipo (Tipo)
);

CREATE TABLE IF NOT EXISTS FortezzePokemon (
	Nome VARCHAR(15),
	Fortezza VARCHAR(10),
	PRIMARY KEY (Nome, Fortezza),
	FOREIGN KEY (Nome) REFERENCES Pokemon (Nome),
	FOREIGN KEY (Fortezza) REFERENCES FortezzePokemon (Fortezza)
);

CREATE TABLE IF NOT EXISTS DebolezzePokemon (
	Nome VARCHAR(15),
	Debolezza VARCHAR(10),
	PRIMARY KEY (Nome, Debolezza),
	FOREIGN KEY (Nome) REFERENCES Pokemon (Nome),
	FOREIGN KEY (Debolezza) REFERENCES DebolezzePokemon (Debolezza)
);

