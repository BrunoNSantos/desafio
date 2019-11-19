package com.brunonsantos.proposta.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarPropostaDTO {

	@NotBlank
	private String nomeCliente;
	
	@NotBlank
	private String email;
	
	@NotNull
	@CPF
	private Integer cpf;
	
	@NotNull
	private BigDecimal renda;
	
}
