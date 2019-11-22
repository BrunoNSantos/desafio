package com.brunonsantos.cartao.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "Credenciais para login", description = "Objeto contendo as credenciais para realizar login.")
public class LoginDTO {
	
	@NotBlank
	@Email
	@Size(max = 150)
	@ApiModelProperty(value = "Email do portador", required = true, allowEmptyValue = false)
	private String email;

	@NotBlank
	@Size(min = 6, max = 32)
	@ApiModelProperty(value = "CPF do portador", required = true, allowEmptyValue = false)
	private String senha;
}
