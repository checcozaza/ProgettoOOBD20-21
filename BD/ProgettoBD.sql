PGDMP     2            	        y         
   ProgettoBD    13.1    13.1     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16394 
   ProgettoBD    DATABASE     h   CREATE DATABASE "ProgettoBD" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Italian_Italy.1252';
    DROP DATABASE "ProgettoBD";
                postgres    false            t           1247    16402    enum_ambiti    DOMAIN     [  CREATE DOMAIN public.enum_ambiti AS character varying(50)
	CONSTRAINT enum_ambiti CHECK ((upper((VALUE)::text) = ANY (ARRAY['ECONOMIA E FINANZA'::text, 'MEDICINA'::text, 'INFORMATICA'::text, 'ALIMENTARE'::text, 'AUTOMOBILISTICA'::text, 'FARMACEUTICA'::text, 'ELETTRONICA'::text, 'MARKETING'::text, 'RICERCA E SVILUPPO'::text, 'CHIMICA'::text])));
     DROP DOMAIN public.enum_ambiti;
       public          postgres    false            p           1247    16397    enum_azienda    DOMAIN     �   CREATE DOMAIN public.enum_azienda AS character varying(8)
	CONSTRAINT enum_azienda CHECK ((upper((VALUE)::text) = ANY (ARRAY['S.P.A.'::text, 'S.S.'::text, 'S.N.C.'::text, 'S.A.S.'::text, 'S.R.L.'::text, 'S.A.P.A.'::text])));
 !   DROP DOMAIN public.enum_azienda;
       public          Bianca    false            x           1247    16405    enum_meeting    DOMAIN     �   CREATE DOMAIN public.enum_meeting AS character varying(30)
	CONSTRAINT enum_meeting CHECK ((upper((VALUE)::text) = ANY (ARRAY['SALA RIUNIONI'::text, 'PIATTAFORMA DI VIDEOCONFERENZA'::text])));
 !   DROP DOMAIN public.enum_meeting;
       public          Bianca    false            �           1247    16411 
   enum_ruoli    DOMAIN     1  CREATE DOMAIN public.enum_ruoli AS character varying(50)
	CONSTRAINT enum_ruoli CHECK ((upper((VALUE)::text) = ANY (ARRAY['RESPONSABILE DELLA COMUNICAZIONE'::text, 'COORDINATORE DI ATTIVITÀ'::text, 'RESPONSABILE AMMINISTRAZIONE'::text, 'RESPONSABILE AREA INFORMATICA'::text, 'PROJECT MANAGER'::text])));
    DROP DOMAIN public.enum_ruoli;
       public          Bianca    false            |           1247    16408    enum_tipologie    DOMAIN     �   CREATE DOMAIN public.enum_tipologie AS character varying(25)
	CONSTRAINT enum_tipologie CHECK ((upper((VALUE)::text) = ANY (ARRAY['RICERCA DI BASE'::text, 'RICERCA INDUSTRIALE'::text, 'RICERCA SPERIMENTALE'::text, 'SVILUPPO SPERIMENTALE'::text])));
 #   DROP DOMAIN public.enum_tipologie;
       public          postgres    false            �            1259    16413    azienda    TABLE     �   CREATE TABLE public.azienda (
    nome character varying(20) NOT NULL,
    formasocietaria public.enum_azienda NOT NULL,
    sedeprincipale character varying(20) NOT NULL,
    isin character varying(12) NOT NULL,
    ambito public.enum_ambiti NOT NULL
);
    DROP TABLE public.azienda;
       public         heap    Bianca    false    628    624            �            1259    16421    progetto    TABLE     �   CREATE TABLE public.progetto (
    tipologia public.enum_tipologie NOT NULL,
    ambito public.enum_ambiti NOT NULL,
    numeropartecipante integer NOT NULL,
    budget numeric NOT NULL,
    codprogetto character varying(8) NOT NULL
);
    DROP TABLE public.progetto;
       public         heap    postgres    false    628    636            �          0    16413    azienda 
   TABLE DATA           V   COPY public.azienda (nome, formasocietaria, sedeprincipale, isin, ambito) FROM stdin;
    public          Bianca    false    200          �          0    16421    progetto 
   TABLE DATA           ^   COPY public.progetto (tipologia, ambito, numeropartecipante, budget, codprogetto) FROM stdin;
    public          postgres    false    201   1       ;           2606    16420    azienda azienda_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.azienda
    ADD CONSTRAINT azienda_pkey PRIMARY KEY (isin);
 >   ALTER TABLE ONLY public.azienda DROP CONSTRAINT azienda_pkey;
       public            Bianca    false    200            =           2606    16428    progetto progetto_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_pkey PRIMARY KEY (codprogetto);
 @   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_pkey;
       public            postgres    false    201            �      x������ � �      �      x������ � �     