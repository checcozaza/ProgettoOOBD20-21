PGDMP                  	        y           Progetto_BD    13.1    13.3 Q    <           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            =           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            >           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            ?           1262    32768    Progetto_BD    DATABASE     i   CREATE DATABASE "Progetto_BD" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Italian_Italy.1252';
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
       public          Bianca    false            �            1255    82216    check_meeting_progetto()    FUNCTION     �  CREATE FUNCTION public.check_meeting_progetto() RETURNS trigger
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
       public          Bianca    false            �            1255    90252    check_progetto_mismatch()    FUNCTION     |  CREATE FUNCTION public.check_progetto_mismatch() RETURNS trigger
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
 0   DROP FUNCTION public.check_progetto_mismatch();
       public          Bianca    false            �            1255    139542    check_valutazione()    FUNCTION     �   CREATE FUNCTION public.check_valutazione() RETURNS trigger
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
 *   DROP FUNCTION public.check_valutazione();
       public          Bianca    false            �            1255    74041    controllo_luogo()    FUNCTION     �  CREATE FUNCTION public.controllo_luogo() RETURNS trigger
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
 (   DROP FUNCTION public.controllo_luogo();
       public          Bianca    false            �            1255    98440    meeting_senza_pm()    FUNCTION     _  CREATE FUNCTION public.meeting_senza_pm() RETURNS trigger
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
 )   DROP FUNCTION public.meeting_senza_pm();
       public          Bianca    false            �            1255    98438    min_partecipanti_meeting()    FUNCTION     �  CREATE FUNCTION public.min_partecipanti_meeting() RETURNS trigger
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
 1   DROP FUNCTION public.min_partecipanti_meeting();
       public          Bianca    false            �            1255    82191    storico_progetti()    FUNCTION       CREATE FUNCTION public.storico_progetti() RETURNS trigger
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
 )   DROP FUNCTION public.storico_progetti();
       public          Bianca    false            �            1255    139501    un_meeting_alla_volta()    FUNCTION     =  CREATE FUNCTION public.un_meeting_alla_volta() RETURNS trigger
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
 .   DROP FUNCTION public.un_meeting_alla_volta();
       public          Bianca    false            �            1259    73785    ambito    TABLE     E   CREATE TABLE public.ambito (
    nome public.enum_ambito NOT NULL
);
    DROP TABLE public.ambito;
       public         heap    Bianca    false    658            �            1259    40960    azienda    TABLE     �   CREATE TABLE public.azienda (
    "PartIVA" character varying(11) NOT NULL,
    nome character varying(12) NOT NULL,
    sedeprincipale character varying(12) NOT NULL
);
    DROP TABLE public.azienda;
       public         heap    postgres    false            �            1259    74015    compmeeting    TABLE     b   CREATE TABLE public.compmeeting (
    codmeeting integer NOT NULL,
    userid integer NOT NULL
);
    DROP TABLE public.compmeeting;
       public         heap    Bianca    false            �            1259    41037    meeting    TABLE       CREATE TABLE public.meeting (
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
    DROP TABLE public.meeting;
       public         heap    Bianca    false            �            1259    82169    meeting_codmeeting_seq    SEQUENCE     �   ALTER TABLE public.meeting ALTER COLUMN codmeeting ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.meeting_codmeeting_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);
            public          Bianca    false    205            �            1259    41014    partecipante    TABLE     p  CREATE TABLE public.partecipante (
    userid integer NOT NULL,
    email character varying(30),
    pw character varying(30),
    cf character varying(16) NOT NULL,
    nome character varying(20) NOT NULL,
    cognome character varying(20) NOT NULL,
    ruolo public.enum_ruolo NOT NULL,
    salariomedio numeric NOT NULL,
    partiva character varying(11) NOT NULL,
    codprogetto integer,
    CONSTRAINT check_password CHECK (((pw)::text ~* '(?=^.{6,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$'::text)),
    CONSTRAINT controllo_cf CHECK (((cf)::text ~* '[A-Z]{6}\d{2}[A-Z]\d{2}[A-Z]\d{3}[A-Z]'::text))
);
     DROP TABLE public.partecipante;
       public         heap    postgres    false    662            �            1259    82165    partecipante_userid_seq    SEQUENCE     �   ALTER TABLE public.partecipante ALTER COLUMN userid ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.partecipante_userid_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    204            �            1259    74028    partecipanteprog    TABLE     }   CREATE TABLE public.partecipanteprog (
    codprog integer NOT NULL,
    userid integer NOT NULL,
    valutazione integer
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
            public          postgres    false    210            �            1259    139538    partecipantiliberi    VIEW     �   CREATE VIEW public.partecipantiliberi AS
 SELECT partecipante.userid
   FROM public.partecipante
  WHERE (partecipante.codprogetto IS NULL);
 %   DROP VIEW public.partecipantiliberi;
       public          Bianca    false    204    204            �            1259    40965    privato    TABLE     �   CREATE TABLE public.privato (
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
       public         heap    Bianca    false    658            �            1259    40984    progetto    TABLE     N  CREATE TABLE public.progetto (
    codprogetto integer NOT NULL,
    tipologia public.enum_tipologia NOT NULL,
    numeropartecipanti integer NOT NULL,
    budget numeric NOT NULL,
    partiva character varying(11) NOT NULL,
    cf character varying(16),
    partitaiva character varying(11),
    terminato boolean DEFAULT false NOT NULL,
    CONSTRAINT progetto_numeropartecipanti_check CHECK (((numeropartecipanti > 0) AND (numeropartecipanti <= 30))),
    CONSTRAINT vincolo_cliente CHECK ((((cf IS NOT NULL) AND (partitaiva IS NULL)) OR ((cf IS NULL) AND (partitaiva IS NOT NULL))))
);
    DROP TABLE public.progetto;
       public         heap    Bianca    false    654            �            1259    82179    progetto_codprogetto_seq    SEQUENCE     �   ALTER TABLE public.progetto ALTER COLUMN codprogetto ADD GENERATED BY DEFAULT AS IDENTITY (
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
       public         heap    Bianca    false    654            �            1259    82189    progrealizzato_codprog_seq    SEQUENCE     �   ALTER TABLE public.progrealizzato ALTER COLUMN codprog ADD GENERATED BY DEFAULT AS IDENTITY (
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
       public         heap    postgres    false            1          0    73785    ambito 
   TABLE DATA                 public          Bianca    false    207   s       *          0    40960    azienda 
   TABLE DATA                 public          postgres    false    200   -s       3          0    74015    compmeeting 
   TABLE DATA                 public          Bianca    false    209   �s       /          0    41037    meeting 
   TABLE DATA                 public          Bianca    false    205   Ot       .          0    41014    partecipante 
   TABLE DATA                 public          postgres    false    204   0u       4          0    74028    partecipanteprog 
   TABLE DATA                 public          postgres    false    210   ew       +          0    40965    privato 
   TABLE DATA                 public          postgres    false    201   �w       2          0    73806 
   progambito 
   TABLE DATA                 public          Bianca    false    208   lx       -          0    40984    progetto 
   TABLE DATA                 public          Bianca    false    203   �x       0          0    73764    progrealizzato 
   TABLE DATA                 public          Bianca    false    206   �y       ,          0    40970    società 
   TABLE DATA                 public          postgres    false    202   	z       @           0    0    meeting_codmeeting_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.meeting_codmeeting_seq', 41, true);
          public          Bianca    false    212            A           0    0    partecipante_userid_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.partecipante_userid_seq', 5, true);
          public          postgres    false    211            B           0    0    partecipanteprog_codprog_seq    SEQUENCE SET     K   SELECT pg_catalog.setval('public.partecipanteprog_codprog_seq', 0, false);
          public          postgres    false    213            C           0    0    progetto_codprogetto_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.progetto_codprogetto_seq', 12, true);
          public          Bianca    false    214            D           0    0    progrealizzato_codprog_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('public.progrealizzato_codprog_seq', 1, false);
          public          Bianca    false    215            �           2606    73792    ambito ambito_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.ambito
    ADD CONSTRAINT ambito_pkey PRIMARY KEY (nome);
 <   ALTER TABLE ONLY public.ambito DROP CONSTRAINT ambito_pkey;
       public            Bianca    false    207            }           2606    106631    azienda azienda_pkey 
   CONSTRAINT     Y   ALTER TABLE ONLY public.azienda
    ADD CONSTRAINT azienda_pkey PRIMARY KEY ("PartIVA");
 >   ALTER TABLE ONLY public.azienda DROP CONSTRAINT azienda_pkey;
       public            postgres    false    200            �           2606    41041    meeting meeting_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.meeting
    ADD CONSTRAINT meeting_pkey PRIMARY KEY (codmeeting);
 >   ALTER TABLE ONLY public.meeting DROP CONSTRAINT meeting_pkey;
       public            Bianca    false    205            �           2606    73958    partecipante partecipante_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_pkey PRIMARY KEY (userid);
 H   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT partecipante_pkey;
       public            postgres    false    204                       2606    40969    privato privato_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.privato
    ADD CONSTRAINT privato_pkey PRIMARY KEY (cf);
 >   ALTER TABLE ONLY public.privato DROP CONSTRAINT privato_pkey;
       public            postgres    false    201            �           2606    40992    progetto progetto_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_pkey PRIMARY KEY (codprogetto);
 @   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_pkey;
       public            Bianca    false    203            �           2606    73771 "   progrealizzato progrealizzato_pkey 
   CONSTRAINT     e   ALTER TABLE ONLY public.progrealizzato
    ADD CONSTRAINT progrealizzato_pkey PRIMARY KEY (codprog);
 L   ALTER TABLE ONLY public.progrealizzato DROP CONSTRAINT progrealizzato_pkey;
       public            Bianca    false    206            �           2606    40974    società societa_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public."società"
    ADD CONSTRAINT societa_pkey PRIMARY KEY (partitaiva);
 A   ALTER TABLE ONLY public."società" DROP CONSTRAINT societa_pkey;
       public            postgres    false    202            �           2606    73860    partecipante unico_cf 
   CONSTRAINT     N   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT unico_cf UNIQUE (cf);
 ?   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT unico_cf;
       public            postgres    false    204            �           2606    82201    partecipante unique_account 
   CONSTRAINT     [   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT unique_account UNIQUE (cf, email);
 E   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT unique_account;
       public            postgres    false    204    204            �           2606    139504 '   compmeeting unique_partecipante_meeting 
   CONSTRAINT     p   ALTER TABLE ONLY public.compmeeting
    ADD CONSTRAINT unique_partecipante_meeting UNIQUE (codmeeting, userid);
 Q   ALTER TABLE ONLY public.compmeeting DROP CONSTRAINT unique_partecipante_meeting;
       public            Bianca    false    209    209            �           2620    139524    meeting composizionemeeting    TRIGGER     �   CREATE TRIGGER composizionemeeting BEFORE UPDATE OF iniziato ON public.meeting FOR EACH ROW EXECUTE FUNCTION public.min_partecipanti_meeting();
 4   DROP TRIGGER composizionemeeting ON public.meeting;
       public          Bianca    false    235    205    205            �           2620    82199    progetto fineprogetto    TRIGGER     �   CREATE TRIGGER fineprogetto AFTER UPDATE OF terminato ON public.progetto FOR EACH ROW EXECUTE FUNCTION public.storico_progetti();
 .   DROP TRIGGER fineprogetto ON public.progetto;
       public          Bianca    false    203    203    236            �           2620    74042    meeting luogooccupato    TRIGGER     u   CREATE TRIGGER luogooccupato BEFORE INSERT ON public.meeting FOR EACH ROW EXECUTE FUNCTION public.controllo_luogo();
 .   DROP TRIGGER luogooccupato ON public.meeting;
       public          Bianca    false    205    223            �           2620    139522    meeting meeting_senza_manager    TRIGGER     �   CREATE TRIGGER meeting_senza_manager BEFORE UPDATE OF iniziato ON public.meeting FOR EACH ROW EXECUTE FUNCTION public.meeting_senza_pm();
 6   DROP TRIGGER meeting_senza_manager ON public.meeting;
       public          Bianca    false    205    220    205            �           2620    82217    compmeeting meetingnonpermesso    TRIGGER     �   CREATE TRIGGER meetingnonpermesso BEFORE INSERT ON public.compmeeting FOR EACH ROW EXECUTE FUNCTION public.check_meeting_progetto();
 7   DROP TRIGGER meetingnonpermesso ON public.compmeeting;
       public          Bianca    false    222    209            �           2620    139502    meeting onemeeting    TRIGGER     x   CREATE TRIGGER onemeeting BEFORE INSERT ON public.meeting FOR EACH ROW EXECUTE FUNCTION public.un_meeting_alla_volta();
 +   DROP TRIGGER onemeeting ON public.meeting;
       public          Bianca    false    219    205            �           2620    90253    compmeeting progettomismatch    TRIGGER     �   CREATE TRIGGER progettomismatch BEFORE INSERT ON public.compmeeting FOR EACH ROW EXECUTE FUNCTION public.check_progetto_mismatch();
 5   DROP TRIGGER progettomismatch ON public.compmeeting;
       public          Bianca    false    209    218            �           2620    73838    partecipante projectmanager    TRIGGER     t   CREATE TRIGGER projectmanager BEFORE INSERT ON public.partecipante FOR EACH ROW EXECUTE FUNCTION public.check_pm();
 4   DROP TRIGGER projectmanager ON public.partecipante;
       public          postgres    false    217    204            �           2620    139546 %   partecipanteprog valutazioneaziendale    TRIGGER     �   CREATE TRIGGER valutazioneaziendale BEFORE UPDATE OF valutazione ON public.partecipanteprog FOR EACH ROW EXECUTE FUNCTION public.check_valutazione();
 >   DROP TRIGGER valutazioneaziendale ON public.partecipanteprog;
       public          postgres    false    221    210    210            �           2606    74018 '   compmeeting compmeeting_codmeeting_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.compmeeting
    ADD CONSTRAINT compmeeting_codmeeting_fkey FOREIGN KEY (codmeeting) REFERENCES public.meeting(codmeeting);
 Q   ALTER TABLE ONLY public.compmeeting DROP CONSTRAINT compmeeting_codmeeting_fkey;
       public          Bianca    false    209    2955    205            �           2606    74023 #   compmeeting compmeeting_userid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.compmeeting
    ADD CONSTRAINT compmeeting_userid_fkey FOREIGN KEY (userid) REFERENCES public.partecipante(userid);
 M   ALTER TABLE ONLY public.compmeeting DROP CONSTRAINT compmeeting_userid_fkey;
       public          Bianca    false    204    2949    209            �           2606    90246     meeting meeting_codprogetto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.meeting
    ADD CONSTRAINT meeting_codprogetto_fkey FOREIGN KEY (codprogetto) REFERENCES public.progetto(codprogetto);
 J   ALTER TABLE ONLY public.meeting DROP CONSTRAINT meeting_codprogetto_fkey;
       public          Bianca    false    203    205    2947            �           2606    82184 *   partecipante partecipante_codprogetto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_codprogetto_fkey FOREIGN KEY (codprogetto) REFERENCES public.progetto(codprogetto) ON DELETE SET NULL;
 T   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT partecipante_codprogetto_fkey;
       public          postgres    false    2947    203    204            �           2606    106646 &   partecipante partecipante_partiva_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_partiva_fkey FOREIGN KEY (partiva) REFERENCES public.azienda("PartIVA");
 P   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT partecipante_partiva_fkey;
       public          postgres    false    2941    204    200            �           2606    74031 .   partecipanteprog partecipanteprog_codprog_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipanteprog
    ADD CONSTRAINT partecipanteprog_codprog_fkey FOREIGN KEY (codprog) REFERENCES public.progrealizzato(codprog);
 X   ALTER TABLE ONLY public.partecipanteprog DROP CONSTRAINT partecipanteprog_codprog_fkey;
       public          postgres    false    206    2957    210            �           2606    74036 -   partecipanteprog partecipanteprog_userid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipanteprog
    ADD CONSTRAINT partecipanteprog_userid_fkey FOREIGN KEY (userid) REFERENCES public.partecipante(userid);
 W   ALTER TABLE ONLY public.partecipanteprog DROP CONSTRAINT partecipanteprog_userid_fkey;
       public          postgres    false    2949    204    210            �           2606    73812 &   progambito progambito_codprogetto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.progambito
    ADD CONSTRAINT progambito_codprogetto_fkey FOREIGN KEY (codprogetto) REFERENCES public.progetto(codprogetto);
 P   ALTER TABLE ONLY public.progambito DROP CONSTRAINT progambito_codprogetto_fkey;
       public          Bianca    false    2947    203    208            �           2606    73817    progambito progambito_nome_fkey    FK CONSTRAINT     ~   ALTER TABLE ONLY public.progambito
    ADD CONSTRAINT progambito_nome_fkey FOREIGN KEY (nome) REFERENCES public.ambito(nome);
 I   ALTER TABLE ONLY public.progambito DROP CONSTRAINT progambito_nome_fkey;
       public          Bianca    false    208    207    2959            �           2606    40998    progetto progetto_cf_fkey    FK CONSTRAINT     u   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_cf_fkey FOREIGN KEY (cf) REFERENCES public.privato(cf);
 C   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_cf_fkey;
       public          Bianca    false    2943    201    203            �           2606    41003 !   progetto progetto_partitaiva_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_partitaiva_fkey FOREIGN KEY (partitaiva) REFERENCES public."società"(partitaiva);
 K   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_partitaiva_fkey;
       public          Bianca    false    2945    202    203            �           2606    106660    progetto progetto_partiva_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_partiva_fkey FOREIGN KEY (partiva) REFERENCES public.azienda("PartIVA");
 H   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_partiva_fkey;
       public          Bianca    false    200    203    2941            1   
   x���          *   �   x���O�@G�~�ŋ
��u2��D������#���c��rN�wy&˫��E��71�[��
&����1k?���NY���&���J���wA,���D�)���
����9�����Q��%X��eyу3����4XZ��c��衑�n���Y~�-�sf��N�-��5+�D      3   S   x���v
Q���W((M��L�K��-�MM-��KW�H�O��uJ�S�2S4�}B]�4Lu�5��<)4Ä
fX ��� �!@�      /   �   x�͏M�@�����Y����q��A���m2d��z�_�d�A�n��<�3�DI��f%���PR>�(e�*`����`CmE�Rt�Tѝt�'z�jBc�
([]�?ԍ.�1�|��cد��:�Q�
�=鹎_ڜd�����y���V+�d�|���U��xiEC��/��f��+?�ҰS,\��@��o���鏐e= }��U      .   %  x�͔A��0���+zs&aLA�^VG��q@=x{Bu���-e6����?�-dw5{U���)|���"N�d�����}���	��;W=��,�-�:��Pv�%Ց8v��E!,TA��L���l�>�,�OR�R�	m�d��ѶPo����;��k��r��5g�L������vC3��I��q4�g~l>����N&\I�f
-�ÑJ�r0�z>:9x����B񆐿�yI��X�aox��niv3a�ZHfV'�+س��qY2�*%�a������HS��w�Cכ�Aj�"޴SV|���)���]PBR4eh��'�����t�rt�M��������A��X˹�R;ką�)-
@�(u!d��:��{�V�q�h��n��u5)���%ک-iG�����MQ�3�!�2�8����äE�K�k? 
c�Z%��!KP�m[vpw������~\�˺�f�xv=d���x���`טOLn&u_�U�^�4W�u]mҷ=��|B���x���q�o���GE��U��˪�b�js5���i%Y      4   [   x���v
Q���W((M��L�+H,*IM�,H�+I-(�OW�H�O1tJ�S�2St�sJK�2��R5�}B]�4u��XӚ�� 2�      +   �   x���v
Q���W((M��L�+(�,K,�W�HN�Q���M�QH�O14�}B]�4ԝ=<���\M���--C�uԝ2�A,�ԌĔTuMk.O2-��rt�s�420u�0��V� �89ĉJ�JY�� ��9e      2   
   x���          -   �   x�͑Kk�0���{s�(~�1=%�n�'�!7EVm	Y���%���.�af�[���9@Y^@�S��Aiy�HXp�}�����BKŴ8��	�)^]��?�a��=�Ȍ\��j�=,B�F.4g0)�q�a��	��Y�RW������v���<J�$M[��mU8�~�'��K��]8]��J�_P���*����MQgi�иX�i���±�����<:�H�]H���}�#      0   p   x���v
Q���W((M��L�+(�O/JM�ɬ�J,�W�H�O	�(�d���g&j*�9���+h�(�e&�%'*�d*$%��kZsyRf����y)��%E��9`��� �u:�      ,   _   x���v
Q���W((M��L�S*�O�L-9�@IA� ��$�$1�,QG!/?7"��������������:
��e�E@���>��\\\ }�     