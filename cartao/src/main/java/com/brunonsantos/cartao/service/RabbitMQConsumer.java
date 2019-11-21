package com.brunonsantos.cartao.service;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.brunonsantos.cartao.model.Proposta;

@Service
public class RabbitMQConsumer {
	
	@Resource
	private CartaoService cartaoService;

	@RabbitListener(queues = "${rabbitmq.queue}")
	public void recievedMessage(Proposta proposta) {
		this.cartaoService.vincularCartao(proposta);
	}
}
