CREATE DOMAIN enum_ruolo AS character varying(50)
CONSTRAINT enum_ruolo CHECK
(upper(VALUE) = ANY ('RESPONSABILE DELLA COMUNICAZIONE', 'COORDINATORE DI
ATTIVITÀ', 'RESPONSABILE AMMINISTRAZIONE', 'RESPONSABILE AREA
INFORMATICA', 'PROJECT MANAGER'));

CREATE DOMAIN enum_tipologia AS character varying(50)
CONSTRAINT enum_tipologia CHECK
(upper(VALUE) = ANY ('RICERCA DI BASE', 'RICERCA INDUSTRIALE', 'RICERCA
SPERIMENTALE', 'SVILUPPO SPERIMENTALE'));


--TABELLA
CREATE TABLE Azienda (
    PartIVA varchar(11) PRIMARY KEY,
    Nome varchar(20) NOT NULL,
    SedePrincipale varchar(12) NOT NULL
);

--VINCOLI
ALTER TABLE
    Azienda
ADD
    CONSTRAINT CheckPartIVA CHECK (((PartIVA) :: text ~* '[0-9]{7}0[0-9]{3}' :: text));

--TABELLA
CREATE TABLE Privato (
    CF varchar(16) PRIMARY KEY,
    Nome varchar(20) NOT NULL,
    Cognome varchar(20) NOT NULL,
);

--VINCOLI
ALTER TABLE
    Privato
ADD
    CONSTRAINT Controllo_CF CHECK (CF ~* '[A-Z]{6}\d{2}[A-Z]\d{2}[A-Z]\d{3}[A-Z]');

--TABELLA
CREATE TABLE Società (
    PartitaIVA varchar(11) PRIMARY KEY,
    NomeSocietà varchar(25) NOT NULL
);

--VINCOLI
ALTER TABLE
    Società
ADD
    CONSTRAINT CheckPartitaIVA CHECK (
        (
            (PartitaIVA) :: text ~* '[0-9]{7}0[0-9]{3}' :: text
        )
    );

--TABELLA
CREATE TABLE Progetto (
    CodProgetto integer PRIMARY KEY,
    Tipologia enum_tipologia NOT NULL,
    NumeroPartecipanti integer DEFAULT 0 NOT NULL,
    Budget decimal NOT NULL,
    PartIVA varchar(11) NOT NULL REFERENCES Azienda(PartIVA),
    CF varchar(16) REFERENCES Privato(CF),
    PartitaIVA varchar(11) REFERENCES Società(PartitaIVA),
    Terminato boolean DEFAULT false NOT NULL
);

--VINCOLI
ALTER TABLE
    Progetto
ADD
    CONSTRAINT Vincolo_Cliente CHECK (
        (
            CF IS NOT NULL
            AND PartitaIva IS NULL
        )
        OR (
            CF IS NULL
            AND PartitaIva IS NOT NULL
        )
    ),
ADD
    CONSTRAINT NumeroPartecipanti_Check CHECK (
        numeropartecipanti >= 0
        AND numeropartecipanti <= 30
    ),
ADD
    CONSTRAINT BudgetLegale CHECK (Budget >= 5000),
ADD
    CONSTRAINT Commissione CHECK (PartIVA <> PartitaIVA);

--TABELLA
CREATE TABLE Partecipante (
    UserID integer PRIMARY KEY,
    Pw varchar(30),
    CF varchar(16) NOT NULL,
    Nome varchar(20) NOT NULL,
    Cognome varchar(20) NOT NULL,
    Ruolo enum_ruolo NOT NULL,
    SalarioMedio decimal NOT NULL,
    CodProgetto integer NOT NULL REFERENCES Progetto(CodProgetto),
    PartIVA varchar(11) NOT NULL REFERENCES Azienda(PartIVA)
);

--VINCOLI
ALTER TABLE
    Partecipante
ADD
    CONSTRAINT Controllo_CF CHECK (CF ~* '[A-Z]{6}\d{2}[A-Z]\d{2}[A-Z]\d{3}[A-Z]'),
ADD
    CONSTRAINT Controllo_Valutazione CHECK (
        (Valutazione >= 0)
        AND (Valutazione <= 5)
    ),
ADD
    CONSTRAINT Check_Password CHECK (
        pw ~* '(?=^.{6,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$'
    ),
ADD
    CONSTRAINT Unico_CF UNIQUE CF,
ADD
    CONSTRAINT StipendioLegale CHECK (SalarioMedio >= 1500);

--TABELLA
CREATE TABLE ProgRealizzato (
    CodProg integer PRIMARY KEY,
    Tipologia enum_tipologia NOT NULL
);

--TABELLA
CREATE TABLE PartecipanteProg (
    Valutazione integer,
    UserID integer NOT NULL REFERENCES Partecipante(UserID),
    CodProg integer NOT NULL REFERENCES ProgRealizzato(CodProg)
);

--TABELLA
CREATE TABLE Meeting(
    CodMeeting integer PRIMARY KEY,
    DataRiunione date NOT NULL,
    OraInizio time NOT NULL,
    OraFine time NOT NULL,
    Piattaforma varchar(10),
    Luogo varchar(10),
    CodProgetto integer NOT NULL REFERENCES Progetto(CodProgetto),
    Iniziato boolean DEFAULT false NOT NULL,
    Finito boolean DEFAULT false NOT NULL
);

--VINCOLI
ALTER TABLE
    Meeting
ADD
    CONSTRAINT Luogo_Riunione CHECK (
        (
            Luogo IS NOT NULL
            AND Piattaforma IS NULL
        )
        OR (
            Luogo IS NULL
            AND Piattaforma IS NOT NULL
        ),
        ADD
            CONSTRAINT ValiditàOrarioMeeting CHECK ((orafine - orainizio) > INTERVAL '30 minutes'),
        ADD
            CONSTRAINT ValiditàDataMeeting CHECK (datariunione >= current_date),
        ADD
            CONSTRAINT Meeting_Futuri CHECK ((datariunione - current_date) >= 14);

--TABELLA
CREATE TABLE CompMeeting (
    CodMeeting integer NOT NULL REFERENCES Meeting(CodMeeting),
    UserID integer NOT NULL REFERENCES Partecipante(UserID)
);

--VINCOLI
ALTER TABLE
    CompMeeting
ADD
    CONSTRAINT Unique_Partecipante_Meeting UNIQUE (CodMeeting, UserID);

--TABELLA
CREATE TABLE Ambito (Nome varchar(25) PRIMARY KEY);

--TABELLA
CREATE TABLE ProgAmbito (
    CodProgetto integer NOT NULL REFERENCES Progetto(CodProgetto),
    Nome varchar(25) REFERENCES Ambito(Nome)
);
