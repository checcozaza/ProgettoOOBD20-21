PGDMP                         y            Progetto_BD    13.1    13.1 #    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    32768    Progetto_BD    DATABASE     i   CREATE DATABASE "Progetto_BD" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Italian_Italy.1252';
    DROP DATABASE "Progetto_BD";
                postgres    false            �           1247    40979    enum_ambito    DOMAIN     [  CREATE DOMAIN public.enum_ambito AS character varying(50)
	CONSTRAINT enum_ambito CHECK ((upper((VALUE)::text) = ANY (ARRAY['ECONOMIA E FINANZA'::text, 'MEDICINA'::text, 'INFORMATICA'::text, 'ALIMENTARE'::text, 'AUTOMOBILISTICA'::text, 'FARMACEUTICA'::text, 'ELETTRONICA'::text, 'MARKETING'::text, 'RICERCA E SVILUPPO'::text, 'CHIMICA'::text])));
     DROP DOMAIN public.enum_ambito;
       public          postgres    false            �           1247    40982 
   enum_ruolo    DOMAIN     1  CREATE DOMAIN public.enum_ruolo AS character varying(50)
	CONSTRAINT enum_ruolo CHECK ((upper((VALUE)::text) = ANY (ARRAY['RESPONSABILE DELLA COMUNICAZIONE'::text, 'COORDINATORE DI ATTIVITÀ'::text, 'RESPONSABILE AMMINISTRAZIONE'::text, 'RESPONSABILE AREA INFORMATICA'::text, 'PROJECT MANAGER'::text])));
    DROP DOMAIN public.enum_ruolo;
       public          Bianca    false            ~           1247    40976    enum_tipologia    DOMAIN     �   CREATE DOMAIN public.enum_tipologia AS character varying(50)
	CONSTRAINT enum_tipologia CHECK ((upper((VALUE)::text) = ANY (ARRAY['RICERCA DI BASE'::text, 'RICERCA INDUSTRIALE'::text, 'RICERCA SPERIMENTALE'::text, 'SVILUPPO SPERIMENTALE'::text])));
 #   DROP DOMAIN public.enum_tipologia;
       public          Bianca    false            �            1259    40960    azienda    TABLE     �   CREATE TABLE public.azienda (
    isin character varying(12) NOT NULL,
    nome character varying(12) NOT NULL,
    sedeprincipale character varying(12) NOT NULL
);
    DROP TABLE public.azienda;
       public         heap    postgres    false            �            1259    41008    conferencecall    TABLE       CREATE TABLE public.conferencecall (
    codconference integer NOT NULL,
    luogo character varying(10),
    numeropartecipanti integer NOT NULL,
    piattaforma character varying(10),
    CONSTRAINT riunione CHECK (((luogo IS NOT NULL) OR (piattaforma IS NOT NULL)))
);
 "   DROP TABLE public.conferencecall;
       public         heap    Bianca    false            �            1259    41037    meeting    TABLE     �   CREATE TABLE public.meeting (
    codmeeting integer NOT NULL,
    datariunione date NOT NULL,
    orainizio time without time zone NOT NULL,
    orafine time without time zone NOT NULL,
    codconference integer
);
    DROP TABLE public.meeting;
       public         heap    Bianca    false            �            1259    41014    partecipante    TABLE     �  CREATE TABLE public.partecipante (
    username integer NOT NULL,
    email character varying(30),
    pw character varying(30),
    cf character varying(16) NOT NULL,
    nome character varying(20) NOT NULL,
    cognome character varying(20) NOT NULL,
    ruolo public.enum_ruolo NOT NULL,
    salariomedio numeric NOT NULL,
    valutazio integer,
    codprogetto integer,
    codconference integer,
    isin character varying(12)
);
     DROP TABLE public.partecipante;
       public         heap    postgres    false    646            �            1259    40965    privato    TABLE     �   CREATE TABLE public.privato (
    cf character varying(16) NOT NULL,
    nome character varying(20) NOT NULL,
    cognome character varying(20) NOT NULL
);
    DROP TABLE public.privato;
       public         heap    postgres    false            �            1259    40984    progetto    TABLE     �  CREATE TABLE public.progetto (
    codprogetto integer NOT NULL,
    tipologia public.enum_tipologia NOT NULL,
    numeropartecipanti integer NOT NULL,
    budget numeric NOT NULL,
    isin character varying(12),
    cf character varying(16),
    partitaiva character varying(11),
    CONSTRAINT progetto_numeropartecipanti_check CHECK (((numeropartecipanti > 0) AND (numeropartecipanti <= 30)))
);
    DROP TABLE public.progetto;
       public         heap    Bianca    false    638            �            1259    40970    societa    TABLE        CREATE TABLE public.societa (
    partitaiva character varying(11) NOT NULL,
    nomesocieta character varying(25) NOT NULL
);
    DROP TABLE public.societa;
       public         heap    postgres    false            �          0    40960    azienda 
   TABLE DATA           =   COPY public.azienda (isin, nome, sedeprincipale) FROM stdin;
    public          postgres    false    200   
.       �          0    41008    conferencecall 
   TABLE DATA           _   COPY public.conferencecall (codconference, luogo, numeropartecipanti, piattaforma) FROM stdin;
    public          Bianca    false    204   '.       �          0    41037    meeting 
   TABLE DATA           ^   COPY public.meeting (codmeeting, datariunione, orainizio, orafine, codconference) FROM stdin;
    public          Bianca    false    206   D.       �          0    41014    partecipante 
   TABLE DATA           �   COPY public.partecipante (username, email, pw, cf, nome, cognome, ruolo, salariomedio, valutazio, codprogetto, codconference, isin) FROM stdin;
    public          postgres    false    205   a.       �          0    40965    privato 
   TABLE DATA           4   COPY public.privato (cf, nome, cognome) FROM stdin;
    public          postgres    false    201   ~.       �          0    40984    progetto 
   TABLE DATA           l   COPY public.progetto (codprogetto, tipologia, numeropartecipanti, budget, isin, cf, partitaiva) FROM stdin;
    public          Bianca    false    203   �.       �          0    40970    societa 
   TABLE DATA           :   COPY public.societa (partitaiva, nomesocieta) FROM stdin;
    public          postgres    false    202   �.       I           2606    40964    azienda azienda_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.azienda
    ADD CONSTRAINT azienda_pkey PRIMARY KEY (isin);
 >   ALTER TABLE ONLY public.azienda DROP CONSTRAINT azienda_pkey;
       public            postgres    false    200            Q           2606    41013 "   conferencecall conferencecall_pkey 
   CONSTRAINT     k   ALTER TABLE ONLY public.conferencecall
    ADD CONSTRAINT conferencecall_pkey PRIMARY KEY (codconference);
 L   ALTER TABLE ONLY public.conferencecall DROP CONSTRAINT conferencecall_pkey;
       public            Bianca    false    204            U           2606    41041    meeting meeting_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.meeting
    ADD CONSTRAINT meeting_pkey PRIMARY KEY (codmeeting);
 >   ALTER TABLE ONLY public.meeting DROP CONSTRAINT meeting_pkey;
       public            Bianca    false    206            S           2606    41021    partecipante partecipante_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_pkey PRIMARY KEY (username);
 H   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT partecipante_pkey;
       public            postgres    false    205            K           2606    40969    privato privato_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.privato
    ADD CONSTRAINT privato_pkey PRIMARY KEY (cf);
 >   ALTER TABLE ONLY public.privato DROP CONSTRAINT privato_pkey;
       public            postgres    false    201            O           2606    40992    progetto progetto_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_pkey PRIMARY KEY (codprogetto);
 @   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_pkey;
       public            Bianca    false    203            M           2606    40974    societa societa_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.societa
    ADD CONSTRAINT societa_pkey PRIMARY KEY (partitaiva);
 >   ALTER TABLE ONLY public.societa DROP CONSTRAINT societa_pkey;
       public            postgres    false    202            \           2606    41042 "   meeting meeting_codconference_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.meeting
    ADD CONSTRAINT meeting_codconference_fkey FOREIGN KEY (codconference) REFERENCES public.conferencecall(codconference);
 L   ALTER TABLE ONLY public.meeting DROP CONSTRAINT meeting_codconference_fkey;
       public          Bianca    false    204    206    2897            Z           2606    41027 ,   partecipante partecipante_codconference_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_codconference_fkey FOREIGN KEY (codconference) REFERENCES public.conferencecall(codconference);
 V   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT partecipante_codconference_fkey;
       public          postgres    false    205    2897    204            Y           2606    41022 *   partecipante partecipante_codprogetto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_codprogetto_fkey FOREIGN KEY (codprogetto) REFERENCES public.progetto(codprogetto);
 T   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT partecipante_codprogetto_fkey;
       public          postgres    false    2895    203    205            [           2606    41032 #   partecipante partecipante_isin_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_isin_fkey FOREIGN KEY (isin) REFERENCES public.azienda(isin);
 M   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT partecipante_isin_fkey;
       public          postgres    false    200    2889    205            W           2606    40998    progetto progetto_cf_fkey    FK CONSTRAINT     u   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_cf_fkey FOREIGN KEY (cf) REFERENCES public.privato(cf);
 C   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_cf_fkey;
       public          Bianca    false    203    2891    201            V           2606    40993    progetto progetto_isin_fkey    FK CONSTRAINT     {   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_isin_fkey FOREIGN KEY (isin) REFERENCES public.azienda(isin);
 E   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_isin_fkey;
       public          Bianca    false    203    200    2889            X           2606    41003 !   progetto progetto_partitaiva_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_partitaiva_fkey FOREIGN KEY (partitaiva) REFERENCES public.societa(partitaiva);
 K   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_partitaiva_fkey;
       public          Bianca    false    202    2893    203            �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �     