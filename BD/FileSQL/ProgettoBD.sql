--
-- PostgreSQL database dump
--

-- Dumped from database version 13.1
-- Dumped by pg_dump version 13.3

-- Started on 2021-07-10 17:18:17

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3164 (class 1262 OID 32768)
-- Name: Progetto_BD; Type: DATABASE; Schema: -; Owner: -
--

CREATE DATABASE "Progetto_BD" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Italian_Italy.1252';


\connect "Progetto_BD"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 667 (class 1247 OID 40982)
-- Name: enum_ruolo; Type: DOMAIN; Schema: public; Owner: -
--

CREATE DOMAIN public.enum_ruolo AS character varying(50)
	CONSTRAINT enum_ruolo CHECK ((upper((VALUE)::text) = ANY (ARRAY['RESPONSABILE DELLA COMUNICAZIONE'::text, 'COORDINATORE DI ATTIVITÀ'::text, 'RESPONSABILE AMMINISTRAZIONE'::text, 'RESPONSABILE AREA INFORMATICA'::text, 'PROJECT MANAGER'::text])));


--
-- TOC entry 663 (class 1247 OID 40976)
-- Name: enum_tipologia; Type: DOMAIN; Schema: public; Owner: -
--

CREATE DOMAIN public.enum_tipologia AS character varying(50)
	CONSTRAINT enum_tipologia CHECK ((upper((VALUE)::text) = ANY (ARRAY['RICERCA DI BASE'::text, 'RICERCA INDUSTRIALE'::text, 'RICERCA SPERIMENTALE'::text, 'SVILUPPO SPERIMENTALE'::text])));


