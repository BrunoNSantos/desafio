package com.brunonsantos.cartao.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import com.brunonsantos.cartao.model.Portador;

public interface AuthService {

	Portador login(String email, String senha) throws BadCredentialsException, AccessDeniedException;

	void logout(HttpServletRequest request);

}
