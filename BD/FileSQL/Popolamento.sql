INSERT INTO Azienda
VALUES
('12345670333', 'Basi Di Dati', 'Napoli'),
('12345670222', 'PostgreSQL', 'Roma'),
('12345670444', 'pgAdmin4', 'Via Claudio');


INSERT INTO Privato
VALUES
('PLLCMN00S17H860L', 'Carmine', 'Paolella');


INSERT INTO Società
VALUES
('12345670555', 'SpaceX');


INSERT INTO Progetto (Tipologia, Budget, PartIVA, CF)
VALUES
('Ricerca sperimentale', 10000, '12345670333', 'PLLCMN00S17H860L');
INSERT INTO Progetto (Tipologia, Budget, PartIVA, PartitaIVA)
VALUES
('Ricerca industriale', 15000, '12345670333', '12345670555');


INSERT INTO Ambito
VALUES
('Elettronica'), ('Medicina'), ('Economia e finanza'), ('Informatica');


UPDATE ProgAmbito
SET Nome = 'Informatica'
WHERE CodProgetto = 19;
UPDATE ProgAmbito
SET Nome = 'Elettronica'
WHERE CodProgetto = 20;


INSERT INTO Partecipante (CF, Nome, Cognome, Ruolo, SalarioMedio,
PartIVA, CodProgetto)
VALUES
('CHHBCG00E47F799U', 'Bianca Giada', 'Chehade', 'Project Manager', 3500,
'12345670333', 19),
('LGRMRA00E06B963U', 'Mario', 'Liguori', 'Responsabile Area Informatica',
2500, '12345670333', 19),
('SMNCRS00E16F839Q', 'Christian', 'Simeone', 'Coordinatore Di Attività',
2500, '12345670333', 19),
('SLVNZE01C56F839S', 'Enza', 'Silvis', 'Coordinatore Di Attività', 2400,
'12345670333', 19),
('ZZAFNC97R05F839R', 'Francesco', 'Zaza', 'Project Manager', 2000,
'12345670222', 20),
('VRRLSS00T45F799E', 'Alessia', 'Verrazzo', 'Responsabile Della
Comunicazione', 2100, '12345670222', 20),
('RLNPQL00D27L259K', 'Pasquale', 'Orlando', 'Responsabile Area
Informatica', 2500, '12345670222', 20);


INSERT INTO Meeting (DataRiunione, OraInizio, OraFine, Piattaforma,
CodProgetto)
VALUES
('01-08-2021', '9:00', '11:00', 'Skype', 19);
INSERT INTO Meeting (DataRiunione, OraInizio, OraFine, Luogo,
CodProgetto)
VALUES
('04-08-2021', '16:30', '17:30', 'Sala 1', 20);


INSERT INTO CompMeeting
VALUES
(42, 17), (42, 18), (42, 19), (42, 20), (43, 21), (43, 22), (43, 23);