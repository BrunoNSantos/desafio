package com.brunonsantos.proposta.service.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brunonsantos.proposta.dto.CriarPropostaDTO;
import com.brunonsantos.proposta.exception.PropostaNaoEncontradaException;
import com.brunonsantos.proposta.model.Proposta;
import com.brunonsantos.proposta.repository.PropostaRepository;
import com.brunonsantos.proposta.service.PropostaService;
import com.brunonsantos.proposta.service.RabbitMQSender;

/**
 * @author bruno
 *
 */
@Service
@Transactional(readOnly = true)
public class PropostaServiceImpl implements PropostaService {

	@Resource
	private PropostaRepository propostaRepository;

	@Resource
	RabbitMQSender rabbitMQSender;

	@Override
	@Transactional(readOnly = false)
	public Proposta criarProposta(CriarPropostaDTO propostaDTO) {
		Proposta proposta = new Proposta();

		BeanUtils.copyProperties(propostaDTO, proposta);
		return this.propostaRepository.save(proposta);
	}

	@Override
	public List<Proposta> listarPropostas() {
		return this.propostaRepository.findAll();
	}

	@Override
	@Transactional(readOnly = false)
	public void aprovarProposta(Long idProposta) throws PropostaNaoEncontradaException {
		Optional<Proposta> optional = this.propostaRepository.findById(idProposta);

		if (!optional.isPresent()) {
			throw new PropostaNaoEncontradaException("A proposta não foi encontrada");
		}

		Proposta proposta = optional.get();
		proposta.setAprovada(true);

		this.propostaRepository.save(proposta);

		// Envia proposta para o RabbitMQ
		rabbitMQSender.send(proposta);
	}

}
