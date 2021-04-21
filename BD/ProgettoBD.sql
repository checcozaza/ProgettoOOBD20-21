--
-- PostgreSQL database dump
--

-- Dumped from database version 13.1
-- Dumped by pg_dump version 13.2

-- Started on 2021-04-21 20:38:46

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
-- TOC entry 657 (class 1247 OID 40979)
-- Name: enum_ambito; Type: DOMAIN; Schema: public; Owner: -
--

CREATE DOMAIN public.enum_ambito AS character varying(50)
	CONSTRAINT enum_ambito CHECK ((upper((VALUE)::text) = ANY (ARRAY['ECONOMIA E FINANZA'::text, 'MEDICINA'::text, 'INFORMATICA'::text, 'ALIMENTARE'::text, 'AUTOMOBILISTICA'::text, 'FARMACEUTICA'::text, 'ELETTRONICA'::text, 'MARKETING'::text, 'RICERCA E SVILUPPO'::text, 'CHIMICA'::text])));


--
-- TOC entry 661 (class 1247 OID 40982)
-- Name: enum_ruolo; Type: DOMAIN; Schema: public; Owner: -
--

CREATE DOMAIN public.enum_ruolo AS character varying(50)
	CONSTRAINT enum_ruolo CHECK ((upper((VALUE)::text) = ANY (ARRAY['RESPONSABILE DELLA COMUNICAZIONE'::text, 'COORDINATORE DI ATTIVITÀ'::text, 'RESPONSABILE AMMINISTRAZIONE'::text, 'RESPONSABILE AREA INFORMATICA'::text, 'PROJECT MANAGER'::text])));


--
-- TOC entry 653 (class 1247 OID 40976)
-- Name: enum_tipologia; Type: DOMAIN; Schema: public; Owner: -
--

CREATE DOMAIN public.enum_tipologia AS character varying(50)
	CONSTRAINT enum_tipologia CHECK ((upper((VALUE)::text) = ANY (ARRAY['RICERCA DI BASE'::text, 'RICERCA INDUSTRIALE'::text, 'RICERCA SPERIMENTALE'::text, 'SVILUPPO SPERIMENTALE'::text])));


