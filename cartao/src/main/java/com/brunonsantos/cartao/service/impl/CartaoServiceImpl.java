package com.brunonsantos.cartao.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.brunonsantos.cartao.model.Cartao;
import com.brunonsantos.cartao.model.Portador;
import com.brunonsantos.cartao.model.Proposta;
import com.brunonsantos.cartao.repository.CartaoRepository;
import com.brunonsantos.cartao.repository.PortadorRepository;
import com.brunonsantos.cartao.service.CartaoService;
import com.brunonsantos.cartao.service.PortadorService;
import com.brunonsantos.cartao.utils.AuthUtils;

@Service
public class CartaoServiceImpl implements CartaoService {

	@Resource
	private CartaoRepository cartaoRepository;

	@Resource
	private PortadorRepository portadorRepository;
	
	@Resource
	private PortadorService portadorService;

	
	@Override
	public void vincularCartao(Proposta proposta) {
		final BigDecimal LIMITE_INICIAL = BigDecimal.valueOf(800);
		Portador portador = this.portadorService.encontrarPorCpf(proposta.getCpf());
		Cartao cartao = new Cartao();
		cartao.setValorLimite(LIMITE_INICIAL);
		
		if (portador == null) {
			portador = new Portador();
			BeanUtils.copyProperties(proposta, portador);
			portador.setSenha(AuthUtils.encodePassword(portador.getCpf()));
		}
		
		portador.addCartao(cartao);
		this.portadorRepository.save(portador);
	}
}
