PGDMP                         y           Progetto_BD    13.1    13.1 O    1           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            2           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            3           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            4           1262    32768    Progetto_BD    DATABASE     i   CREATE DATABASE "Progetto_BD" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Italian_Italy.1252';
    DROP DATABASE "Progetto_BD";
                postgres    false            �           1247    40979    enum_ambito    DOMAIN     [  CREATE DOMAIN public.enum_ambito AS character varying(50)
	CONSTRAINT enum_ambito CHECK ((upper((VALUE)::text) = ANY (ARRAY['ECONOMIA E FINANZA'::text, 'MEDICINA'::text, 'INFORMATICA'::text, 'ALIMENTARE'::text, 'AUTOMOBILISTICA'::text, 'FARMACEUTICA'::text, 'ELETTRONICA'::text, 'MARKETING'::text, 'RICERCA E SVILUPPO'::text, 'CHIMICA'::text])));
     DROP DOMAIN public.enum_ambito;
       public          postgres    false            �           1247    40982 
   enum_ruolo    DOMAIN     1  CREATE DOMAIN public.enum_ruolo AS character varying(50)
	CONSTRAINT enum_ruolo CHECK ((upper((VALUE)::text) = ANY (ARRAY['RESPONSABILE DELLA COMUNICAZIONE'::text, 'COORDINATORE DI ATTIVITÀ'::text, 'RESPONSABILE AMMINISTRAZIONE'::text, 'RESPONSABILE AREA INFORMATICA'::text, 'PROJECT MANAGER'::text])));
    DROP DOMAIN public.enum_ruolo;
       public          Bianca    false            �           1247    40976    enum_tipologia    DOMAIN     �   CREATE DOMAIN public.enum_tipologia AS character varying(50)
	CONSTRAINT enum_tipologia CHECK ((upper((VALUE)::text) = ANY (ARRAY['RICERCA DI BASE'::text, 'RICERCA INDUSTRIALE'::text, 'RICERCA SPERIMENTALE'::text, 'SVILUPPO SPERIMENTALE'::text])));
 #   DROP DOMAIN public.enum_tipologia;
       public          Bianca    false            �            1255    82216    check_meeting_progetto()    FUNCTION     !  CREATE FUNCTION public.check_meeting_progetto() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE utenti_liberi REFCURSOR;
DECLARE progettista_libero integer;

BEGIN
	OPEN utenti_liberi FOR (SELECT UserID FROM Partecipante WHERE CodProgetto IS NULL);
	LOOP
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
 /   DROP FUNCTION public.check_meeting_progetto();
       public          Bianca    false            �            1255    73831 
   check_pm()    FUNCTION     �  CREATE FUNCTION public.check_pm() RETURNS trigger
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
 !   DROP FUNCTION public.check_pm();
       public          Bianca    false            �            1255    90252    check_progetto_mismatch()    FUNCTION     �  CREATE FUNCTION public.check_progetto_mismatch() RETURNS trigger
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
 0   DROP FUNCTION public.check_progetto_mismatch();
       public          Bianca    false            �            1255    82100    check_valutazione()    FUNCTION     B  CREATE FUNCTION public.check_valutazione() RETURNS trigger
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
 *   DROP FUNCTION public.check_valutazione();
       public          Bianca    false            �            1255    74041    controllo_luogo()    FUNCTION     �  CREATE FUNCTION public.controllo_luogo() RETURNS trigger
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
 (   DROP FUNCTION public.controllo_luogo();
       public          Bianca    false            �            1255    74043     controllo_partecipante_meeting()    FUNCTION     �  CREATE FUNCTION public.controllo_partecipante_meeting() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE partecipanti integer;

BEGIN
	SELECT userid INTO partecipanti
	FROM CompMeeting AS C NATURAL JOIN Meeting AS M
	WHERE orainizio IS NOT NULL AND orafine IS NULL;
	
	IF new.userid IN (partecipanti) THEN
		RAISE 'Un dipendente può partecipare a solo un meeting alla volta';
		RETURN NULL;
	END IF;
	RETURN new;
END;
$$;
 7   DROP FUNCTION public.controllo_partecipante_meeting();
       public          Bianca    false            �            1255    98440    meeting_senza_pm()    FUNCTION     b  CREATE FUNCTION public.meeting_senza_pm() RETURNS trigger
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
 )   DROP FUNCTION public.meeting_senza_pm();
       public          Bianca    false            �            1255    98438    min_partecipanti_meeting()    FUNCTION     �  CREATE FUNCTION public.min_partecipanti_meeting() RETURNS trigger
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
 1   DROP FUNCTION public.min_partecipanti_meeting();
       public          Bianca    false            �            1255    82191    storico_progetti()    FUNCTION     "  CREATE FUNCTION public.storico_progetti() RETURNS trigger
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
 )   DROP FUNCTION public.storico_progetti();
       public          Bianca    false            �            1259    73785    ambito    TABLE     E   CREATE TABLE public.ambito (
    nome public.enum_ambito NOT NULL
);
    DROP TABLE public.ambito;
       public         heap    Bianca    false    657            �            1259    40960    azienda    TABLE     �   CREATE TABLE public.azienda (
    isin character varying(12) NOT NULL,
    nome character varying(12) NOT NULL,
    sedeprincipale character varying(12) NOT NULL
);
    DROP TABLE public.azienda;
       public         heap    postgres    false            �            1259    74015    compmeeting    TABLE     b   CREATE TABLE public.compmeeting (
    codmeeting integer NOT NULL,
    userid integer NOT NULL
);
    DROP TABLE public.compmeeting;
       public         heap    Bianca    false            �            1259    41037    meeting    TABLE     �  CREATE TABLE public.meeting (
    codmeeting integer NOT NULL,
    datariunione date NOT NULL,
    orainizio time without time zone NOT NULL,
    orafine time without time zone,
    piattaforma character varying(10),
    luogo character varying(10),
    codprogetto integer NOT NULL,
    CONSTRAINT luogo_riunione CHECK ((((luogo IS NOT NULL) AND (piattaforma IS NULL)) OR ((luogo IS NULL) AND (piattaforma IS NOT NULL))))
);
    DROP TABLE public.meeting;
       public         heap    Bianca    false            �            1259    82169    meeting_codmeeting_seq    SEQUENCE     �   ALTER TABLE public.meeting ALTER COLUMN codmeeting ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.meeting_codmeeting_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);
            public          Bianca    false    205            �            1259    41014    partecipante    TABLE     �  CREATE TABLE public.partecipante (
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
     DROP TABLE public.partecipante;
       public         heap    postgres    false    661            �            1259    82165    partecipante_userid_seq    SEQUENCE     �   ALTER TABLE public.partecipante ALTER COLUMN userid ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.partecipante_userid_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    204            �            1259    74028    partecipanteprog    TABLE     d   CREATE TABLE public.partecipanteprog (
    codprog integer NOT NULL,
    userid integer NOT NULL
);
 $   DROP TABLE public.partecipanteprog;
       public         heap    postgres    false            �            1259    82173    partecipanteprog_codprog_seq    SEQUENCE     �   ALTER TABLE public.partecipanteprog ALTER COLUMN codprog ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.partecipanteprog_codprog_seq
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    210            �            1259    40965    privato    TABLE     �   CREATE TABLE public.privato (
    cf character varying(16) NOT NULL,
    nome character varying(20) NOT NULL,
    cognome character varying(20) NOT NULL,
    CONSTRAINT controllocf CHECK (((cf)::text ~* '[A-Z]{6}\d{2}[A-Z]\d{2}[A-Z]\d{3}[A-Z]'::text))
);
    DROP TABLE public.privato;
       public         heap    postgres    false            �            1259    73806 
   progambito    TABLE     k   CREATE TABLE public.progambito (
    codprogetto integer NOT NULL,
    nome public.enum_ambito NOT NULL
);
    DROP TABLE public.progambito;
       public         heap    Bianca    false    657            �            1259    40984    progetto    TABLE     K  CREATE TABLE public.progetto (
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
    DROP TABLE public.progetto;
       public         heap    Bianca    false    653            �            1259    82179    progetto_codprogetto_seq    SEQUENCE     �   ALTER TABLE public.progetto ALTER COLUMN codprogetto ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.progetto_codprogetto_seq
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);
            public          Bianca    false    203            �            1259    73764    progrealizzato    TABLE     s   CREATE TABLE public.progrealizzato (
    codprog integer NOT NULL,
    tipologia public.enum_tipologia NOT NULL
);
 "   DROP TABLE public.progrealizzato;
       public         heap    Bianca    false    653            �            1259    82189    progrealizzato_codprog_seq    SEQUENCE     �   ALTER TABLE public.progrealizzato ALTER COLUMN codprog ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.progrealizzato_codprog_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);
            public          Bianca    false    206            �            1259    40970    società    TABLE     �   CREATE TABLE public."società" (
    partitaiva character varying(11) NOT NULL,
    nomesocieta character varying(25) NOT NULL
);
    DROP TABLE public."società";
       public         heap    postgres    false            &          0    73785    ambito 
   TABLE DATA           &   COPY public.ambito (nome) FROM stdin;
    public          Bianca    false    207   �r                 0    40960    azienda 
   TABLE DATA           =   COPY public.azienda (isin, nome, sedeprincipale) FROM stdin;
    public          postgres    false    200   �r       (          0    74015    compmeeting 
   TABLE DATA           9   COPY public.compmeeting (codmeeting, userid) FROM stdin;
    public          Bianca    false    209   Ns       $          0    41037    meeting 
   TABLE DATA           p   COPY public.meeting (codmeeting, datariunione, orainizio, orafine, piattaforma, luogo, codprogetto) FROM stdin;
    public          Bianca    false    205   ks       #          0    41014    partecipante 
   TABLE DATA           �   COPY public.partecipante (userid, email, pw, cf, nome, cognome, ruolo, salariomedio, valutazione, isin, codprogetto) FROM stdin;
    public          postgres    false    204   �s       )          0    74028    partecipanteprog 
   TABLE DATA           ;   COPY public.partecipanteprog (codprog, userid) FROM stdin;
    public          postgres    false    210    t                  0    40965    privato 
   TABLE DATA           4   COPY public.privato (cf, nome, cognome) FROM stdin;
    public          postgres    false    201   =t       '          0    73806 
   progambito 
   TABLE DATA           7   COPY public.progambito (codprogetto, nome) FROM stdin;
    public          Bianca    false    208   zt       "          0    40984    progetto 
   TABLE DATA           w   COPY public.progetto (codprogetto, tipologia, numeropartecipanti, budget, isin, cf, partitaiva, terminato) FROM stdin;
    public          Bianca    false    203   �t       %          0    73764    progrealizzato 
   TABLE DATA           <   COPY public.progrealizzato (codprog, tipologia) FROM stdin;
    public          Bianca    false    206   �t       !          0    40970    società 
   TABLE DATA           =   COPY public."società" (partitaiva, nomesocieta) FROM stdin;
    public          postgres    false    202   u       5           0    0    meeting_codmeeting_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.meeting_codmeeting_seq', 4, true);
          public          Bianca    false    212            6           0    0    partecipante_userid_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.partecipante_userid_seq', 4, true);
          public          postgres    false    211            7           0    0    partecipanteprog_codprog_seq    SEQUENCE SET     K   SELECT pg_catalog.setval('public.partecipanteprog_codprog_seq', 0, false);
          public          postgres    false    213            8           0    0    progetto_codprogetto_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.progetto_codprogetto_seq', 3, true);
          public          Bianca    false    214            9           0    0    progrealizzato_codprog_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('public.progrealizzato_codprog_seq', 1, false);
          public          Bianca    false    215            �           2606    73792    ambito ambito_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.ambito
    ADD CONSTRAINT ambito_pkey PRIMARY KEY (nome);
 <   ALTER TABLE ONLY public.ambito DROP CONSTRAINT ambito_pkey;
       public            Bianca    false    207            u           2606    40964    azienda azienda_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.azienda
    ADD CONSTRAINT azienda_pkey PRIMARY KEY (isin);
 >   ALTER TABLE ONLY public.azienda DROP CONSTRAINT azienda_pkey;
       public            postgres    false    200            �           2606    41041    meeting meeting_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.meeting
    ADD CONSTRAINT meeting_pkey PRIMARY KEY (codmeeting);
 >   ALTER TABLE ONLY public.meeting DROP CONSTRAINT meeting_pkey;
       public            Bianca    false    205            }           2606    73958    partecipante partecipante_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_pkey PRIMARY KEY (userid);
 H   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT partecipante_pkey;
       public            postgres    false    204            w           2606    40969    privato privato_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.privato
    ADD CONSTRAINT privato_pkey PRIMARY KEY (cf);
 >   ALTER TABLE ONLY public.privato DROP CONSTRAINT privato_pkey;
       public            postgres    false    201            {           2606    40992    progetto progetto_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_pkey PRIMARY KEY (codprogetto);
 @   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_pkey;
       public            Bianca    false    203            �           2606    73771 "   progrealizzato progrealizzato_pkey 
   CONSTRAINT     e   ALTER TABLE ONLY public.progrealizzato
    ADD CONSTRAINT progrealizzato_pkey PRIMARY KEY (codprog);
 L   ALTER TABLE ONLY public.progrealizzato DROP CONSTRAINT progrealizzato_pkey;
       public            Bianca    false    206            y           2606    40974    società societa_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public."società"
    ADD CONSTRAINT societa_pkey PRIMARY KEY (partitaiva);
 A   ALTER TABLE ONLY public."società" DROP CONSTRAINT societa_pkey;
       public            postgres    false    202                       2606    73860    partecipante unico_cf 
   CONSTRAINT     N   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT unico_cf UNIQUE (cf);
 ?   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT unico_cf;
       public            postgres    false    204            �           2606    82201    partecipante unique_account 
   CONSTRAINT     [   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT unique_account UNIQUE (cf, email);
 E   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT unique_account;
       public            postgres    false    204    204            �           2620    82199    progetto fineprogetto    TRIGGER     �   CREATE TRIGGER fineprogetto AFTER UPDATE OF terminato ON public.progetto FOR EACH ROW EXECUTE FUNCTION public.storico_progetti();
 .   DROP TRIGGER fineprogetto ON public.progetto;
       public          Bianca    false    203    219    203            �           2620    74042    meeting luogooccupato    TRIGGER     u   CREATE TRIGGER luogooccupato BEFORE INSERT ON public.meeting FOR EACH ROW EXECUTE FUNCTION public.controllo_luogo();
 .   DROP TRIGGER luogooccupato ON public.meeting;
       public          Bianca    false    221    205            �           2620    98439    meeting meeting    TRIGGER     �   CREATE TRIGGER meeting BEFORE UPDATE OF orainizio ON public.meeting FOR EACH ROW EXECUTE FUNCTION public.min_partecipanti_meeting();
 (   DROP TRIGGER meeting ON public.meeting;
       public          Bianca    false    205    205    235            �           2620    98441    meeting meeting_senza_manager    TRIGGER     �   CREATE TRIGGER meeting_senza_manager BEFORE UPDATE OF orainizio ON public.meeting FOR EACH ROW EXECUTE FUNCTION public.meeting_senza_pm();
 6   DROP TRIGGER meeting_senza_manager ON public.meeting;
       public          Bianca    false    218    205    205            �           2620    82217    compmeeting meetingnonpermesso    TRIGGER     �   CREATE TRIGGER meetingnonpermesso BEFORE INSERT ON public.compmeeting FOR EACH ROW EXECUTE FUNCTION public.check_meeting_progetto();
 7   DROP TRIGGER meetingnonpermesso ON public.compmeeting;
       public          Bianca    false    209    220            �           2620    74044    compmeeting partecipantemeeting    TRIGGER     �   CREATE TRIGGER partecipantemeeting BEFORE INSERT ON public.compmeeting FOR EACH ROW EXECUTE FUNCTION public.controllo_partecipante_meeting();
 8   DROP TRIGGER partecipantemeeting ON public.compmeeting;
       public          Bianca    false    233    209            �           2620    90253    compmeeting progettomismatch    TRIGGER     �   CREATE TRIGGER progettomismatch BEFORE INSERT ON public.compmeeting FOR EACH ROW EXECUTE FUNCTION public.check_progetto_mismatch();
 5   DROP TRIGGER progettomismatch ON public.compmeeting;
       public          Bianca    false    209    234            �           2620    73838    partecipante projectmanager    TRIGGER     t   CREATE TRIGGER projectmanager BEFORE INSERT ON public.partecipante FOR EACH ROW EXECUTE FUNCTION public.check_pm();
 4   DROP TRIGGER projectmanager ON public.partecipante;
       public          postgres    false    216    204            �           2620    82101 !   partecipante valutazioneaziendale    TRIGGER     �   CREATE TRIGGER valutazioneaziendale BEFORE UPDATE OF valutazione ON public.partecipante FOR EACH ROW EXECUTE FUNCTION public.check_valutazione();
 :   DROP TRIGGER valutazioneaziendale ON public.partecipante;
       public          postgres    false    204    217    204            �           2606    74018 '   compmeeting compmeeting_codmeeting_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.compmeeting
    ADD CONSTRAINT compmeeting_codmeeting_fkey FOREIGN KEY (codmeeting) REFERENCES public.meeting(codmeeting);
 Q   ALTER TABLE ONLY public.compmeeting DROP CONSTRAINT compmeeting_codmeeting_fkey;
       public          Bianca    false    2947    205    209            �           2606    74023 #   compmeeting compmeeting_userid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.compmeeting
    ADD CONSTRAINT compmeeting_userid_fkey FOREIGN KEY (userid) REFERENCES public.partecipante(userid);
 M   ALTER TABLE ONLY public.compmeeting DROP CONSTRAINT compmeeting_userid_fkey;
       public          Bianca    false    2941    204    209            �           2606    90246     meeting meeting_codprogetto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.meeting
    ADD CONSTRAINT meeting_codprogetto_fkey FOREIGN KEY (codprogetto) REFERENCES public.progetto(codprogetto);
 J   ALTER TABLE ONLY public.meeting DROP CONSTRAINT meeting_codprogetto_fkey;
       public          Bianca    false    205    203    2939            �           2606    82184 *   partecipante partecipante_codprogetto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_codprogetto_fkey FOREIGN KEY (codprogetto) REFERENCES public.progetto(codprogetto) ON DELETE SET NULL;
 T   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT partecipante_codprogetto_fkey;
       public          postgres    false    2939    203    204            �           2606    41032 #   partecipante partecipante_isin_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_isin_fkey FOREIGN KEY (isin) REFERENCES public.azienda(isin);
 M   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT partecipante_isin_fkey;
       public          postgres    false    200    2933    204            �           2606    74031 .   partecipanteprog partecipanteprog_codprog_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipanteprog
    ADD CONSTRAINT partecipanteprog_codprog_fkey FOREIGN KEY (codprog) REFERENCES public.progrealizzato(codprog);
 X   ALTER TABLE ONLY public.partecipanteprog DROP CONSTRAINT partecipanteprog_codprog_fkey;
       public          postgres    false    210    206    2949            �           2606    74036 -   partecipanteprog partecipanteprog_userid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipanteprog
    ADD CONSTRAINT partecipanteprog_userid_fkey FOREIGN KEY (userid) REFERENCES public.partecipante(userid);
 W   ALTER TABLE ONLY public.partecipanteprog DROP CONSTRAINT partecipanteprog_userid_fkey;
       public          postgres    false    2941    204    210            �           2606    73812 &   progambito progambito_codprogetto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.progambito
    ADD CONSTRAINT progambito_codprogetto_fkey FOREIGN KEY (codprogetto) REFERENCES public.progetto(codprogetto);
 P   ALTER TABLE ONLY public.progambito DROP CONSTRAINT progambito_codprogetto_fkey;
       public          Bianca    false    208    2939    203            �           2606    73817    progambito progambito_nome_fkey    FK CONSTRAINT     ~   ALTER TABLE ONLY public.progambito
    ADD CONSTRAINT progambito_nome_fkey FOREIGN KEY (nome) REFERENCES public.ambito(nome);
 I   ALTER TABLE ONLY public.progambito DROP CONSTRAINT progambito_nome_fkey;
       public          Bianca    false    207    208    2951            �           2606    40998    progetto progetto_cf_fkey    FK CONSTRAINT     u   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_cf_fkey FOREIGN KEY (cf) REFERENCES public.privato(cf);
 C   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_cf_fkey;
       public          Bianca    false    201    2935    203            �           2606    40993    progetto progetto_isin_fkey    FK CONSTRAINT     {   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_isin_fkey FOREIGN KEY (isin) REFERENCES public.azienda(isin);
 E   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_isin_fkey;
       public          Bianca    false    2933    203    200            �           2606    41003 !   progetto progetto_partitaiva_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_partitaiva_fkey FOREIGN KEY (partitaiva) REFERENCES public."società"(partitaiva);
 K   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_partitaiva_fkey;
       public          Bianca    false    2937    202    203            &      x������ � �         i   x��1
�@F�z��@X��!E:�i~�1qg�,��w�y���z�⃤�FEW��'�%2���]����rRx=jEl�*Gs�����X�BΠMJq���|L%"�      (      x������ � �      $   9   x�3�4202�50�52�44�20 "NC+c0#*??�3Əӈ����Bc�=... ���      #   \   x�3����(G7?gK� S7c� N����|ΨĪD΢�����Ĥ̜T�Ģ�D�̼����Ē��DN#Nc����|#N#�=... `        )      x������ � �          -   x�s��prv70p51w3���t�L�KN�t�H�HLI����� �4	�      '      x������ � �      "   I   x�3��LN-JNTH�THJ,N�44�4500�L�L�7�t��prv70p51w3�����L�2��eJXW� ��W      %      x�3�,�LN-JNTH�THJ,N����� Vjn      !   !   x�K�L�ObC΀��Ԣ���}�b���� ���     