--
-- TOC entry 220 (class 1255 OID 82216)
-- Name: check_meeting_progetto(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_meeting_progetto() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE utenti_liberi REFCURSOR;
DECLARE progettista_libero integer;

BEGIN
	OPEN utenti_liberi FOR (SELECT UserID FROM Partecipante WHERE CodProgetto IS NULL);
	LOOP
		EXIT WHEN NOT FOUND;
		FETCH utenti_liberi INTO progettista_libero;
		IF new.UserID = progettista_libero THEN
			RAISE 'Impossibile partecipare a questo meeting: il progettista non ha attualmente alcun progetto a carico';
			RETURN NULL;
		END IF;
		RETURN NEW;
	END LOOP;
	CLOSE utenti_liberi;
END;
$$;


--
-- TOC entry 216 (class 1255 OID 73831)
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
-- TOC entry 233 (class 1255 OID 90252)
-- Name: check_progetto_mismatch(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_progetto_mismatch() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
		IF new.UserID IN (SELECT UserID 
						FROM Partecipante AS PA JOIN Meeting AS ME ON PA.CodProgetto=ME.CodProgetto
						WHERE new.CodMeeting IN (SELECT CodMeeting 
											FROM Meeting
											WHERE CodProgetto <> PA.CodProgetto)) THEN
			RAISE 'Il progettista può partecipare solo a meeting su progetti a cui partecipa';
			RETURN NULL;
		END IF;
		RETURN NEW;
END;
$$;


--
-- TOC entry 217 (class 1255 OID 82100)
-- Name: check_valutazione(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_valutazione() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	IF old.valutazione IS NOT NULL THEN
		RAISE 'Un partecipante non può avere più di una valutazione per progetto';
		RETURN NULL;
	ELSE
		UPDATE Partecipante
		SET valutazione=new.valutazione;
	END IF;
	RETURN NEW;
END;

$$;


--
-- TOC entry 221 (class 1255 OID 74041)
-- Name: controllo_luogo(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.controllo_luogo() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE n_riunioni integer;

BEGIN
	SELECT COUNT(*) INTO n_riunioni
	FROM MEETING AS M
	WHERE luogo=new.luogo AND DataRiunione=new.DataRiunione AND
	(new.OraInizio >= OraInizio AND new.OraInizio <= OraFine);
	
	IF (n_riunioni > 0) THEN
		RAISE 'Errore, il luogo è già occupato da una riunione';
		RETURN NULL;
	END IF;
	RETURN NEW;
END;


$$;


--
-- TOC entry 235 (class 1255 OID 106628)
-- Name: controllo_partecipante_meeting(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.controllo_partecipante_meeting() RETURNS trigger
    LANGUAGE plpgsql
    AS $$

DECLARE Partecipanti REFCURSOR;
DECLARE Partecipante_Occupato integer;

BEGIN
	OPEN Partecipanti FOR (SELECT UserID
							FROM CompMeeting AS C NATURAL JOIN Meeting AS M
							WHERE OraInizio IS NOT NULL AND OraFine IS NULL);
							
	LOOP
		EXIT WHEN NOT FOUND;
		FETCH Partecipanti INTO Partecipante_Occupato;
		
		IF new.UserID = Partecipante_Occupato THEN
			RAISE 'Un dipendente può partecipare a un solo meeting alla volta.';
			RETURN NULL;
		END IF;
	END LOOP;
	CLOSE Partecipanti;
RETURN NEW;
END;
$$;


--
-- TOC entry 218 (class 1255 OID 98440)
-- Name: meeting_senza_pm(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.meeting_senza_pm() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE Project_Manager integer;

BEGIN
    IF old.OraInizio IS NULL THEN
        SELECT UserID INTO Project_Manager
		FROM Partecipante
		WHERE Ruolo = 'Project Manager' AND CodProgetto = new.CodProgetto;

		IF Project_Manager NOT IN (SELECT UserId
								  FROM CompMeeting
								  WHERE CodMeeting = new.CodMeeting) THEN
								  
            RAISE 'Non è possibile avviare un meeting su un progetto senza il relativo project manager.';
            RETURN NULL;
        END IF;
		RETURN NEW;
    END IF;
END;
$$;


--
-- TOC entry 234 (class 1255 OID 98438)
-- Name: min_partecipanti_meeting(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.min_partecipanti_meeting() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE N_Partecipanti integer;
DECLARE N_Progettisti integer;

BEGIN
	IF old.OraInizio IS NULL THEN
		SELECT COUNT(UserID) INTO N_Partecipanti
		FROM CompMeeting
		WHERE CodMeeting = new.CodMeeting;
		
		SELECT COUNT(UserID) INTO N_Progettisti
		FROM PartecipanteProg
		WHERE CodProg = new.CodMeeting;
		
		IF N_Partecipanti < (1/3 * (N_Progettisti)) THEN
			RAISE 'Non è possibile avviare un meeting su un progetto con un numero di partecipanti minore ad 1/3 del numero totale di partecipanti al progetto in questione.';
			RETURN NULL;
		END IF;
	END IF;
END;
$$;


--
-- TOC entry 219 (class 1255 OID 82191)
-- Name: storico_progetti(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.storico_progetti() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	IF new.terminato=TRUE THEN
		INSERT INTO ProgRealizzato
		VALUES (old.codprogetto, old.tipologia);
		
		DELETE FROM Progetto
		WHERE CodProgetto=old.codprogetto;
	END IF;
	RETURN NEW;
END;

$$;


SET default_table_access_method = heap;

--
-- TOC entry 207 (class 1259 OID 73785)
-- Name: ambito; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.ambito (
    nome public.enum_ambito NOT NULL
);


--
-- TOC entry 200 (class 1259 OID 40960)
-- Name: azienda; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.azienda (
    isin character varying(12) NOT NULL,
    nome character varying(12) NOT NULL,
    sedeprincipale character varying(12) NOT NULL
);


--
-- TOC entry 209 (class 1259 OID 74015)
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
    orafine time without time zone,
    piattaforma character varying(10),
    luogo character varying(10),
    codprogetto integer NOT NULL,
    CONSTRAINT luogo_riunione CHECK ((((luogo IS NOT NULL) AND (piattaforma IS NULL)) OR ((luogo IS NULL) AND (piattaforma IS NOT NULL))))
);


--
-- TOC entry 212 (class 1259 OID 82169)
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
-- TOC entry 204 (class 1259 OID 41014)
-- Name: partecipante; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.partecipante (
    userid integer NOT NULL,
    email character varying(30),
    pw character varying(30),
    cf character varying(16) NOT NULL,
    nome character varying(20) NOT NULL,
    cognome character varying(20) NOT NULL,
    ruolo public.enum_ruolo NOT NULL,
    salariomedio numeric NOT NULL,
    valutazione integer,
    isin character varying(12) NOT NULL,
    codprogetto integer,
    CONSTRAINT check_password CHECK (((pw)::text ~* '(?=^.{6,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$'::text)),
    CONSTRAINT controllo_cf CHECK (((cf)::text ~* '[A-Z]{6}\d{2}[A-Z]\d{2}[A-Z]\d{3}[A-Z]'::text)),
    CONSTRAINT controllo_valutazione CHECK (((valutazione >= 0) AND (valutazione <= 5)))
);


--
-- TOC entry 211 (class 1259 OID 82165)
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
-- TOC entry 210 (class 1259 OID 74028)
-- Name: partecipanteprog; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.partecipanteprog (
    codprog integer NOT NULL,
    userid integer NOT NULL
);


--
-- TOC entry 213 (class 1259 OID 82173)
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
-- TOC entry 208 (class 1259 OID 73806)
-- Name: progambito; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.progambito (
    codprogetto integer NOT NULL,
    nome public.enum_ambito NOT NULL
);


--
-- TOC entry 203 (class 1259 OID 40984)
-- Name: progetto; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.progetto (
    codprogetto integer NOT NULL,
    tipologia public.enum_tipologia NOT NULL,
    numeropartecipanti integer NOT NULL,
    budget numeric NOT NULL,
    isin character varying(12) NOT NULL,
    cf character varying(16),
    partitaiva character varying(11),
    terminato boolean DEFAULT false NOT NULL,
    CONSTRAINT progetto_numeropartecipanti_check CHECK (((numeropartecipanti > 0) AND (numeropartecipanti <= 30))),
    CONSTRAINT vincolo_cliente CHECK ((((cf IS NOT NULL) AND (partitaiva IS NULL)) OR ((cf IS NULL) AND (partitaiva IS NOT NULL))))
);


--
-- TOC entry 214 (class 1259 OID 82179)
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
-- TOC entry 215 (class 1259 OID 82189)
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
    nomesocieta character varying(25) NOT NULL
);


--
-- TOC entry 3110 (class 0 OID 73785)
-- Dependencies: 207
-- Data for Name: ambito; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.ambito (nome) FROM stdin;
\.


--
-- TOC entry 3103 (class 0 OID 40960)
-- Dependencies: 200
-- Data for Name: azienda; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.azienda (isin, nome, sedeprincipale) FROM stdin;
BCFZ1	InfoTech	Via Chiaia
bianca1	nomegenerico	napoli
ciao1	nintendo	new york
ciao2	CiaoMamma	letto
\.


--
-- TOC entry 3112 (class 0 OID 74015)
-- Dependencies: 209
-- Data for Name: compmeeting; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.compmeeting (codmeeting, userid) FROM stdin;
\.


--
-- TOC entry 3108 (class 0 OID 41037)
-- Dependencies: 205
-- Data for Name: meeting; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.meeting (codmeeting, datariunione, orainizio, orafine, piattaforma, luogo, codprogetto) FROM stdin;
2	2021-05-22	17:00:00	18:30:00	Zoom	\N	2
3	2021-05-25	17:00:00	18:30:00	Zoom	\N	3
\.


--
-- TOC entry 3107 (class 0 OID 41014)
-- Dependencies: 204
-- Data for Name: partecipante; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.partecipante (userid, email, pw, cf, nome, cognome, ruolo, salariomedio, valutazione, isin, codprogetto) FROM stdin;
3	\N	\N	ZZAFNC97R05F839R	Checco	Zaza	responsabile area informatica	2000	3	ciao2	2
\.


--
-- TOC entry 3113 (class 0 OID 74028)
-- Dependencies: 210
-- Data for Name: partecipanteprog; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.partecipanteprog (codprog, userid) FROM stdin;
\.


--
-- TOC entry 3104 (class 0 OID 40965)
-- Dependencies: 201
-- Data for Name: privato; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.privato (cf, nome, cognome) FROM stdin;
CHHBCG00E47F799U	Bianca	Chehade
\.


--
-- TOC entry 3111 (class 0 OID 73806)
-- Dependencies: 208
-- Data for Name: progambito; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.progambito (codprogetto, nome) FROM stdin;
\.


--
-- TOC entry 3106 (class 0 OID 40984)
-- Dependencies: 203
-- Data for Name: progetto; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.progetto (codprogetto, tipologia, numeropartecipanti, budget, isin, cf, partitaiva, terminato) FROM stdin;
2	Ricerca di base	12	5000	ciao2	CHHBCG00E47F799U	\N	f
3	Ricerca di base	15	5000	ciao2	CHHBCG00E47F799U	\N	f
\.


--
-- TOC entry 3109 (class 0 OID 73764)
-- Dependencies: 206
-- Data for Name: progrealizzato; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.progrealizzato (codprog, tipologia) FROM stdin;
1	ricerca di base
\.


--
-- TOC entry 3105 (class 0 OID 40970)
-- Dependencies: 202
-- Data for Name: società; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."società" (partitaiva, nomesocieta) FROM stdin;
ciaociao1	PovertàSQL
\.


--
-- TOC entry 3124 (class 0 OID 0)
-- Dependencies: 212
-- Name: meeting_codmeeting_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.meeting_codmeeting_seq', 4, true);


--
-- TOC entry 3125 (class 0 OID 0)
-- Dependencies: 211
-- Name: partecipante_userid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.partecipante_userid_seq', 4, true);


--
-- TOC entry 3126 (class 0 OID 0)
-- Dependencies: 213
-- Name: partecipanteprog_codprog_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.partecipanteprog_codprog_seq', 0, false);


--
-- TOC entry 3127 (class 0 OID 0)
-- Dependencies: 214
-- Name: progetto_codprogetto_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.progetto_codprogetto_seq', 3, true);


--
-- TOC entry 3128 (class 0 OID 0)
-- Dependencies: 215
-- Name: progrealizzato_codprog_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.progrealizzato_codprog_seq', 1, false);


--
-- TOC entry 2951 (class 2606 OID 73792)
-- Name: ambito ambito_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ambito
    ADD CONSTRAINT ambito_pkey PRIMARY KEY (nome);


--
-- TOC entry 2933 (class 2606 OID 40964)
-- Name: azienda azienda_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.azienda
    ADD CONSTRAINT azienda_pkey PRIMARY KEY (isin);


--
-- TOC entry 2947 (class 2606 OID 41041)
-- Name: meeting meeting_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.meeting
    ADD CONSTRAINT meeting_pkey PRIMARY KEY (codmeeting);


--
-- TOC entry 2941 (class 2606 OID 73958)
-- Name: partecipante partecipante_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_pkey PRIMARY KEY (userid);


--
-- TOC entry 2935 (class 2606 OID 40969)
-- Name: privato privato_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.privato
    ADD CONSTRAINT privato_pkey PRIMARY KEY (cf);


--
-- TOC entry 2939 (class 2606 OID 40992)
-- Name: progetto progetto_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_pkey PRIMARY KEY (codprogetto);


--
-- TOC entry 2949 (class 2606 OID 73771)
-- Name: progrealizzato progrealizzato_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.progrealizzato
    ADD CONSTRAINT progrealizzato_pkey PRIMARY KEY (codprog);


--
-- TOC entry 2937 (class 2606 OID 40974)
-- Name: società societa_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."società"
    ADD CONSTRAINT societa_pkey PRIMARY KEY (partitaiva);


--
-- TOC entry 2943 (class 2606 OID 73860)
-- Name: partecipante unico_cf; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT unico_cf UNIQUE (cf);


--
-- TOC entry 2945 (class 2606 OID 82201)
-- Name: partecipante unique_account; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT unique_account UNIQUE (cf, email);


--
-- TOC entry 2964 (class 2620 OID 82199)
-- Name: progetto fineprogetto; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER fineprogetto AFTER UPDATE OF terminato ON public.progetto FOR EACH ROW EXECUTE FUNCTION public.storico_progetti();


--
-- TOC entry 2967 (class 2620 OID 74042)
-- Name: meeting luogooccupato; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER luogooccupato BEFORE INSERT ON public.meeting FOR EACH ROW EXECUTE FUNCTION public.controllo_luogo();


--
-- TOC entry 2968 (class 2620 OID 98439)
-- Name: meeting meeting; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER meeting BEFORE UPDATE OF orainizio ON public.meeting FOR EACH ROW EXECUTE FUNCTION public.min_partecipanti_meeting();


--
-- TOC entry 2969 (class 2620 OID 98441)
-- Name: meeting meeting_senza_manager; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER meeting_senza_manager BEFORE UPDATE OF orainizio ON public.meeting FOR EACH ROW EXECUTE FUNCTION public.meeting_senza_pm();


--
-- TOC entry 2970 (class 2620 OID 82217)
-- Name: compmeeting meetingnonpermesso; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER meetingnonpermesso BEFORE INSERT ON public.compmeeting FOR EACH ROW EXECUTE FUNCTION public.check_meeting_progetto();


--
-- TOC entry 2972 (class 2620 OID 106629)
-- Name: compmeeting partecipantemeeting; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER partecipantemeeting BEFORE INSERT ON public.compmeeting FOR EACH ROW EXECUTE FUNCTION public.controllo_partecipante_meeting();


--
-- TOC entry 2971 (class 2620 OID 90253)
-- Name: compmeeting progettomismatch; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER progettomismatch BEFORE INSERT ON public.compmeeting FOR EACH ROW EXECUTE FUNCTION public.check_progetto_mismatch();


--
-- TOC entry 2965 (class 2620 OID 73838)
-- Name: partecipante projectmanager; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER projectmanager BEFORE INSERT ON public.partecipante FOR EACH ROW EXECUTE FUNCTION public.check_pm();


--
-- TOC entry 2966 (class 2620 OID 82101)
-- Name: partecipante valutazioneaziendale; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER valutazioneaziendale BEFORE UPDATE OF valutazione ON public.partecipante FOR EACH ROW EXECUTE FUNCTION public.check_valutazione();


--
-- TOC entry 2960 (class 2606 OID 74018)
-- Name: compmeeting compmeeting_codmeeting_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.compmeeting
    ADD CONSTRAINT compmeeting_codmeeting_fkey FOREIGN KEY (codmeeting) REFERENCES public.meeting(codmeeting);


--
-- TOC entry 2961 (class 2606 OID 74023)
-- Name: compmeeting compmeeting_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.compmeeting
    ADD CONSTRAINT compmeeting_userid_fkey FOREIGN KEY (userid) REFERENCES public.partecipante(userid);


--
-- TOC entry 2957 (class 2606 OID 90246)
-- Name: meeting meeting_codprogetto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.meeting
    ADD CONSTRAINT meeting_codprogetto_fkey FOREIGN KEY (codprogetto) REFERENCES public.progetto(codprogetto);


--
-- TOC entry 2956 (class 2606 OID 82184)
-- Name: partecipante partecipante_codprogetto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_codprogetto_fkey FOREIGN KEY (codprogetto) REFERENCES public.progetto(codprogetto) ON DELETE SET NULL;


--
-- TOC entry 2955 (class 2606 OID 41032)
-- Name: partecipante partecipante_isin_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_isin_fkey FOREIGN KEY (isin) REFERENCES public.azienda(isin);


--
-- TOC entry 2962 (class 2606 OID 74031)
-- Name: partecipanteprog partecipanteprog_codprog_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.partecipanteprog
    ADD CONSTRAINT partecipanteprog_codprog_fkey FOREIGN KEY (codprog) REFERENCES public.progrealizzato(codprog);


--
-- TOC entry 2963 (class 2606 OID 74036)
-- Name: partecipanteprog partecipanteprog_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.partecipanteprog
    ADD CONSTRAINT partecipanteprog_userid_fkey FOREIGN KEY (userid) REFERENCES public.partecipante(userid);


--
-- TOC entry 2958 (class 2606 OID 73812)
-- Name: progambito progambito_codprogetto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.progambito
    ADD CONSTRAINT progambito_codprogetto_fkey FOREIGN KEY (codprogetto) REFERENCES public.progetto(codprogetto);


--
-- TOC entry 2959 (class 2606 OID 73817)
-- Name: progambito progambito_nome_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.progambito
    ADD CONSTRAINT progambito_nome_fkey FOREIGN KEY (nome) REFERENCES public.ambito(nome);


--
-- TOC entry 2953 (class 2606 OID 40998)
-- Name: progetto progetto_cf_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_cf_fkey FOREIGN KEY (cf) REFERENCES public.privato(cf);


--
-- TOC entry 2952 (class 2606 OID 40993)
-- Name: progetto progetto_isin_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_isin_fkey FOREIGN KEY (isin) REFERENCES public.azienda(isin);


--
-- TOC entry 2954 (class 2606 OID 41003)
-- Name: progetto progetto_partitaiva_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_partitaiva_fkey FOREIGN KEY (partitaiva) REFERENCES public."società"(partitaiva);


-- Completed on 2021-04-21 20:38:54

--
-- PostgreSQL database dump complete
--

