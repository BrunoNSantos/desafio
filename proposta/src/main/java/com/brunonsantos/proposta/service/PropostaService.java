package com.brunonsantos.proposta.service;

import java.util.List;

import com.brunonsantos.proposta.dto.CriarPropostaDTO;
import com.brunonsantos.proposta.exception.PropostaNaoEncontradaException;
import com.brunonsantos.proposta.model.Proposta;

public interface PropostaService {

	Proposta criarProposta(CriarPropostaDTO proposta);

	List<Proposta> listarPropostas();

	void aprovarProposta(Long idProposta) throws PropostaNaoEncontradaException;
}