--
-- TOC entry 228 (class 1255 OID 139555)
-- Name: ambitoprogetto(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.ambitoprogetto() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	INSERT INTO ProgAmbito (codprogetto)
	VALUES (New.CodProgetto);
RETURN NEW;
END;
$$;


--
-- TOC entry 227 (class 1255 OID 82216)
-- Name: check_meeting_progetto(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_meeting_progetto() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE progettista_libero integer;

BEGIN
	FOR progettista_libero IN (SELECT UserID FROM Partecipante WHERE CodProgetto IS NULL)
	LOOP
		IF new.UserID = progettista_libero THEN
			RAISE 'Impossibile partecipare a questo meeting: il progettista non ha attualmente alcun progetto a carico';
			RETURN NULL;
		END IF;
	END LOOP;
	RETURN NEW;
END; 
$$;


--
-- TOC entry 221 (class 1255 OID 73831)
-- Name: check_pm(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_pm() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE npm integer;
BEGIN
	IF UPPER(new.ruolo) <> 'PROJECT MANAGER' THEN
		return NEW;
	ELSE 
	    SELECT count(*) INTO npm
    	FROM partecipante
    	WHERE UPPER(ruolo) = 'PROJECT MANAGER'
		AND codprogetto=new.codprogetto;
	
		if (npm > 0) then
        	raise 'Errore, un progetto non può avere più di un project manager';
			return NULL;
    	end if;
		return new;
	END IF;
END;
$$;


--
-- TOC entry 222 (class 1255 OID 90252)
-- Name: check_progetto_mismatch(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_progetto_mismatch() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
		IF new.UserID IN (SELECT UserID 
						FROM Partecipante AS PA NATURAL JOIN Meeting AS ME
						WHERE ME.CodMeeting <> new.CodMeeting) THEN
			RAISE 'Il progettista può partecipare solo a meeting su progetti a cui partecipa';
			RETURN NULL;
		END IF;
		RETURN NEW;
END;
$$;


--
-- TOC entry 225 (class 1255 OID 139542)
-- Name: check_valutazione(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_valutazione() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	IF old.Valutazione IS NOT NULL THEN
		RAISE 'Un partecipante ha già una valutazione per questo progetto';
		RETURN NULL;
	END IF;
	RETURN NEW;
END;
$$;


--
-- TOC entry 230 (class 1255 OID 74041)
-- Name: controllo_luogo(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.controllo_luogo() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE n_riunioni integer;

BEGIN
	SELECT COUNT(*) INTO n_riunioni
	FROM MEETING AS M
	WHERE LOWER(luogo)=LOWER(new.luogo) AND DataRiunione=new.DataRiunione AND
	(new.OraInizio >= OraInizio AND new.OraInizio <= OraFine);
	
	IF (n_riunioni > 0) THEN
		RAISE 'Errore, il luogo è già occupato da una riunione';
		RETURN NULL;
	END IF;
	RETURN NEW;
END;
$$;


--
-- TOC entry 229 (class 1255 OID 139602)
-- Name: incrementapartecipanti(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.incrementapartecipanti() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	UPDATE Progetto
	SET numeropartecipanti=numeropartecipanti+1
	WHERE CodProgetto=new.CodProgetto;
RETURN NEW;
END;
$$;


--
-- TOC entry 244 (class 1255 OID 139583)
-- Name: inserimentomeetingambito(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.inserimentomeetingambito() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	IF New.CodProgetto IN(SELECT CodProgetto
							FROM ProgAmbito AS M
							WHERE Nome IS NULL) THEN
		RAISE 'Per il progetto in questione non è stato inserito alcun ambito di appartenenza. Per programmare un meeting, inserisci almeno un ambito';
		RETURN NULL;
	END IF;
RETURN NEW;
END
$$;


--
-- TOC entry 226 (class 1255 OID 139581)
-- Name: inserimentomeetingtipologia(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.inserimentomeetingtipologia() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	IF New.CodProgetto IN(SELECT CodProgetto
							FROM ProgAmbito AS M
							WHERE Nome IS NULL) THEN
		RAISE 'Per il progetto in questione non è stato inserito alcun ambito di appartenenza. Per programmare un meeting, inserisci almeno un ambito';
		RETURN NULL;
	END IF;
RETURN NEW;
END
$$;


--
-- TOC entry 245 (class 1255 OID 139586)
-- Name: inserimentopartecipanteambito(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.inserimentopartecipanteambito() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	IF New.CodProgetto IN(SELECT CodProgetto
							FROM ProgAmbito AS M
							WHERE Nome IS NULL) THEN
		RAISE 'Per il progetto in questione non è stato inserito alcun ambito di appartenenza. Per programmare un meeting, inserisci almeno un ambito';
		RETURN NULL;
	END IF;
RETURN NEW;
END
$$;


--
-- TOC entry 224 (class 1255 OID 98440)
-- Name: meeting_senza_pm(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.meeting_senza_pm() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE Project_Manager integer;

BEGIN
    IF old.iniziato = false THEN
        SELECT UserID INTO Project_Manager
		FROM Partecipante
		WHERE Ruolo = 'Project Manager' AND CodProgetto = new.CodProgetto;

		IF Project_Manager NOT IN (SELECT UserID
								  FROM CompMeeting
								  WHERE CodMeeting = new.CodMeeting) THEN
								  
            RAISE 'Non è possibile avviare un meeting su un progetto senza il relativo project manager.';
            RETURN NULL;
        END IF;
    END IF;
RETURN NEW;
END;
$$;


--
-- TOC entry 242 (class 1255 OID 98438)
-- Name: min_partecipanti_meeting(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.min_partecipanti_meeting() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE N_Partecipanti integer;
DECLARE N_Progettisti integer;

BEGIN
	IF old.iniziato = false THEN
		SELECT COUNT(UserID) INTO N_Partecipanti
		FROM CompMeeting
		WHERE CodMeeting = new.CodMeeting;
		
		SELECT COUNT(UserID) INTO N_Progettisti
		FROM Partecipante
		WHERE CodProgetto = new.CodProgetto;
		
		IF N_Partecipanti < (N_Progettisti::double precision)/3 THEN
			RAISE 'Non è possibile avviare un meeting su un progetto con un numero di partecipanti minore ad 1/3 del numero totale di partecipanti al progetto in questione.';
			RETURN NULL;
		END IF;
	END IF;
RETURN NEW;
END;
$$;


--
-- TOC entry 243 (class 1255 OID 82191)
-- Name: storico_progetti(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.storico_progetti() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE progettista integer;
BEGIN
	IF new.terminato=TRUE THEN
		INSERT INTO ProgRealizzato
		VALUES (old.codprogetto, old.tipologia);
									 
		FOR progettista IN (SELECT userid
							FROM Partecipante
							WHERE codprogetto=old.codprogetto)
							
		LOOP
			INSERT INTO partecipanteprog
			VALUES (old.codprogetto, progettista);
		END LOOP;
		
		DELETE FROM Progetto
		WHERE codprogetto=old.codprogetto;
	END IF;
	RETURN NEW;
END;
$$;


--
-- TOC entry 223 (class 1255 OID 139501)
-- Name: un_meeting_alla_volta(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.un_meeting_alla_volta() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	IF New.CodProgetto IN (SELECT CodProgetto
						 FROM Meeting AS M
						 WHERE OraFine IS NULL) THEN
				RAISE 'Un meeting per questo progetto è già calendarizzato';
				RETURN NULL;
	END IF;
	RETURN NEW;
END
$$;


SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 139571)
-- Name: ambito; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.ambito (
    nome character varying(25) NOT NULL
);


--
-- TOC entry 200 (class 1259 OID 40960)
-- Name: azienda; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.azienda (
    partiva character varying(11) NOT NULL,
    nome character varying(12) NOT NULL,
    sedeprincipale character varying(12) NOT NULL,
    CONSTRAINT checkpartiva CHECK (((partiva)::text ~* '[0-9]{7}0[0-9]{3}'::text))
);


--
-- TOC entry 207 (class 1259 OID 74015)
-- Name: compmeeting; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.compmeeting (
    codmeeting integer NOT NULL,
    userid integer NOT NULL
);


--
-- TOC entry 205 (class 1259 OID 41037)
-- Name: meeting; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.meeting (
    codmeeting integer NOT NULL,
    datariunione date NOT NULL,
    orainizio time without time zone NOT NULL,
    orafine time without time zone NOT NULL,
    piattaforma character varying(10),
    luogo character varying(10),
    codprogetto integer NOT NULL,
    iniziato boolean DEFAULT false NOT NULL,
    finito boolean DEFAULT false NOT NULL,
    CONSTRAINT luogo_riunione CHECK ((((luogo IS NOT NULL) AND (piattaforma IS NULL)) OR ((luogo IS NULL) AND (piattaforma IS NOT NULL)))),
    CONSTRAINT meeting_futuri CHECK (((datariunione - CURRENT_DATE) >= 14)),
    CONSTRAINT "validitàdatameeting" CHECK ((datariunione >= CURRENT_DATE)),
    CONSTRAINT "validitàorariomeeting" CHECK (((orafine - orainizio) > '00:30:00'::interval))
);


--
-- TOC entry 210 (class 1259 OID 82169)
-- Name: meeting_codmeeting_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.meeting ALTER COLUMN codmeeting ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.meeting_codmeeting_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 218 (class 1259 OID 139588)
-- Name: meetingimminenti; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW public.meetingimminenti AS
 SELECT m.codmeeting,
    m.codprogetto
   FROM public.meeting m
  WHERE ((m.datariunione - CURRENT_DATE) <= 7);


--
-- TOC entry 208 (class 1259 OID 74028)
-- Name: partecipanteprog; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.partecipanteprog (
    codprog integer NOT NULL,
    userid integer NOT NULL,
    valutazione integer
);


--
-- TOC entry 219 (class 1259 OID 139592)
-- Name: numprogetti; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW public.numprogetti AS
 SELECT partecipanteprog.userid,
    count(partecipanteprog.codprog) AS numeroprogettirealizzati
   FROM public.partecipanteprog
  GROUP BY partecipanteprog.userid;


--
-- TOC entry 204 (class 1259 OID 41014)
-- Name: partecipante; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.partecipante (
    userid integer NOT NULL,
    pw character varying(30),
    cf character varying(16) NOT NULL,
    nome character varying(20) NOT NULL,
    cognome character varying(20) NOT NULL,
    ruolo public.enum_ruolo NOT NULL,
    salariomedio numeric NOT NULL,
    partiva character varying(11) NOT NULL,
    codprogetto integer,
    CONSTRAINT check_password CHECK (((pw)::text ~* '(?=^.{6,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$'::text)),
    CONSTRAINT controllo_cf CHECK (((cf)::text ~* '[A-Z]{6}\d{2}[A-Z]\d{2}[A-Z]\d{3}[A-Z]'::text)),
    CONSTRAINT stipendiolegale CHECK ((salariomedio >= (1500)::numeric))
);


--
-- TOC entry 209 (class 1259 OID 82165)
-- Name: partecipante_userid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.partecipante ALTER COLUMN userid ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.partecipante_userid_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 211 (class 1259 OID 82173)
-- Name: partecipanteprog_codprog_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.partecipanteprog ALTER COLUMN codprog ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.partecipanteprog_codprog_seq
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 214 (class 1259 OID 139538)
-- Name: partecipantiliberi; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW public.partecipantiliberi AS
 SELECT partecipante.userid
   FROM public.partecipante
  WHERE (partecipante.codprogetto IS NULL);


--
-- TOC entry 201 (class 1259 OID 40965)
-- Name: privato; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.privato (
    cf character varying(16) NOT NULL,
    nome character varying(20) NOT NULL,
    cognome character varying(20) NOT NULL,
    CONSTRAINT controllocf CHECK (((cf)::text ~* '[A-Z]{6}\d{2}[A-Z]\d{2}[A-Z]\d{3}[A-Z]'::text))
);


--
-- TOC entry 216 (class 1259 OID 139557)
-- Name: progambito; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.progambito (
    codprogetto integer NOT NULL,
    nome character varying(25) DEFAULT NULL::character varying
);


--
-- TOC entry 203 (class 1259 OID 40984)
-- Name: progetto; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.progetto (
    codprogetto integer NOT NULL,
    tipologia public.enum_tipologia NOT NULL,
    numeropartecipanti integer DEFAULT 0 NOT NULL,
    budget numeric NOT NULL,
    partiva character varying(11) NOT NULL,
    cf character varying(16),
    partitaiva character varying(11),
    terminato boolean DEFAULT false NOT NULL,
    CONSTRAINT budgetlegale CHECK ((budget >= (5000)::numeric)),
    CONSTRAINT commissione CHECK (((partiva)::text <> (partitaiva)::text)),
    CONSTRAINT progetto_numeropartecipanti_check CHECK (((numeropartecipanti >= 0) AND (numeropartecipanti <= 30))),
    CONSTRAINT vincolo_cliente CHECK ((((cf IS NOT NULL) AND (partitaiva IS NULL)) OR ((cf IS NULL) AND (partitaiva IS NOT NULL))))
);


--
-- TOC entry 212 (class 1259 OID 82179)
-- Name: progetto_codprogetto_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.progetto ALTER COLUMN codprogetto ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.progetto_codprogetto_seq
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 206 (class 1259 OID 73764)
-- Name: progrealizzato; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.progrealizzato (
    codprog integer NOT NULL,
    tipologia public.enum_tipologia NOT NULL
);


--
-- TOC entry 213 (class 1259 OID 82189)
-- Name: progrealizzato_codprog_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.progrealizzato ALTER COLUMN codprog ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.progrealizzato_codprog_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 202 (class 1259 OID 40970)
-- Name: società; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."società" (
    partitaiva character varying(11) NOT NULL,
    nomesocieta character varying(25) NOT NULL,
    CONSTRAINT checkpartitaiva CHECK (((partitaiva)::text ~* '[0-9]{7}0[0-9]{3}'::text))
);


--
-- TOC entry 220 (class 1259 OID 139596)
-- Name: tipologieprogetti; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW public.tipologieprogetti AS
 SELECT progetto.tipologia,
    count(progetto.codprogetto) AS numeroprogettiassociati
   FROM public.progetto
  GROUP BY progetto.tipologia;


--
-- TOC entry 215 (class 1259 OID 139547)
-- Name: valutazionemedia; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW public.valutazionemedia AS
 SELECT partecipanteprog.userid,
    avg(partecipanteprog.valutazione) AS valutazionemedia
   FROM public.partecipanteprog
  GROUP BY partecipanteprog.userid;


--
-- TOC entry 3158 (class 0 OID 139571)
-- Dependencies: 217
-- Data for Name: ambito; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.ambito (nome) VALUES ('Elettronica');
INSERT INTO public.ambito (nome) VALUES ('Medicina');
INSERT INTO public.ambito (nome) VALUES ('Economia e finanza');
INSERT INTO public.ambito (nome) VALUES ('Informatica');


--
-- TOC entry 3143 (class 0 OID 40960)
-- Dependencies: 200
-- Data for Name: azienda; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.azienda (partiva, nome, sedeprincipale) VALUES ('12345670333', 'Basi Di Dati', 'Napoli');
INSERT INTO public.azienda (partiva, nome, sedeprincipale) VALUES ('12345670222', 'PostgreSQL', 'Roma');
INSERT INTO public.azienda (partiva, nome, sedeprincipale) VALUES ('12345670444', 'pgAdmin4', 'Via Claudio');


--
-- TOC entry 3150 (class 0 OID 74015)
-- Dependencies: 207
-- Data for Name: compmeeting; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.compmeeting (codmeeting, userid) VALUES (42, 17);
INSERT INTO public.compmeeting (codmeeting, userid) VALUES (42, 18);
INSERT INTO public.compmeeting (codmeeting, userid) VALUES (42, 19);
INSERT INTO public.compmeeting (codmeeting, userid) VALUES (42, 20);
INSERT INTO public.compmeeting (codmeeting, userid) VALUES (43, 21);
INSERT INTO public.compmeeting (codmeeting, userid) VALUES (43, 22);
INSERT INTO public.compmeeting (codmeeting, userid) VALUES (43, 23);


--
-- TOC entry 3148 (class 0 OID 41037)
-- Dependencies: 205
-- Data for Name: meeting; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.meeting (codmeeting, datariunione, orainizio, orafine, piattaforma, luogo, codprogetto, iniziato, finito) VALUES (42, '2021-08-01', '09:00:00', '11:00:00', 'Skype', NULL, 19, false, false);
INSERT INTO public.meeting (codmeeting, datariunione, orainizio, orafine, piattaforma, luogo, codprogetto, iniziato, finito) VALUES (43, '2021-08-04', '16:30:00', '17:30:00', NULL, 'Sala 1', 20, false, false);


--
-- TOC entry 3147 (class 0 OID 41014)
-- Dependencies: 204
-- Data for Name: partecipante; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.partecipante (userid, pw, cf, nome, cognome, ruolo, salariomedio, partiva, codprogetto) VALUES (18, NULL, 'LGRMRA00E06B963U', 'Mario', 'Liguori', 'Responsabile Area Informatica', 2500, '12345670333', 19);
INSERT INTO public.partecipante (userid, pw, cf, nome, cognome, ruolo, salariomedio, partiva, codprogetto) VALUES (19, NULL, 'SMNCRS00E16F839Q', 'Christian', 'Simeone', 'Coordinatore Di Attività', 2500, '12345670333', 19);
INSERT INTO public.partecipante (userid, pw, cf, nome, cognome, ruolo, salariomedio, partiva, codprogetto) VALUES (20, NULL, 'SLVNZE01C56F839S', 'Enza', 'Silvis', 'Coordinatore Di Attività', 2400, '12345670333', 19);
INSERT INTO public.partecipante (userid, pw, cf, nome, cognome, ruolo, salariomedio, partiva, codprogetto) VALUES (21, NULL, 'ZZAFNC97R05F839R', 'Francesco', 'Zaza', 'Project Manager', 2000, '12345670222', 20);
INSERT INTO public.partecipante (userid, pw, cf, nome, cognome, ruolo, salariomedio, partiva, codprogetto) VALUES (22, NULL, 'VRRLSS00T45F799E', 'Alessia', 'Verrazzo', 'Responsabile Della Comunicazione', 2100, '12345670222', 20);
INSERT INTO public.partecipante (userid, pw, cf, nome, cognome, ruolo, salariomedio, partiva, codprogetto) VALUES (23, NULL, 'RLNPQL00D27L259K', 'Pasquale', 'Orlando', 'Responsabile Area Informatica', 2500, '12345670222', 20);
INSERT INTO public.partecipante (userid, pw, cf, nome, cognome, ruolo, salariomedio, partiva, codprogetto) VALUES (17, NULL, 'CHHBCG00E47F799U', 'Bianca Giada', 'Chehade', 'Project Manager', 3500, '12345670333', 19);


--
-- TOC entry 3151 (class 0 OID 74028)
-- Dependencies: 208
-- Data for Name: partecipanteprog; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3144 (class 0 OID 40965)
-- Dependencies: 201
-- Data for Name: privato; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.privato (cf, nome, cognome) VALUES ('PLLCMN00S17H860L', 'Carmine', 'Paolella');


--
-- TOC entry 3157 (class 0 OID 139557)
-- Dependencies: 216
-- Data for Name: progambito; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.progambito (codprogetto, nome) VALUES (19, 'Informatica');
INSERT INTO public.progambito (codprogetto, nome) VALUES (20, 'Elettronica');


--
-- TOC entry 3146 (class 0 OID 40984)
-- Dependencies: 203
-- Data for Name: progetto; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.progetto (codprogetto, tipologia, numeropartecipanti, budget, partiva, cf, partitaiva, terminato) VALUES (19, 'Ricerca sperimentale', 0, 10000, '12345670333', 'PLLCMN00S17H860L', NULL, false);
INSERT INTO public.progetto (codprogetto, tipologia, numeropartecipanti, budget, partiva, cf, partitaiva, terminato) VALUES (20, 'Ricerca industriale', 0, 15000, '12345670333', NULL, '12345670555', false);


--
-- TOC entry 3149 (class 0 OID 73764)
-- Dependencies: 206
-- Data for Name: progrealizzato; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3145 (class 0 OID 40970)
-- Dependencies: 202
-- Data for Name: società; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public."società" (partitaiva, nomesocieta) VALUES ('12345670555', 'SpaceX');


--
-- TOC entry 3165 (class 0 OID 0)
-- Dependencies: 210
-- Name: meeting_codmeeting_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.meeting_codmeeting_seq', 43, true);


--
-- TOC entry 3166 (class 0 OID 0)
-- Dependencies: 209
-- Name: partecipante_userid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.partecipante_userid_seq', 20, true);


--
-- TOC entry 3167 (class 0 OID 0)
-- Dependencies: 211
-- Name: partecipanteprog_codprog_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.partecipanteprog_codprog_seq', 0, false);


--
-- TOC entry 3168 (class 0 OID 0)
-- Dependencies: 212
-- Name: progetto_codprogetto_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.progetto_codprogetto_seq', 20, true);


--
-- TOC entry 3169 (class 0 OID 0)
-- Dependencies: 213
-- Name: progrealizzato_codprog_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.progrealizzato_codprog_seq', 1, false);


--
-- TOC entry 2981 (class 2606 OID 139575)
-- Name: ambito ambito_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ambito
    ADD CONSTRAINT ambito_pkey PRIMARY KEY (nome);


--
-- TOC entry 2963 (class 2606 OID 106631)
-- Name: azienda azienda_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.azienda
    ADD CONSTRAINT azienda_pkey PRIMARY KEY (partiva);


--
-- TOC entry 2975 (class 2606 OID 41041)
-- Name: meeting meeting_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.meeting
    ADD CONSTRAINT meeting_pkey PRIMARY KEY (codmeeting);


--
-- TOC entry 2971 (class 2606 OID 73958)
-- Name: partecipante partecipante_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_pkey PRIMARY KEY (userid);


--
-- TOC entry 2965 (class 2606 OID 40969)
-- Name: privato privato_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.privato
    ADD CONSTRAINT privato_pkey PRIMARY KEY (cf);


--
-- TOC entry 2969 (class 2606 OID 40992)
-- Name: progetto progetto_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_pkey PRIMARY KEY (codprogetto);


--
-- TOC entry 2977 (class 2606 OID 73771)
-- Name: progrealizzato progrealizzato_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.progrealizzato
    ADD CONSTRAINT progrealizzato_pkey PRIMARY KEY (codprog);


--
-- TOC entry 2967 (class 2606 OID 40974)
-- Name: società societa_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."società"
    ADD CONSTRAINT societa_pkey PRIMARY KEY (partitaiva);


--
-- TOC entry 2973 (class 2606 OID 73860)
-- Name: partecipante unico_cf; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT unico_cf UNIQUE (cf);


--
-- TOC entry 2979 (class 2606 OID 139504)
-- Name: compmeeting unique_partecipante_meeting; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.compmeeting
    ADD CONSTRAINT unique_partecipante_meeting UNIQUE (codmeeting, userid);


--
-- TOC entry 3004 (class 2620 OID 139584)
-- Name: meeting ambitoprogettomeeting; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER ambitoprogettomeeting BEFORE INSERT ON public.meeting FOR EACH ROW EXECUTE FUNCTION public.inserimentomeetingambito();


--
-- TOC entry 2997 (class 2620 OID 139587)
-- Name: partecipante ambitoprogettopartecipante; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER ambitoprogettopartecipante BEFORE INSERT ON public.partecipante FOR EACH ROW EXECUTE FUNCTION public.inserimentopartecipanteambito();


--
-- TOC entry 3002 (class 2620 OID 139524)
-- Name: meeting composizionemeeting; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER composizionemeeting BEFORE UPDATE OF iniziato ON public.meeting FOR EACH ROW EXECUTE FUNCTION public.min_partecipanti_meeting();


--
-- TOC entry 2994 (class 2620 OID 82199)
-- Name: progetto fineprogetto; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER fineprogetto AFTER UPDATE OF terminato ON public.progetto FOR EACH ROW EXECUTE FUNCTION public.storico_progetti();


--
-- TOC entry 2999 (class 2620 OID 74042)
-- Name: meeting luogooccupato; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER luogooccupato BEFORE INSERT ON public.meeting FOR EACH ROW EXECUTE FUNCTION public.controllo_luogo();


--
-- TOC entry 3001 (class 2620 OID 139522)
-- Name: meeting meeting_senza_manager; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER meeting_senza_manager BEFORE UPDATE OF iniziato ON public.meeting FOR EACH ROW EXECUTE FUNCTION public.meeting_senza_pm();


--
-- TOC entry 3005 (class 2620 OID 82217)
-- Name: compmeeting meetingnonpermesso; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER meetingnonpermesso BEFORE INSERT ON public.compmeeting FOR EACH ROW EXECUTE FUNCTION public.check_meeting_progetto();


--
-- TOC entry 2998 (class 2620 OID 139603)
-- Name: partecipante nuovopartecipante; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER nuovopartecipante BEFORE UPDATE OF codprogetto ON public.partecipante FOR EACH ROW EXECUTE FUNCTION public.incrementapartecipanti();


--
-- TOC entry 3000 (class 2620 OID 139502)
-- Name: meeting onemeeting; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER onemeeting BEFORE INSERT ON public.meeting FOR EACH ROW EXECUTE FUNCTION public.un_meeting_alla_volta();


--
-- TOC entry 3006 (class 2620 OID 90253)
-- Name: compmeeting progettomismatch; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER progettomismatch BEFORE INSERT ON public.compmeeting FOR EACH ROW EXECUTE FUNCTION public.check_progetto_mismatch();


--
-- TOC entry 2996 (class 2620 OID 73838)
-- Name: partecipante projectmanager; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER projectmanager BEFORE INSERT ON public.partecipante FOR EACH ROW EXECUTE FUNCTION public.check_pm();


--
-- TOC entry 2995 (class 2620 OID 139556)
-- Name: progetto projecttopic; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER projecttopic AFTER INSERT ON public.progetto FOR EACH ROW EXECUTE FUNCTION public.ambitoprogetto();


--
-- TOC entry 3003 (class 2620 OID 139582)
-- Name: meeting tipologiaprogettomeeting; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER tipologiaprogettomeeting BEFORE INSERT ON public.meeting FOR EACH ROW EXECUTE FUNCTION public.inserimentomeetingtipologia();


--
-- TOC entry 3007 (class 2620 OID 139546)
-- Name: partecipanteprog valutazioneaziendale; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER valutazioneaziendale BEFORE UPDATE OF valutazione ON public.partecipanteprog FOR EACH ROW EXECUTE FUNCTION public.check_valutazione();


--
-- TOC entry 2988 (class 2606 OID 74018)
-- Name: compmeeting compmeeting_codmeeting_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.compmeeting
    ADD CONSTRAINT compmeeting_codmeeting_fkey FOREIGN KEY (codmeeting) REFERENCES public.meeting(codmeeting);


--
-- TOC entry 2989 (class 2606 OID 74023)
-- Name: compmeeting compmeeting_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.compmeeting
    ADD CONSTRAINT compmeeting_userid_fkey FOREIGN KEY (userid) REFERENCES public.partecipante(userid);


--
-- TOC entry 2987 (class 2606 OID 90246)
-- Name: meeting meeting_codprogetto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.meeting
    ADD CONSTRAINT meeting_codprogetto_fkey FOREIGN KEY (codprogetto) REFERENCES public.progetto(codprogetto);


--
-- TOC entry 2985 (class 2606 OID 82184)
-- Name: partecipante partecipante_codprogetto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_codprogetto_fkey FOREIGN KEY (codprogetto) REFERENCES public.progetto(codprogetto) ON DELETE SET NULL;


--
-- TOC entry 2986 (class 2606 OID 106646)
-- Name: partecipante partecipante_partiva_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_partiva_fkey FOREIGN KEY (partiva) REFERENCES public.azienda(partiva);


--
-- TOC entry 2990 (class 2606 OID 74031)
-- Name: partecipanteprog partecipanteprog_codprog_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.partecipanteprog
    ADD CONSTRAINT partecipanteprog_codprog_fkey FOREIGN KEY (codprog) REFERENCES public.progrealizzato(codprog);


--
-- TOC entry 2991 (class 2606 OID 74036)
-- Name: partecipanteprog partecipanteprog_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.partecipanteprog
    ADD CONSTRAINT partecipanteprog_userid_fkey FOREIGN KEY (userid) REFERENCES public.partecipante(userid);


--
-- TOC entry 2992 (class 2606 OID 139561)
-- Name: progambito progambito_codprogetto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.progambito
    ADD CONSTRAINT progambito_codprogetto_fkey FOREIGN KEY (codprogetto) REFERENCES public.progetto(codprogetto);


--
-- TOC entry 2993 (class 2606 OID 139576)
-- Name: progambito progambito_nome_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.progambito
    ADD CONSTRAINT progambito_nome_fkey FOREIGN KEY (nome) REFERENCES public.ambito(nome);


--
-- TOC entry 2982 (class 2606 OID 40998)
-- Name: progetto progetto_cf_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_cf_fkey FOREIGN KEY (cf) REFERENCES public.privato(cf);


--
-- TOC entry 2983 (class 2606 OID 41003)
-- Name: progetto progetto_partitaiva_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_partitaiva_fkey FOREIGN KEY (partitaiva) REFERENCES public."società"(partitaiva);


--
-- TOC entry 2984 (class 2606 OID 106660)
-- Name: progetto progetto_partiva_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_partiva_fkey FOREIGN KEY (partiva) REFERENCES public.azienda(partiva);


-- Completed on 2021-07-10 17:18:26

--
-- PostgreSQL database dump complete
--

