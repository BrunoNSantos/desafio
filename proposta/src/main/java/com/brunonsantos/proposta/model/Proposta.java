package com.brunonsantos.proposta.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

import lombok.Data;

/**
 * @author bruno
 *
 */
@Data
@Entity
@Table(schema = "public", name = "proposta")
public class Proposta {

	@Id
	@SequenceGenerator(name = "propostaSeq", sequenceName = "public.proposta_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "propostaSeq")
	@Column(name = "id")
	private Long id;
	
	@NotBlank
	@Column(name = "nome_cliente")
	private String nomeCliente;
	
	@NotBlank
	@Column(name = "email")
	private String email;
	
	@NotNull
	@CPF
	@Column(name = "cpf")
	private Integer cpf;
	
	@NotNull
	@Column(name = "renda")
	private BigDecimal renda;
	
	@Column(name = "aprovada")
	private Boolean aprovada;
	
	
	public Proposta() {
		this.aprovada = false;
	}
}
