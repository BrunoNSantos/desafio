package com.brunonsantos.cartao.service.impl;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.brunonsantos.cartao.model.Portador;
import com.brunonsantos.cartao.model.Proposta;
import com.brunonsantos.cartao.repository.PortadorRepository;
import com.brunonsantos.cartao.service.PortadorService;

@Service
public class PortadorServiceImpl implements PortadorService{

	@Resource
	private PortadorRepository portadorRepository;
	
	
	@Override
	public Portador criarPortador(Proposta proposta) {
		Portador portador = new Portador();
		BeanUtils.copyProperties(proposta, portador);
		
		
		return this.portadorRepository.save(portador);
	}

	@Override
	public Portador encontrarPorId(Long id) {
		Optional<Portador> optional = this.portadorRepository.findById(id);
		return optional.isPresent() ? optional.get() : null;
	}

	@Override
	public Portador encontrarPorCpf(String cpf) {
		return this.portadorRepository.findByCpf(cpf);
	}
	
}
