PGDMP                         y            Progetto_BD    13.1    13.1 4    	           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            
           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    32768    Progetto_BD    DATABASE     i   CREATE DATABASE "Progetto_BD" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Italian_Italy.1252';
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
       public          Bianca    false            �            1255    73823 
   check_pm()    FUNCTION     L  CREATE FUNCTION public.check_pm() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE npm int;
BEGIN
    SELECT count(*) INTO npm
    FROM partecipante
    WHERE ruolo = 'Project Manager';
    
    if npm >0 then
        Raise 'Errore, un progetto non può avere più di un project manager';
    end if;
    return new;
END;
$$;
 !   DROP FUNCTION public.check_pm();
       public          Bianca    false            �            1259    73785    ambito    TABLE     E   CREATE TABLE public.ambito (
    nome public.enum_ambito NOT NULL
);
    DROP TABLE public.ambito;
       public         heap    Bianca    false    647            �            1259    40960    azienda    TABLE     �   CREATE TABLE public.azienda (
    isin character varying(12) NOT NULL,
    nome character varying(12) NOT NULL,
    sedeprincipale character varying(12) NOT NULL
);
    DROP TABLE public.azienda;
       public         heap    postgres    false            �            1259    41008    conferencecall    TABLE     �   CREATE TABLE public.conferencecall (
    codconference integer NOT NULL,
    luogo character varying(10),
    numeropartecipanti integer NOT NULL,
    piattaforma character varying(10)
);
 "   DROP TABLE public.conferencecall;
       public         heap    Bianca    false            �            1259    41037    meeting    TABLE     �   CREATE TABLE public.meeting (
    codmeeting integer NOT NULL,
    datariunione date NOT NULL,
    orainizio time without time zone NOT NULL,
    orafine time without time zone NOT NULL,
    codconference integer NOT NULL
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
    codprogetto integer NOT NULL,
    codconference integer,
    isin character varying(12) NOT NULL
);
     DROP TABLE public.partecipante;
       public         heap    postgres    false    651            �            1259    73793    partecipanteprog    TABLE     f   CREATE TABLE public.partecipanteprog (
    username integer NOT NULL,
    codprog integer NOT NULL
);
 $   DROP TABLE public.partecipanteprog;
       public         heap    Bianca    false            �            1259    40965    privato    TABLE     �   CREATE TABLE public.privato (
    cf character varying(16) NOT NULL,
    nome character varying(20) NOT NULL,
    cognome character varying(20) NOT NULL
);
    DROP TABLE public.privato;
       public         heap    postgres    false            �            1259    73806 
   progambito    TABLE     k   CREATE TABLE public.progambito (
    codprogetto integer NOT NULL,
    nome public.enum_ambito NOT NULL
);
    DROP TABLE public.progambito;
       public         heap    Bianca    false    647            �            1259    40984    progetto    TABLE     �  CREATE TABLE public.progetto (
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
       public         heap    Bianca    false    643            �            1259    73764    progrealizzato    TABLE     s   CREATE TABLE public.progrealizzato (
    codprog integer NOT NULL,
    tipologia public.enum_tipologia NOT NULL
);
 "   DROP TABLE public.progrealizzato;
       public         heap    Bianca    false    643            �            1259    40970    societa    TABLE        CREATE TABLE public.societa (
    partitaiva character varying(11) NOT NULL,
    nomesocieta character varying(25) NOT NULL
);
    DROP TABLE public.societa;
       public         heap    postgres    false                      0    73785    ambito 
   TABLE DATA           &   COPY public.ambito (nome) FROM stdin;
    public          Bianca    false    208   'C       �          0    40960    azienda 
   TABLE DATA           =   COPY public.azienda (isin, nome, sedeprincipale) FROM stdin;
    public          postgres    false    200   DC                  0    41008    conferencecall 
   TABLE DATA           _   COPY public.conferencecall (codconference, luogo, numeropartecipanti, piattaforma) FROM stdin;
    public          Bianca    false    204   {C                 0    41037    meeting 
   TABLE DATA           ^   COPY public.meeting (codmeeting, datariunione, orainizio, orafine, codconference) FROM stdin;
    public          Bianca    false    206   �C                 0    41014    partecipante 
   TABLE DATA           �   COPY public.partecipante (username, email, pw, cf, nome, cognome, ruolo, salariomedio, valutazio, codprogetto, codconference, isin) FROM stdin;
    public          postgres    false    205   �C                 0    73793    partecipanteprog 
   TABLE DATA           =   COPY public.partecipanteprog (username, codprog) FROM stdin;
    public          Bianca    false    209   <D       �          0    40965    privato 
   TABLE DATA           4   COPY public.privato (cf, nome, cognome) FROM stdin;
    public          postgres    false    201   YD                 0    73806 
   progambito 
   TABLE DATA           7   COPY public.progambito (codprogetto, nome) FROM stdin;
    public          Bianca    false    210   vD       �          0    40984    progetto 
   TABLE DATA           l   COPY public.progetto (codprogetto, tipologia, numeropartecipanti, budget, isin, cf, partitaiva) FROM stdin;
    public          Bianca    false    203   �D                 0    73764    progrealizzato 
   TABLE DATA           <   COPY public.progrealizzato (codprog, tipologia) FROM stdin;
    public          Bianca    false    207   �D       �          0    40970    societa 
   TABLE DATA           :   COPY public.societa (partitaiva, nomesocieta) FROM stdin;
    public          postgres    false    202   �D       m           2606    73792    ambito ambito_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.ambito
    ADD CONSTRAINT ambito_pkey PRIMARY KEY (nome);
 <   ALTER TABLE ONLY public.ambito DROP CONSTRAINT ambito_pkey;
       public            Bianca    false    208            ]           2606    40964    azienda azienda_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.azienda
    ADD CONSTRAINT azienda_pkey PRIMARY KEY (isin);
 >   ALTER TABLE ONLY public.azienda DROP CONSTRAINT azienda_pkey;
       public            postgres    false    200            e           2606    41013 "   conferencecall conferencecall_pkey 
   CONSTRAINT     k   ALTER TABLE ONLY public.conferencecall
    ADD CONSTRAINT conferencecall_pkey PRIMARY KEY (codconference);
 L   ALTER TABLE ONLY public.conferencecall DROP CONSTRAINT conferencecall_pkey;
       public            Bianca    false    204            i           2606    41041    meeting meeting_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.meeting
    ADD CONSTRAINT meeting_pkey PRIMARY KEY (codmeeting);
 >   ALTER TABLE ONLY public.meeting DROP CONSTRAINT meeting_pkey;
       public            Bianca    false    206            g           2606    41021    partecipante partecipante_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_pkey PRIMARY KEY (username);
 H   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT partecipante_pkey;
       public            postgres    false    205            _           2606    40969    privato privato_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.privato
    ADD CONSTRAINT privato_pkey PRIMARY KEY (cf);
 >   ALTER TABLE ONLY public.privato DROP CONSTRAINT privato_pkey;
       public            postgres    false    201            c           2606    40992    progetto progetto_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_pkey PRIMARY KEY (codprogetto);
 @   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_pkey;
       public            Bianca    false    203            k           2606    73771 "   progrealizzato progrealizzato_pkey 
   CONSTRAINT     e   ALTER TABLE ONLY public.progrealizzato
    ADD CONSTRAINT progrealizzato_pkey PRIMARY KEY (codprog);
 L   ALTER TABLE ONLY public.progrealizzato DROP CONSTRAINT progrealizzato_pkey;
       public            Bianca    false    207            [           2606    73822    conferencecall riunione    CHECK CONSTRAINT     �   ALTER TABLE public.conferencecall
    ADD CONSTRAINT riunione CHECK ((((luogo IS NOT NULL) AND (piattaforma IS NULL)) OR ((luogo IS NULL) AND (piattaforma IS NOT NULL)))) NOT VALID;
 <   ALTER TABLE public.conferencecall DROP CONSTRAINT riunione;
       public          Bianca    false    204    204    204    204            a           2606    40974    societa societa_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.societa
    ADD CONSTRAINT societa_pkey PRIMARY KEY (partitaiva);
 >   ALTER TABLE ONLY public.societa DROP CONSTRAINT societa_pkey;
       public            postgres    false    202            y           2620    73824    partecipante projectmanager    TRIGGER     t   CREATE TRIGGER projectmanager BEFORE INSERT ON public.partecipante FOR EACH ROW EXECUTE FUNCTION public.check_pm();
 4   DROP TRIGGER projectmanager ON public.partecipante;
       public          postgres    false    205    211            t           2606    41042 "   meeting meeting_codconference_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.meeting
    ADD CONSTRAINT meeting_codconference_fkey FOREIGN KEY (codconference) REFERENCES public.conferencecall(codconference);
 L   ALTER TABLE ONLY public.meeting DROP CONSTRAINT meeting_codconference_fkey;
       public          Bianca    false    2917    206    204            r           2606    41027 ,   partecipante partecipante_codconference_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_codconference_fkey FOREIGN KEY (codconference) REFERENCES public.conferencecall(codconference);
 V   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT partecipante_codconference_fkey;
       public          postgres    false    205    204    2917            q           2606    41022 *   partecipante partecipante_codprogetto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_codprogetto_fkey FOREIGN KEY (codprogetto) REFERENCES public.progetto(codprogetto);
 T   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT partecipante_codprogetto_fkey;
       public          postgres    false    205    203    2915            s           2606    41032 #   partecipante partecipante_isin_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT partecipante_isin_fkey FOREIGN KEY (isin) REFERENCES public.azienda(isin);
 M   ALTER TABLE ONLY public.partecipante DROP CONSTRAINT partecipante_isin_fkey;
       public          postgres    false    2909    200    205            v           2606    73801 .   partecipanteprog partecipanteprog_codprog_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipanteprog
    ADD CONSTRAINT partecipanteprog_codprog_fkey FOREIGN KEY (codprog) REFERENCES public.progrealizzato(codprog);
 X   ALTER TABLE ONLY public.partecipanteprog DROP CONSTRAINT partecipanteprog_codprog_fkey;
       public          Bianca    false    2923    209    207            u           2606    73796 /   partecipanteprog partecipanteprog_username_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.partecipanteprog
    ADD CONSTRAINT partecipanteprog_username_fkey FOREIGN KEY (username) REFERENCES public.partecipante(username);
 Y   ALTER TABLE ONLY public.partecipanteprog DROP CONSTRAINT partecipanteprog_username_fkey;
       public          Bianca    false    209    205    2919            w           2606    73812 &   progambito progambito_codprogetto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.progambito
    ADD CONSTRAINT progambito_codprogetto_fkey FOREIGN KEY (codprogetto) REFERENCES public.progetto(codprogetto);
 P   ALTER TABLE ONLY public.progambito DROP CONSTRAINT progambito_codprogetto_fkey;
       public          Bianca    false    210    2915    203            x           2606    73817    progambito progambito_nome_fkey    FK CONSTRAINT     ~   ALTER TABLE ONLY public.progambito
    ADD CONSTRAINT progambito_nome_fkey FOREIGN KEY (nome) REFERENCES public.ambito(nome);
 I   ALTER TABLE ONLY public.progambito DROP CONSTRAINT progambito_nome_fkey;
       public          Bianca    false    2925    210    208            o           2606    40998    progetto progetto_cf_fkey    FK CONSTRAINT     u   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_cf_fkey FOREIGN KEY (cf) REFERENCES public.privato(cf);
 C   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_cf_fkey;
       public          Bianca    false    203    2911    201            n           2606    40993    progetto progetto_isin_fkey    FK CONSTRAINT     {   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_isin_fkey FOREIGN KEY (isin) REFERENCES public.azienda(isin);
 E   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_isin_fkey;
       public          Bianca    false    200    2909    203            p           2606    41003 !   progetto progetto_partitaiva_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.progetto
    ADD CONSTRAINT progetto_partitaiva_fkey FOREIGN KEY (partitaiva) REFERENCES public.societa(partitaiva);
 K   ALTER TABLE ONLY public.progetto DROP CONSTRAINT progetto_partitaiva_fkey;
       public          Bianca    false    203    202    2913                  x������ � �      �   '   x�srv�2���K�IM���LTp��L�L����� ���             x������ � �            x������ � �         w   x�3426��!g'gwWs7sK�PN��ļ�DN�ԌĔT΀�������ļ���"N�&C���e�ejfn5**������<������2�ӭhTjqr>gTbU"A�b���� �m)�            x������ � �      �      x������ � �            x������ � �      �   1   x�3��LN-JNTH�THJ,N�42�44 N'g�(C�? ����� 
�
z            x������ � �      �      x������ � �     