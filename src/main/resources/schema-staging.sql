DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;

-- TABLES CREATING
CREATE TABLE public.makes
(
    make_id integer                           NOT NULL GENERATED ALWAYS AS IDENTITY ( CYCLE INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    make    text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT makes_pkey PRIMARY KEY (make_id),
    CONSTRAINT makes_make_key UNIQUE (make)
);

CREATE TABLE public.models
(
    model_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( CYCLE INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    make_id integer NOT NULL,
    model text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT models_pkey PRIMARY KEY (model_id),
    CONSTRAINT models_model_make_id_key UNIQUE (model, make_id),
    CONSTRAINT models_make_id_fkey FOREIGN KEY (make_id)
    REFERENCES public.makes (make_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID
    );

CREATE TABLE public.categories
(
    category_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( CYCLE INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    category text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT categories_pkey PRIMARY KEY (category_id),
    CONSTRAINT categories_category_key UNIQUE (category)
    );

CREATE TABLE public.cars
(
    object_id text COLLATE pg_catalog."default" NOT NULL,
    model_id integer NOT NULL,
    year integer NOT NULL,
    CONSTRAINT cars_pkey PRIMARY KEY (object_id),
    CONSTRAINT cars_model_id_fkey FOREIGN KEY (model_id)
    REFERENCES public.models (model_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID
    );

CREATE TABLE public.cars_categories
(
    object_id text COLLATE pg_catalog."default" NOT NULL,
    category_id integer NOT NULL,
    CONSTRAINT cars_categories_pkey PRIMARY KEY (object_id, category_id),
    CONSTRAINT cars_categories_category_id_fkey FOREIGN KEY (category_id)
    REFERENCES public.categories (category_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT cars_categories_object_id_fkey FOREIGN KEY (object_id)
    REFERENCES public.cars (object_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    );

-- THE TABLE OWNER SETTING
ALTER TABLE IF EXISTS public.makes OWNER to postgres;
ALTER TABLE IF EXISTS public.models OWNER to postgres;;
ALTER TABLE IF EXISTS public.categories OWNER to postgres;
ALTER TABLE IF EXISTS public.cars OWNER to postgres;
ALTER TABLE IF EXISTS public.cars_categories OWNER to postgres;
