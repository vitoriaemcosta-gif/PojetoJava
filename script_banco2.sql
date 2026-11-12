--
-- PostgreSQL database dump
--

\restrict 47pLz5ZPctOf3g7TxZtVEMcK6VDo35krtheedjhcoQbCyBAlmUZT2rz2iDxMxwh

-- Dumped from database version 18.6
-- Dumped by pg_dump version 18.6

-- Started on 2026-11-11 22:20:01

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

--
-- TOC entry 221 (class 1259 OID 16405)
-- Name: transacao; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transacao (
    id integer CONSTRAINT "Transacao_id_not_null" NOT NULL,
    cpf character varying CONSTRAINT "Transacao_cpf_not_null" NOT NULL,
    data_hora character varying(50) CONSTRAINT "Transacao_data_hora_not_null" NOT NULL,
    tipo character varying(10) CONSTRAINT "Transacao_tipo_not_null" NOT NULL,
    moeda character varying(20) CONSTRAINT "Transacao_moeda_not_null" NOT NULL,
    valor numeric(15,4) CONSTRAINT "Transacao_valor_not_null" NOT NULL,
    taxa numeric(15,4),
    saldo_reais_apos numeric(15,2),
    saldo_btc_apos numeric(15,4),
    saldo_eth_apos numeric(15,4),
    saldo_xrp_apos numeric(15,4)
);


ALTER TABLE public.transacao OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16404)
-- Name: Transacao_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Transacao_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."Transacao_id_seq" OWNER TO postgres;

--
-- TOC entry 5024 (class 0 OID 0)
-- Dependencies: 220
-- Name: Transacao_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Transacao_id_seq" OWNED BY public.transacao.id;


--
-- TOC entry 219 (class 1259 OID 16385)
-- Name: investidor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.investidor (
    cpf character varying NOT NULL,
    nome character varying NOT NULL,
    senha character varying NOT NULL,
    saldo_reais numeric DEFAULT 0.00 NOT NULL,
    saldo_btc numeric DEFAULT 0.00 NOT NULL,
    saldo_eth numeric DEFAULT 0.00 NOT NULL,
    saldo_xrp numeric DEFAULT 0.00 NOT NULL,
    email character varying(150)
);


ALTER TABLE public.investidor OWNER TO postgres;

--
-- TOC entry 4864 (class 2604 OID 16408)
-- Name: transacao id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transacao ALTER COLUMN id SET DEFAULT nextval('public."Transacao_id_seq"'::regclass);


--
-- TOC entry 5016 (class 0 OID 16385)
-- Dependencies: 219
-- Data for Name: investidor; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.investidor (cpf, nome, senha, saldo_reais, saldo_btc, saldo_eth, saldo_xrp, email) FROM stdin;
12345678900	Gabriela Biondi	123456	500.00	0.0	0.0	0.0	\N
43451945800	Vitoria 	434519	1022.22	0	0	0	vitoriae.m@gmail.com
\.


--
-- TOC entry 5018 (class 0 OID 16405)
-- Dependencies: 221
-- Data for Name: transacao; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.transacao (id, cpf, data_hora, tipo, moeda, valor, taxa, saldo_reais_apos, saldo_btc_apos, saldo_eth_apos, saldo_xrp_apos) FROM stdin;
17	43451945800	11-11-2026 19:10	+	REAL	1022.2200	0.0000	1022.22	0.0000	0.0000	0.0000
\.


--
-- TOC entry 5025 (class 0 OID 0)
-- Dependencies: 220
-- Name: Transacao_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Transacao_id_seq"', 17, true);


--
-- TOC entry 4868 (class 2606 OID 16418)
-- Name: transacao Transacao_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transacao
    ADD CONSTRAINT "Transacao_pkey" PRIMARY KEY (id);


--
-- TOC entry 4866 (class 2606 OID 16398)
-- Name: investidor investidor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.investidor
    ADD CONSTRAINT investidor_pkey PRIMARY KEY (cpf);


-- Completed on 2026-11-11 22:20:02

--
-- PostgreSQL database dump complete
--

\unrestrict 47pLz5ZPctOf3g7TxZtVEMcK6VDo35krtheedjhcoQbCyBAlmUZT2rz2iDxMxwh

