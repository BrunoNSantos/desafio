package com.brunonsantos.cartao.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import com.brunonsantos.cartao.model.Portador;
import com.brunonsantos.cartao.service.AuthService;
import com.brunonsantos.cartao.service.PortadorService;
import com.brunonsantos.cartao.utils.AuthUtils;
import com.brunonsantos.cartao.utils.SessionManager;

public class AuthServiceImpl implements AuthService{
	
	static final String MSG_BAD_CREDENTIALS = "E-mail ou senha inválidos.";
	
	@Resource
	private PortadorService portadorService;

	@Override
	public Portador login(String email, String password) throws BadCredentialsException, AccessDeniedException {
		Portador portador = this.portadorService.encontrarPorEmail(email);
		
		// Verifica senha do usuário
		if ((portador == null) || !AuthUtils.isPasswordValid(portador.getCpf(), password) || email == null
				|| password == null) {
			throw new BadCredentialsException(MSG_BAD_CREDENTIALS);
		}
		
		return portador;
	}

	@Override
	public void logout(HttpServletRequest request) {
		SessionManager.invalidateUserSession(request);
	}
}
