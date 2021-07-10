CREATE OR REPLACE FUNCTION Storico_Progetti()
RETURNS TRIGGER AS 
$FineProgetto$
DECLARE Progettista integer;
BEGIN
	IF New.Terminato=TRUE THEN
		INSERT INTO ProgRealizzato
		VALUES (Old.CodProgetto, Old.Tipologia);
						 
		FOR Progettista IN (SELECT UserID
							FROM Partecipante
							WHERE CodProgetto=Old.CodProgetto)
							
		LOOP
			INSERT INTO PartecipanteProg
			VALUES (Old.CodProgetto, Progettista);
		END LOOP;
		
		DELETE FROM Progetto
		WHERE CodProgetto=Old.CodProgetto;
	END IF;
RETURN NEW;
END;
$FineProgetto$ LANGUAGE PLPGSQL;
	 
CREATE TRIGGER FineProgetto
AFTER UPDATE ON Progetto
FOR EACH ROW 
EXECUTE PROCEDURE Storico_Progetti();


CREATE OR REPLACE FUNCTION Controllo_Luogo()
RETURNS TRIGGER AS
$LuogoOccupato$
DECLARE n_riunioni integer;
BEGIN
	SELECT COUNT(*) INTO n_riunioni
	FROM Meeting AS M
	WHERE LOWER(Luogo)=LOWER(New.Luogo) AND DataRiunione=New.DataRiunione AND (New.OraInizio>=OraInizio AND	new.OraInizio<=OraFine);
	
	IF (n_riunioni>0) THEN
		RAISE 'Errore, il luogo è già occupato da una riunione';
		RETURN NULL;
	END IF;
	RETURN NEW;
END;
$LuogoOccupato$ LANGUAGE PLPGSQL;
 
CREATE TRIGGER LuogoOccupato
BEFORE INSERT ON Meeting
FOR EACH ROW 
EXECUTE PROCEDURE Controllo_Luogo();


CREATE OR REPLACE FUNCTION Check_Progetto_Mismatch()
RETURNS TRIGGER AS
$ProgettoMismatch$
BEGIN
	IF new.UserID IN(SELECT UserID 	
                    FROM Partecipante AS PA NATURAL JOIN Meeting AS ME
				    WHERE ME.CodMeeting <> new.CodMeeting) THEN
		RAISE 'Il progettista può partecipare solo a meeting su	progetti a cui partecipa';
		RETURN NULL;
		END IF;
RETURN NEW;
END;
$ProgettoMismatch$ LANGUAGE PLPGSQL;
 
CREATE TRIGGER ProgettoMismatch
BEFORE INSERT ON CompMeeting
FOR EACH ROW 
EXECUTE PROCEDURE Check_Progetto_Mismatch();


CREATE OR REPLACE FUNCTION Min_Partecipanti_Meeting()
RETURNS TRIGGER AS 
$ComposizioneMeeting$
DECLARE N_Partecipanti integer;
DECLARE N_Progettisti integer;
 
BEGIN
    IF Old.Iniziato = false THEN
        SELECT COUNT(UserID) INTO N_Partecipanti
        FROM CompMeeting
        WHERE CodMeeting = New.CodMeeting;
 
        SELECT COUNT(UserID) INTO N_Progettisti
        FROM Partecipante
        WHERE CodProgetto = New.CodProgetto;
 
        IF N_Partecipanti < (N_Progettisti::double precision)/3 THEN
        	RAISE 'Non è possibile avviare un meeting su un progetto con un numero di partecipanti minore ad 1/3 del numero	totale di partecipanti al progetto in questione.';
        RETURN NULL;
        END IF;
    END IF;
RETURN NEW;
END;
$ComposizioneMeeting$ LANGUAGE PLPGSQL;
 
CREATE TRIGGER Meeting
BEFORE UPDATE OF Iniziato ON Meeting
FOR EACH ROW 
EXECUTE PROCEDURE Min_Partecipanti_Meeting();


CREATE OR REPLACE FUNCTION Meeting_Senza_PM()
RETURNS TRIGGER AS
$Meeting_Senza_Manager$
DECLARE Project_Manager integer;
BEGIN														
    IF Old.Iniziato = false THEN
 		(SELECT UserID INTO Project_Manager
 	    FROM Partecipante
 		WHERE Ruolo = 'Project Manager' AND CodProgetto = new.CodProgetto);
 
		IF Project_Manager NOT IN (SELECT UserID 										
                                    FROM CompMeeting 										
                                    WHERE CodMeeting = new.CodMeeting) THEN
			RAISE 'Non è possibile avviare un meeting su un progetto senza il relativo project manager.';
 		RETURN NULL;
 		END IF;											
    END IF;
RETURN NEW;
END;
$Meeting_Senza_Manager$ LANGUAGE PLPGSQL;
 
CREATE TRIGGER Meeting_Senza_Manager
BEFORE UPDATE OF Iniziato ON Meeting
FOR EACH ROW
EXECUTE PROCEDURE Meeting_Senza_PM();


CREATE OR REPLACE FUNCTION Check_Meeting_Progetto()
RETURNS TRIGGER AS
$MeetingNonPermesso$
DECLARE Progettista_Libero integer;
BEGIN
	FOR Progettista_Libero IN (SELECT UserID FROM Partecipante 	WHERE CodProgetto IS NULL)
	LOOP
		IF New.UserID = Progettista_Libero THEN
			RAISE 'Impossibile partecipare a questo meeting: il progettista non ha attualmente alcun progetto a carico';
			RETURN NULL;
		END IF;
	END LOOP;
