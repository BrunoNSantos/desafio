package com.brunonsantos.cartao.service;

import com.brunonsantos.cartao.model.Portador;
import com.brunonsantos.cartao.model.Proposta;

public interface PortadorService {

	Portador criarPortador(Proposta proposta);
	
	Portador encontrarPorId(Long id);
	
	Portador encontrarPorCpf(String cpf);
}
