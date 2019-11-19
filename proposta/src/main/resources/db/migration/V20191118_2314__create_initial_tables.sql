CREATE SEQUENCE public.proposta_seq;

CREATE TABLE public.proposta
(
  id bigint NOT NULL DEFAULT nextval('proposta_seq'::regclass),
  nome_cliente character varying(100) NOT NULL,
  email character varying(80) NOT NULL,
  cpf bigint NOT NULL,
  renda numeric(6,2),
  aprovada boolean NOT NULL DEFAULT false,
  CONSTRAINT pk_proposta PRIMARY KEY (id)
);