RETURN NEW;
END;
$MeetingNonPermesso$ LANGUAGE PLPGSQL;
 
CREATE TRIGGER MeetingNonPermesso
BEFORE INSERT ON CompMeeting
FOR EACH ROW
EXECUTE PROCEDURE Check_Meeting_Progetto();


CREATE OR REPLACE FUNCTION Check_Valutazione()
RETURNS TRIGGER AS
$ValutazioneAziendale$
BEGIN
	IF Old.Valutazione IS NOT NULL THEN
		RAISE 'Un partecipante ha già una valutazione per questo progetto';
		RETURN NULL;
	END IF;
RETURN NEW;
END;
$ValutazioneAziendale$ LANGUAGE PLPGSQL;
 
CREATE TRIGGER ValutazioneAziendale
BEFORE UPDATE OF Valutazione ON PartecipanteProg
FOR EACH ROW WHEN
EXECUTE PROCEDURE Check_Valutazione();


CREATE OR REPLACE FUNCTION Check_PM()
RETURNS TRIGGER AS
$ProjectManager$
DECLARE npm integer;
BEGIN
	IF UPPER(new.Ruolo) <> 'PROJECT MANAGER' THEN
		RETURN NEW;
	ELSE 
		SELECT COUNT(*) INTO npm									
        FROM Partecipante
    	WHERE UPPER(ruolo) = 'PROJECT MANAGER' AND CodProgetto=New.CodProgetto;
	
		IF (npm > 0) THEN
        		RAISE 'Errore, un progetto non può avere più di un project manager';
			RETURN NULL;
    	END IF;
	RETURN NEW;
	END IF;
END;
$ProjectManager$ LANGUAGE PLPGSQL;
 
CREATE TRIGGER ProjectManager
BEFORE INSERT ON Partecipante
FOR EACH ROW 
EXECUTE PROCEDURE Check_PM();


CREATE OR REPLACE FUNCTION Un_Meeting_Alla_Volta()
RETURNS TRIGGER AS
$OneMeeting$
BEGIN
	IF New.CodProgetto IN(SELECT CodProgetto
					    FROM Meeting AS M
					    WHERE OraFine IS NULL) THEN
		RAISE 'Un meeting per questo progetto è già calendarizzato';
		RETURN NULL;
	END IF;
RETURN NEW;
END
$OneMeeting$ LANGUAGE PLPGSQL;
 
CREATE TRIGGER OneMeeting
BEFORE INSERT ON Meeting
FOR EACH ROW 
EXECUTE PROCEDURE Un_Meeting_Alla_Volta();


CREATE FUNCTION AmbitoProgetto()
RETURNS TRIGGER AS
$ProjectTopic$
BEGIN
	INSERT INTO ProgAmbito (codprogetto)
	VALUES (New.CodProgetto);
RETURN NEW;
END;
$ProjectTopic$ LANGUAGE PLPGSQL;
 
CREATE TRIGGER ProjectTopic
AFTER INSERT ON Progetto
FOR EACH ROW 
EXECUTE PROCEDURE AmbitoProgetto();


CREATE OR REPLACE FUNCTION InserimentoMeetingAmbito()
RETURNS TRIGGER AS
$AmbitoProgettoMeeting$
BEGIN
	IF New.CodProgetto IN(SELECT CodProgetto
					FROM ProgAmbito AS M
					WHERE Nome IS NULL) THEN
		RAISE 'Per il progetto in questione non è stato inserito alcun ambito di appartenenza. Per programmare un meeting, inserisci almeno un ambito';
		RETURN NULL;
	END IF;
RETURN NEW;
END
$AmbitoProgettoMeeting$ LANGUAGE PLPGSQL;
 
CREATE TRIGGER AmbitoProgettoMeeting
BEFORE INSERT ON Meeting
FOR EACH ROW 
EXECUTE PROCEDURE InserimentoMeetingAmbito();


CREATE OR REPLACE FUNCTION InserimentoPartecipanteAmbito()
RETURNS TRIGGER AS
$AmbitoProgettoPartecipante$
BEGIN
	IF New.CodProgetto IN(SELECT CodProgetto
					    FROM ProgAmbito AS M
					    WHERE Nome IS NULL) THEN
		RAISE 'Per il progetto in questione non è stato inserito alcun ambito di appartenenza. Per programmare un meeting, inserisci almeno un ambito';
		RETURN NULL;
	END IF;
RETURN NEW;
END
$AmbitoProgettoPartecipante$ LANGUAGE PLPGSQL;
 
CREATE TRIGGER AmbitoProgettoPartecipante
BEFORE INSERT ON Partecipante
FOR EACH ROW 
EXECUTE PROCEDURE InserimentoPartecipanteAmbito();


CREATE FUNCTION IncrementaPartecipanti()
RETURNS TRIGGER AS
$NuovoPartecipante$
BEGIN
	UPDATE Progetto
	SET NumeroPartecipanti=NumeroPartecipanti+1
	WHERE CodProgetto=New.CodProgetto;
RETURN NEW;
END;
$NuovoPartecipante$ LANGUAGE PLPGSQL;
 
CREATE TRIGGER NuovoPartecipante
BEFORE UPDATE OF CodProgetto ON Partecipante
FOR EACH ROW 
EXECUTE PROCEDURE IncrementaPartecipanti();