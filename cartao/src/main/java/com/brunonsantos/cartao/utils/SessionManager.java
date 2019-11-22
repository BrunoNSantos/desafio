package com.brunonsantos.cartao.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.brunonsantos.cartao.model.Portador;
import com.brunonsantos.cartao.security.AuthenticatedUser;
import com.brunonsantos.cartao.security.UserAuthentication;

@Component
public class SessionManager {

	public static HttpSession createSessionForUser(HttpServletRequest request, Portador portador) {
		if (portador == null) {
			throw new IllegalArgumentException("O portador não pode ser nulo");
		}

		HttpSession session = request.getSession();
		if (!session.isNew()) {
			invalidateUserSession(request);
			session = request.getSession(true);
		}

		AuthenticatedUser authenticatedUser = new AuthenticatedUser();
		authenticatedUser.setEmail(portador.getEmail());
		authenticatedUser.setId(portador.getId());

		Authentication authentication = new UserAuthentication(authenticatedUser);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return session;
	}

	public static void invalidateUserSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(null);

		SecurityContextHolder.clearContext();
	}
}
