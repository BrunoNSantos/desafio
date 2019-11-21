package com.brunonsantos.proposta.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.brunonsantos.proposta.model.Proposta;

@Service
public class RabbitMQSender {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	@Value("${rabbitmq.exchange}")
	private String exchange;

	@Value("${rabbitmq.routingkey}")
	private String routingkey;

	public void send(Proposta proposta) {
		rabbitTemplate.convertAndSend(exchange, routingkey, proposta);
		System.out.println("Enviando proposta = " + proposta);

	}

}