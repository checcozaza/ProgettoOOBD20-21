CREATE VIEW PartecipantiLiberi (UserID) AS
(SELECT UserID
FROM Partecipante
WHERE CodProgetto IS NULL);


CREATE VIEW ValutazioneMedia (UserID, ValutazioneMedia) AS
(SELECT UserID, AVG(Valutazione)
FROM PartecipanteProg
GROUP BY UserID);


CREATE VIEW MeetingImminenti (CodMeeting, CodProgetto) AS
(SELECT CodMeeting, Codprogetto
FROM Meeting AS M
WHERE ((datariunione-current_date) <= 7));


CREATE VIEW NumProgetti (UserID, NumeroProgettiRealizzati) AS
(SELECT UserID, COUNT(CodProg)
FROM PartecipanteProg
GROUP BY UserID);


CREATE VIEW TipologieProgetti (Tipologia, NumeroProgettiAssociati) AS
(SELECT Tipologia, COUNT(CodProgetto)
FROM Progetto
GROUP BY Tipologia);