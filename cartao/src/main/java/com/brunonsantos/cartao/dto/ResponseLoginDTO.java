package com.brunonsantos.cartao.dto;

import com.brunonsantos.cartao.model.Portador;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseLoginDTO {

	@ApiModelProperty(value = "Nome do portador")
	private String nome;

	@ApiModelProperty(value = "CPF do portador")
	private String email;

	
	public ResponseLoginDTO(Portador portador) {
		this.nome = portador.getNomeCliente();
		this.email = portador.getEmail();
	}
}
