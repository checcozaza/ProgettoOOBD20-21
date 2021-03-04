PGDMP     ,                     y            Progetto_BD    13.1    13.1 7               0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    32768    Progetto_BD    DATABASE     i   CREATE DATABASE "Progetto_BD" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Italian_Italy.1252';
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
       public          Bianca    false            �            1259    73785    ambito    TABLE     E   CREATE TABLE public.ambito (
    nome public.enum_ambito NOT NULL
);
    DROP TABLE public.ambito;
       public         heap    Bianca    false    649            �            1259    40960    azienda    TABLE     �   CREATE TABLE public.azienda (
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
    CONSTRAINT luogo_riunione CHECK ((((luogo IS NOT NULL) AND (piattaforma IS NULL)) OR ((luogo IS NULL) AND (piattaforma IS NOT NULL))))
);
    DROP TABLE public.meeting;
       public         heap    Bianca    false            �            1259    41014    partecipante    TABLE       CREATE TABLE public.partecipante (
    userid integer NOT NULL,
    email character varying(30),
    pw character varying(30),
    cf character varying(16) NOT NULL,
    nome character varying(20) NOT NULL,
    cognome character varying(20) NOT NULL,
    ruolo public.enum_ruolo NOT NULL,
    salariomedio numeric NOT NULL,
    valutazio integer,
    codprogetto integer NOT NULL,
    isin character varying(12) NOT NULL,
    CONSTRAINT controllo_cf CHECK (((cf)::text ~* '[A-Z]{6}\d{2}[A-Z]\d{2}[A-Z]\d{3}[A-Z]'::text))
);
     DROP TABLE public.partecipante;
       public         heap    postgres    false    653            �            1259    74028    partecipanteprog    TABLE     d   CREATE TABLE public.partecipanteprog (
    codprog integer NOT NULL,
    userid integer NOT NULL
);
 $   DROP TABLE public.partecipanteprog;
       public         heap    postgres    false            �            1259    40965    privato    TABLE     �   CREATE TABLE public.privato (
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
       public         heap    Bianca    false    649            �            1259    40984    progetto    TABLE     �  CREATE TABLE public.progetto (
    codprogetto integer NOT NULL,
    tipologia public.enum_tipologia NOT NULL,
    numeropartecipanti integer NOT NULL,
    budget numeric NOT NULL,
    isin character varying(12) NOT NULL,
    cf character varying(16),
    partitaiva character varying(11),
    CONSTRAINT progetto_numeropartecipanti_check CHECK (((numeropartecipanti > 0) AND (numeropartecipanti <= 30)))
);
    DROP TABLE public.progetto;
       public         heap    Bianca    false    645            �            1259    73764    progrealizzato    TABLE     s   CREATE TABLE public.progrealizzato (
    codprog integer NOT NULL,
    tipologia public.enum_tipologia NOT NULL
);
 "   DROP TABLE public.progrealizzato;
       public         heap    Bianca    false    645            �            1259    40970    societa    TABLE        CREATE TABLE public.societa (
    partitaiva character varying(11) NOT NULL,
    nomesocieta character varying(25) NOT NULL
);
    DROP TABLE public.societa;
       public         heap    postgres    false            	          0    73785    ambito 
   TABLE DATA           &   COPY public.ambito (nome) FROM stdin;
    public          Bianca    false    207   �I                 0    40960    azienda 
   TABLE DATA           =   COPY public.azienda (isin, nome, sedeprincipale) FROM stdin;
    public          postgres    false    200   J                 0    74015    compmeeting 
   TABLE DATA           9   COPY public.compmeeting (codmeeting, userid) FROM stdin;
    public          Bianca    false    209   _J                 0    41037    meeting 
   TABLE DATA           c   COPY public.meeting (codmeeting, datariunione, orainizio, orafine, piattaforma, luogo) FROM stdin;
    public          Bianca    false    205   �J                 0    41014    partecipante 
   TABLE DATA              COPY public.partecipante (userid, email, pw, cf, nome, cognome, ruolo, salariomedio, valutazio, codprogetto, isin) FROM stdin;
    public          postgres    false    204   �J                 0    74028    partecipanteprog 
   TABLE DATA           ;   COPY public.partecipanteprog (codprog, userid) FROM stdin;
    public          postgres    false    210   BK                 0    40965    privato 
   TABLE DATA           4   COPY public.privato (cf, nome, cognome) FROM stdin;
    public          postgres    false    201   _K       
          0    73806 
   progambito 
   TABLE DATA           7   COPY public.progambito (codprogetto, nome) FROM stdin;
    public          Bianca    false    208   |K                 0    40984    progetto 
   TABLE DATA           l   COPY public.progetto (codprogetto, tipologia, numeropartecipanti, budget, isin, cf, partitaiva) FROM stdin;
    public          Bianca    false    203   �K                 0    73764    progrealizzato 
   TABLE DATA           <   COPY public.progrealizzato (codprog, tipologia) FROM stdin;
    public          Bianca    false    206   �K                 0    40970    societa 
   TABLE DATA           :   COPY public.societa (partitaiva, nomesocieta) FROM stdin;
    public          postgres    false    202   L       q           2606    73792    ambito ambito_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.ambito
    ADD CONSTRAINT ambito_pkey PRIMARY KEY (nome);
 <   ALTER TABLE ONLY public.ambito DROP CONSTRAINT ambito_pkey;
       public            Bianca    false    207            a           2606    40964    azienda azienda_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.azienda
    ADD CONSTRAINT azienda_pkey PRIMARY KEY (isin);
 >   ALTER TABLE ONLY public.azienda DROP CONSTRAINT azienda_pkey;
       public            postgres    false    200            m           2606    41041    meeting meeting_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.meeting
    ADD CONSTRAINT meeting_pkey PRIMARY KEY (codmeeting);
 >   ALTER TABLE ONLY public.meeting DROP CONSTRAINT meeting_pkey;
       public            Bianca    false    205            i           2606    73958    partecipante partecipante_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_pkey PRIMARY KEY (userid);
 H   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT partecipante_pkey;
       public            postgres    false    204            c           2606    40969    privato privato_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.privato
    ADD CONSTRAINT privato_pkey PRIMARY KEY (cf);
 >   ALTER TABLE ONLY public.privato DROP CONSTRAINT privato_pkey;
       public            postgres    false    201            g           2606    40992    progetto progetto_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_pkey PRIMARY KEY (codprogetto);
 @   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_pkey;
       public            Bianca    false    203            o           2606    73771 "   progrealizzato progrealizzato_pkey 
   CONSTRAINT     e   ALTER TABLE ONLY public.progrealizzato
    ADD CONSTRAINT progrealizzato_pkey PRIMARY KEY (codprog);
 L   ALTER TABLE ONLY public.progrealizzato DROP CONSTRAINT progrealizzato_pkey;
       public            Bianca    false    206            e           2606    40974    societa societa_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.societa
    ADD CONSTRAINT societa_pkey PRIMARY KEY (partitaiva);
 >   ALTER TABLE ONLY public.societa DROP CONSTRAINT societa_pkey;
       public            postgres    false    202            k           2606    73860    partecipante unico_cf 
   CONSTRAINT     N   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT unico_cf UNIQUE (cf);
 ?   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT unico_cf;
       public            postgres    false    204            ~           2620    74042    meeting luogooccupato    TRIGGER     u   CREATE TRIGGER luogooccupato BEFORE INSERT ON public.meeting FOR EACH ROW EXECUTE FUNCTION public.controllo_luogo();
 .   DROP TRIGGER luogooccupato ON public.meeting;
       public          Bianca    false    205    212                       2620    74044    compmeeting partecipantemeeting    TRIGGER     �   CREATE TRIGGER partecipantemeeting BEFORE INSERT ON public.compmeeting FOR EACH ROW EXECUTE FUNCTION public.controllo_partecipante_meeting();
 8   DROP TRIGGER partecipantemeeting ON public.compmeeting;
       public          Bianca    false    224    209            }           2620    73838    partecipante projectmanager    TRIGGER     t   CREATE TRIGGER projectmanager BEFORE INSERT ON public.partecipante FOR EACH ROW EXECUTE FUNCTION public.check_pm();
 4   DROP TRIGGER projectmanager ON public.partecipante;
       public          postgres    false    211    204            y           2606    74018 '   compmeeting compmeeting_codmeeting_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.compmeeting
    ADD CONSTRAINT compmeeting_codmeeting_fkey FOREIGN KEY (codmeeting) REFERENCES public.meeting(codmeeting);
 Q   ALTER TABLE ONLY public.compmeeting DROP CONSTRAINT compmeeting_codmeeting_fkey;
       public          Bianca    false    209    205    2925            z           2606    74023 #   compmeeting compmeeting_userid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.compmeeting
    ADD CONSTRAINT compmeeting_userid_fkey FOREIGN KEY (userid) REFERENCES public.partecipante(userid);
 M   ALTER TABLE ONLY public.compmeeting DROP CONSTRAINT compmeeting_userid_fkey;
       public          Bianca    false    2921    209    204            u           2606    41022 *   partecipante partecipante_codprogetto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_codprogetto_fkey FOREIGN KEY (codprogetto) REFERENCES public.progetto(codprogetto);
 T   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT partecipante_codprogetto_fkey;
       public          postgres    false    203    204    2919            v           2606    41032 #   partecipante partecipante_isin_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_isin_fkey FOREIGN KEY (isin) REFERENCES public.azienda(isin);
 M   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT partecipante_isin_fkey;
       public          postgres    false    204    200    2913            {           2606    74031 .   partecipanteprog partecipanteprog_codprog_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipanteprog
    ADD CONSTRAINT partecipanteprog_codprog_fkey FOREIGN KEY (codprog) REFERENCES public.progrealizzato(codprog);
 X   ALTER TABLE ONLY public.partecipanteprog DROP CONSTRAINT partecipanteprog_codprog_fkey;
       public          postgres    false    206    210    2927            |           2606    74036 -   partecipanteprog partecipanteprog_userid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipanteprog
    ADD CONSTRAINT partecipanteprog_userid_fkey FOREIGN KEY (userid) REFERENCES public.partecipante(userid);
 W   ALTER TABLE ONLY public.partecipanteprog DROP CONSTRAINT partecipanteprog_userid_fkey;
       public          postgres    false    204    210    2921            w           2606    73812 &   progambito progambito_codprogetto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.progambito
    ADD CONSTRAINT progambito_codprogetto_fkey FOREIGN KEY (codprogetto) REFERENCES public.progetto(codprogetto);
 P   ALTER TABLE ONLY public.progambito DROP CONSTRAINT progambito_codprogetto_fkey;
       public          Bianca    false    208    2919    203            x           2606    73817    progambito progambito_nome_fkey    FK CONSTRAINT     ~   ALTER TABLE ONLY public.progambito
    ADD CONSTRAINT progambito_nome_fkey FOREIGN KEY (nome) REFERENCES public.ambito(nome);
 I   ALTER TABLE ONLY public.progambito DROP CONSTRAINT progambito_nome_fkey;
       public          Bianca    false    208    207    2929            s           2606    40998    progetto progetto_cf_fkey    FK CONSTRAINT     u   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_cf_fkey FOREIGN KEY (cf) REFERENCES public.privato(cf);
 C   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_cf_fkey;
       public          Bianca    false    201    203    2915            r           2606    40993    progetto progetto_isin_fkey    FK CONSTRAINT     {   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_isin_fkey FOREIGN KEY (isin) REFERENCES public.azienda(isin);
 E   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_isin_fkey;
       public          Bianca    false    200    2913    203            t           2606    41003 !   progetto progetto_partitaiva_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_partitaiva_fkey FOREIGN KEY (partitaiva) REFERENCES public.societa(partitaiva);
 K   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_partitaiva_fkey;
       public          Bianca    false    202    203    2917            	      x������ � �         C   x�srv�2���K�IM���LTp��L�L�J�L�KN4����MMO�K-�L���K,�������� <��            x�3�45�21z\\\ J         P   x�3426�4202�50�50�4��20 "��Ĉ��tN,N�2��� �,�41'31/���������s2�b����  �         O   x�35��!g'gwWs7sK�PN��ļ�DN�ԌĔT΀�������ļ���"N 442�L+3����� =3�            x������ � �            x������ � �      
      x������ � �         @   x�3��LN-JNTH�THJ,N�42�44 N'g�(C�? �242ƦЀ3)31/9�,F��� �"(            x������ � �            x������ � �     