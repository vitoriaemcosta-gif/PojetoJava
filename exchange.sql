--
-- PostgreSQL database dump limpo para entrega
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';
SET default_table_access_method = heap;

-- Criar tabela investidor
CREATE TABLE IF NOT EXISTS public.investidor (
    cpf character varying NOT NULL,
    nome character varying NOT NULL,
    senha character varying NOT NULL,
    saldo_reais numeric DEFAULT 0.00 NOT NULL,
    saldo_btc numeric DEFAULT 0.00 NOT NULL,
    saldo_eth numeric DEFAULT 0.00 NOT NULL,
    saldo_xrp numeric DEFAULT 0.00 NOT NULL,
    email character varying(150),
    CONSTRAINT investidor_pkey PRIMARY KEY (cpf)
);

ALTER TABLE public.investidor OWNER TO postgres;

-- Criar sequência para a transacao
CREATE SEQUENCE IF NOT EXISTS public."Transacao_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public."Transacao_id_seq" OWNER TO postgres;

-- Criar tabela transacao
CREATE TABLE IF NOT EXISTS public.transacao (
    id integer DEFAULT nextval('public."Transacao_id_seq"'::regclass) NOT NULL,
    cpf character varying NOT NULL,
    data_hora character varying(50) NOT NULL,
    tipo character varying(10) NOT NULL,
    moeda character varying(20) NOT NULL,
    valor numeric(15,4) NOT NULL,
    taxa numeric(15,4),
    saldo_reais_apos numeric(15,2),
    saldo_btc_apos numeric(15,4),
    saldo_eth_apos numeric(15,4),
    saldo_xrp_apos numeric(15,4),
    CONSTRAINT "Transacao_pkey" PRIMARY KEY (id)
);

ALTER TABLE public.transacao OWNER TO postgres;

ALTER SEQUENCE public."Transacao_id_seq" OWNED BY public.transacao.id;

-- Inserir dados na tabela investidor
INSERT INTO public.investidor (cpf, nome, senha, saldo_reais, saldo_btc, saldo_eth, saldo_xrp, email) 
VALUES 
('12345678900', 'Gabriela Biondi', '123456', 500.00, 0.0, 0.0, 0.0, NULL),
('43451945800', 'Vitoria', '434519', 2448.02, 0.00102857142857143, 0, 0, 'vitoriae.m@gmail.com')
ON CONFLICT (cpf) DO NOTHING;

-- Inserir dados na tabela transacao
INSERT INTO public.transacao (id, cpf, data_hora, tipo, moeda, valor, taxa, saldo_reais_apos, saldo_btc_apos, saldo_eth_apos, saldo_xrp_apos) 
VALUES 
(17, '43451945800', '11-11-2026 19:10', '+', 'REAL', 1022.2200, 0.0000, 1022.22, 0.0000, 0.0000, 0.0000),
(18, '43451945800', '12-11-2026 22:10', '+', 'BTC', 500.0000, 10.0000, 512.22, 0.0014, 0.0000, 0.0000),
(19, '43451945800', '12-11-2026 22:11', '-', 'BTC', 0.0004, 4.2000, 648.02, 0.0010, 0.0000, 0.0000),
(20, '43451945800', '12-11-2026 22:14', '+', 'REAL', 2000.0000, 0.0000, 2648.02, 0.0010, 0.0000, 0.0000),
(21, '43451945800', '12-11-2026 22:14', '-', 'REAL', 200.0000, 0.0000, 2448.02, 0.0010, 0.0000, 0.0000)
ON CONFLICT (id) DO NOTHING;

-- Atualizar o contador da sequência para o próximo ID correto
SELECT pg_catalog.setval('public."Transacao_id_seq"', 21, true);