PGDMP         6                y            Progetto_BD    13.1    13.1     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    32768    Progetto_BD    DATABASE     i   CREATE DATABASE "Progetto_BD" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Italian_Italy.1252';
    DROP DATABASE "Progetto_BD";
                postgres    false            �            1259    40960    azienda    TABLE     �   CREATE TABLE public.azienda (
    isin character varying(12) NOT NULL,
    nome character varying(12) NOT NULL,
    sedeprincipale character varying(12) NOT NULL
);
    DROP TABLE public.azienda;
       public         heap    postgres    false            �            1259    40965    privato    TABLE     �   CREATE TABLE public.privato (
    cf character varying(16) NOT NULL,
    nome character varying(20) NOT NULL,
    cognome character varying(20) NOT NULL
);
    DROP TABLE public.privato;
       public         heap    postgres    false            �            1259    40970    societa    TABLE        CREATE TABLE public.societa (
    partitaiva character varying(11) NOT NULL,
    nomesocieta character varying(25) NOT NULL
);
    DROP TABLE public.societa;
       public         heap    postgres    false            �          0    40960    azienda 
   TABLE DATA           =   COPY public.azienda (isin, nome, sedeprincipale) FROM stdin;
    public          postgres    false    200   �       �          0    40965    privato 
   TABLE DATA           4   COPY public.privato (cf, nome, cognome) FROM stdin;
    public          postgres    false    201   �       �          0    40970    societa 
   TABLE DATA           :   COPY public.societa (partitaiva, nomesocieta) FROM stdin;
    public          postgres    false    202   �       )           2606    40964    azienda azienda_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.azienda
    ADD CONSTRAINT azienda_pkey PRIMARY KEY (isin);
 >   ALTER TABLE ONLY public.azienda DROP CONSTRAINT azienda_pkey;
       public            postgres    false    200            +           2606    40969    privato privato_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.privato
    ADD CONSTRAINT privato_pkey PRIMARY KEY (cf);
 >   ALTER TABLE ONLY public.privato DROP CONSTRAINT privato_pkey;
       public            postgres    false    201            -           2606    40974    societa societa_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.societa
    ADD CONSTRAINT societa_pkey PRIMARY KEY (partitaiva);
 >   ALTER TABLE ONLY public.societa DROP CONSTRAINT societa_pkey;
       public            postgres    false    202            �      x������ � �      �      x������ � �      �      x������ � �     