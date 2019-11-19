package com.brunonsantos.proposta.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.brunonsantos.proposta.dto.CriarPropostaDTO;
import com.brunonsantos.proposta.exception.PropostaNaoEncontradaException;
import com.brunonsantos.proposta.model.Proposta;

public interface PropostaService {
	
	Proposta criarProposta(CriarPropostaDTO proposta);
	
	Page<Proposta> listarPropostas(Pageable pageable);
	
	void aprovarProposta(Long idProposta) throws PropostaNaoEncontradaException;
}
