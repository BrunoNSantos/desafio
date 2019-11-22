CREATE SEQUENCE public.proposta_seq;

CREATE SEQUENCE public.portador_seq;

CREATE SEQUENCE public.cartao_seq;

CREATE SEQUENCE public.lancamento_seq;

CREATE SEQUENCE public.fatura_seq;


CREATE TABLE public.proposta
(
  id bigint NOT NULL DEFAULT nextval('proposta_seq'::regclass),
  nome_cliente character varying(100) NOT NULL,
  email character varying(80) NOT NULL,
  cpf character varying(20) NOT NULL,
  renda numeric(6,2),
  aprovada boolean NOT NULL DEFAULT false,
  CONSTRAINT pk_proposta PRIMARY KEY (id)
);

CREATE TABLE public.portador
(
  id bigint NOT NULL DEFAULT nextval('portador_seq'::regclass),
  nome_cliente character varying(100) NOT NULL,
  email character varying(80) NOT NULL,
  senha character varying(200),
  cpf character varying(20) NOT NULL,
  renda numeric(6,2),
  CONSTRAINT pk_portador PRIMARY KEY (id)
);

CREATE TABLE public.cartao
(
  id bigint NOT NULL DEFAULT nextval('cartao_seq'::regclass),
  id_portador bigint NOT NULL,
  valor_limite numeric(6,2),
  CONSTRAINT fk_portador_cartao FOREIGN KEY (id_portador)
	REFERENCES public.portador (id) MATCH SIMPLE
	ON UPDATE NO ACTION ON DELETE NO ACTION,	
  CONSTRAINT pk_cartao PRIMARY KEY (id)
);

CREATE TABLE public.lancamento
(
  id bigint NOT NULL DEFAULT nextval('lancamento_seq'::regclass),
  id_cartao bigint NOT NULL,
  data timestamp with time zone NOT NULL DEFAULT now() NOT NULL,
  tipo character varying(30) NOT NULL,
  descricao character varying(100) NOT NULL,
  valor numeric(6,2),
  CONSTRAINT fk_lancamento_cartao FOREIGN KEY (id_cartao)
	REFERENCES public.portador (id) MATCH SIMPLE
	ON UPDATE NO ACTION ON DELETE NO ACTION,	
  CONSTRAINT pk_lancamento PRIMARY KEY (id)
);

