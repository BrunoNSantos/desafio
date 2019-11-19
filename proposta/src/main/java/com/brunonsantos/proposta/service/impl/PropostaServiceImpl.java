package com.brunonsantos.proposta.service.impl;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brunonsantos.proposta.dto.CriarPropostaDTO;
import com.brunonsantos.proposta.exception.PropostaNaoEncontradaException;
import com.brunonsantos.proposta.model.Proposta;
import com.brunonsantos.proposta.repository.PropostaRepository;
import com.brunonsantos.proposta.service.PropostaService;

/**
 * @author bruno
 *
 */

@Service
@Transactional(readOnly = true)
public class PropostaServiceImpl implements PropostaService{

	@Resource
	private PropostaRepository propostaRepository;
	
	
	@Override
	@Transactional(readOnly = false)
	public Proposta criarProposta(CriarPropostaDTO propostaDTO) {
		Proposta proposta = new Proposta();
		BeanUtils.copyProperties(propostaDTO, proposta);
		return this.propostaRepository.save(proposta);
	}

	@Override
	public Page<Proposta> listarPropostas(Pageable pageable) {
		return this.propostaRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = false)
	public void aprovarProposta(Long idProposta) throws PropostaNaoEncontradaException {
		Optional<Proposta> optional = this.propostaRepository.findById(idProposta);
		
		if(!optional.isPresent()) {
			throw new PropostaNaoEncontradaException("A proposta não foi encontrada");
		}
		
		Proposta proposta =  optional.get();
		proposta.setAprovada(true);
		
		this.propostaRepository.save(proposta);
	}

}
