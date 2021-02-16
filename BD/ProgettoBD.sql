PGDMP     )                    y         
   ProgettoBD    13.1    13.1 %    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16394 
   ProgettoBD    DATABASE     h   CREATE DATABASE "ProgettoBD" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Italian_Italy.1252';
    DROP DATABASE "ProgettoBD";
                postgres    false            z           1247    16402    enum_ambiti    DOMAIN     [  CREATE DOMAIN public.enum_ambiti AS character varying(50)
	CONSTRAINT enum_ambiti CHECK ((upper((VALUE)::text) = ANY (ARRAY['ECONOMIA E FINANZA'::text, 'MEDICINA'::text, 'INFORMATICA'::text, 'ALIMENTARE'::text, 'AUTOMOBILISTICA'::text, 'FARMACEUTICA'::text, 'ELETTRONICA'::text, 'MARKETING'::text, 'RICERCA E SVILUPPO'::text, 'CHIMICA'::text])));
     DROP DOMAIN public.enum_ambiti;
       public          postgres    false            v           1247    16397    enum_azienda    DOMAIN     �   CREATE DOMAIN public.enum_azienda AS character varying(8)
	CONSTRAINT enum_azienda CHECK ((upper((VALUE)::text) = ANY (ARRAY['S.P.A.'::text, 'S.S.'::text, 'S.N.C.'::text, 'S.A.S.'::text, 'S.R.L.'::text, 'S.A.P.A.'::text])));
 !   DROP DOMAIN public.enum_azienda;
       public          Bianca    false            ~           1247    16405    enum_meeting    DOMAIN     �   CREATE DOMAIN public.enum_meeting AS character varying(30)
	CONSTRAINT enum_meeting CHECK ((upper((VALUE)::text) = ANY (ARRAY['SALA RIUNIONI'::text, 'PIATTAFORMA DI VIDEOCONFERENZA'::text])));
 !   DROP DOMAIN public.enum_meeting;
       public          Bianca    false            �           1247    16411 
   enum_ruoli    DOMAIN     1  CREATE DOMAIN public.enum_ruoli AS character varying(50)
	CONSTRAINT enum_ruoli CHECK ((upper((VALUE)::text) = ANY (ARRAY['RESPONSABILE DELLA COMUNICAZIONE'::text, 'COORDINATORE DI ATTIVITÀ'::text, 'RESPONSABILE AMMINISTRAZIONE'::text, 'RESPONSABILE AREA INFORMATICA'::text, 'PROJECT MANAGER'::text])));
    DROP DOMAIN public.enum_ruoli;
       public          Bianca    false            �           1247    16408    enum_tipologie    DOMAIN     �   CREATE DOMAIN public.enum_tipologie AS character varying(25)
	CONSTRAINT enum_tipologie CHECK ((upper((VALUE)::text) = ANY (ARRAY['RICERCA DI BASE'::text, 'RICERCA INDUSTRIALE'::text, 'RICERCA SPERIMENTALE'::text, 'SVILUPPO SPERIMENTALE'::text])));
 #   DROP DOMAIN public.enum_tipologie;
       public          postgres    false            �            1255    16452 
   check_pm()    FUNCTION     2  CREATE FUNCTION public.check_pm() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE npm int;
BEGIN
	SELECT count(*) INTO npm
	FROM partecipanti
	WHERE ruolo = 'Project Manager';
	
	if npm > 0 then
		Raise 'Errore, un progetto non può avere più di un project manager';
	end if;
	return new;
END;
$$;
 !   DROP FUNCTION public.check_pm();
       public          postgres    false            �            1259    16413    azienda    TABLE     �   CREATE TABLE public.azienda (
    nome character varying(20) NOT NULL,
    formasocietaria public.enum_azienda NOT NULL,
    sedeprincipale character varying(20) NOT NULL,
    isin character varying(12) NOT NULL,
    ambito public.enum_ambiti NOT NULL
);
    DROP TABLE public.azienda;
       public         heap    Bianca    false    630    634            �            1259    24618    chiamata    TABLE     d   CREATE TABLE public.chiamata (
    codmeeting character varying(8),
    cf character varying(16)
);
    DROP TABLE public.chiamata;
       public         heap    postgres    false            �            1259    16429    cliente    TABLE     �   CREATE TABLE public.cliente (
    nome character varying(20) NOT NULL,
    cognome character varying(20) NOT NULL,
    partitaiva character varying(11) NOT NULL
);
    DROP TABLE public.cliente;
       public         heap    postgres    false            �            1259    24592    commissione    TABLE     p   CREATE TABLE public.commissione (
    partitaiva character varying(11),
    codprogetto character varying(8)
);
    DROP TABLE public.commissione;
       public         heap    postgres    false            �            1259    16434    meeting    TABLE       CREATE TABLE public.meeting (
    luogo public.enum_meeting NOT NULL,
    datariunione date NOT NULL,
    orainizio time without time zone NOT NULL,
    orafine time without time zone,
    numeropartecipanti integer NOT NULL,
    codmeeting character varying(8) NOT NULL
);
    DROP TABLE public.meeting;
       public         heap    postgres    false    638            �            1259    16442    partecipanti    TABLE     �  CREATE TABLE public.partecipanti (
    cf character varying(16) NOT NULL,
    nome character varying(20) NOT NULL,
    cognome character varying(20) NOT NULL,
    datanascita date NOT NULL,
    luogonascita character varying(20) NOT NULL,
    salariomedio numeric NOT NULL,
    valutazioneaziendale integer,
    tipologiaprogettirealizzati public.enum_tipologie,
    ruolo public.enum_ruoli NOT NULL
);
     DROP TABLE public.partecipanti;
       public         heap    Bianca    false    642    646            �            1259    16421    progetto    TABLE       CREATE TABLE public.progetto (
    tipologia public.enum_tipologie NOT NULL,
    ambito public.enum_ambiti NOT NULL,
    numeropartecipante integer NOT NULL,
    budget numeric NOT NULL,
    codprogetto character varying(8) NOT NULL,
    isin character varying(12)
);
    DROP TABLE public.progetto;
       public         heap    postgres    false    634    642            �          0    16413    azienda 
   TABLE DATA           V   COPY public.azienda (nome, formasocietaria, sedeprincipale, isin, ambito) FROM stdin;
    public          Bianca    false    200   �0       �          0    24618    chiamata 
   TABLE DATA           2   COPY public.chiamata (codmeeting, cf) FROM stdin;
    public          postgres    false    206   �0       �          0    16429    cliente 
   TABLE DATA           <   COPY public.cliente (nome, cognome, partitaiva) FROM stdin;
    public          postgres    false    202   �0       �          0    24592    commissione 
   TABLE DATA           >   COPY public.commissione (partitaiva, codprogetto) FROM stdin;
    public          postgres    false    205   �0       �          0    16434    meeting 
   TABLE DATA           j   COPY public.meeting (luogo, datariunione, orainizio, orafine, numeropartecipanti, codmeeting) FROM stdin;
    public          postgres    false    203   �0       �          0    16442    partecipanti 
   TABLE DATA           �   COPY public.partecipanti (cf, nome, cognome, datanascita, luogonascita, salariomedio, valutazioneaziendale, tipologiaprogettirealizzati, ruolo) FROM stdin;
    public          Bianca    false    204   1       �          0    16421    progetto 
   TABLE DATA           d   COPY public.progetto (tipologia, ambito, numeropartecipante, budget, codprogetto, isin) FROM stdin;
    public          postgres    false    201   x1       T           2606    16420    azienda azienda_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.azienda
    ADD CONSTRAINT azienda_pkey PRIMARY KEY (isin);
 >   ALTER TABLE ONLY public.azienda DROP CONSTRAINT azienda_pkey;
       public            Bianca    false    200            X           2606    16433    cliente cliente_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (partitaiva);
 >   ALTER TABLE ONLY public.cliente DROP CONSTRAINT cliente_pkey;
       public            postgres    false    202            Z           2606    16441    meeting meeting_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.meeting
    ADD CONSTRAINT meeting_pkey PRIMARY KEY (codmeeting);
 >   ALTER TABLE ONLY public.meeting DROP CONSTRAINT meeting_pkey;
       public            postgres    false    203            Q           2606    16450    meeting numeropartecipanti     CHECK CONSTRAINT     �   ALTER TABLE public.meeting
    ADD CONSTRAINT "numeropartecipanti " CHECK (((numeropartecipanti > 0) AND (numeropartecipanti <= 100))) NOT VALID;
 B   ALTER TABLE public.meeting DROP CONSTRAINT "numeropartecipanti ";
       public          postgres    false    203    203            \           2606    16449    partecipanti partecipanti_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.partecipanti
    ADD CONSTRAINT partecipanti_pkey PRIMARY KEY (cf);
 H   ALTER TABLE ONLY public.partecipanti DROP CONSTRAINT partecipanti_pkey;
       public            Bianca    false    204            V           2606    16428    progetto progetto_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_pkey PRIMARY KEY (codprogetto);
 @   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_pkey;
       public            postgres    false    201            R           2606    16451    partecipanti valutazione    CHECK CONSTRAINT     �   ALTER TABLE public.partecipanti
    ADD CONSTRAINT valutazione CHECK (((valutazioneaziendale >= 1) AND (valutazioneaziendale <= 5))) NOT VALID;
 =   ALTER TABLE public.partecipanti DROP CONSTRAINT valutazione;
       public          Bianca    false    204    204            b           2620    16453    partecipanti projectmanager    TRIGGER     t   CREATE TRIGGER projectmanager BEFORE INSERT ON public.partecipanti FOR EACH ROW EXECUTE FUNCTION public.check_pm();
 4   DROP TRIGGER projectmanager ON public.partecipanti;
       public          Bianca    false    207    204            a           2606    24626    chiamata chiamata_cf_fkey    FK CONSTRAINT     z   ALTER TABLE ONLY public.chiamata
    ADD CONSTRAINT chiamata_cf_fkey FOREIGN KEY (cf) REFERENCES public.partecipanti(cf);
 C   ALTER TABLE ONLY public.chiamata DROP CONSTRAINT chiamata_cf_fkey;
       public          postgres    false    206    204    2908            `           2606    24621 !   chiamata chiamata_codmeeting_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.chiamata
    ADD CONSTRAINT chiamata_codmeeting_fkey FOREIGN KEY (codmeeting) REFERENCES public.meeting(codmeeting);
 K   ALTER TABLE ONLY public.chiamata DROP CONSTRAINT chiamata_codmeeting_fkey;
       public          postgres    false    2906    206    203            _           2606    24600 (   commissione commissione_codprogetto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.commissione
    ADD CONSTRAINT commissione_codprogetto_fkey FOREIGN KEY (codprogetto) REFERENCES public.progetto(codprogetto);
 R   ALTER TABLE ONLY public.commissione DROP CONSTRAINT commissione_codprogetto_fkey;
       public          postgres    false    201    2902    205            ^           2606    24595 '   commissione commissione_partitaiva_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.commissione
    ADD CONSTRAINT commissione_partitaiva_fkey FOREIGN KEY (partitaiva) REFERENCES public.cliente(partitaiva);
 Q   ALTER TABLE ONLY public.commissione DROP CONSTRAINT commissione_partitaiva_fkey;
       public          postgres    false    205    2904    202            ]           2606    24652    progetto fk1    FK CONSTRAINT     l   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT fk1 FOREIGN KEY (isin) REFERENCES public.azienda(isin);
 6   ALTER TABLE ONLY public.progetto DROP CONSTRAINT fk1;
       public          postgres    false    2900    200    201            �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �   U   x��Q
�  ���]�����t ��(Bc���{o�M9��.lPM��W;�9:O�$y�}A��`�~��M��0ψ��s�      �      x������ � �     