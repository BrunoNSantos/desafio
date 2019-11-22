package com.brunonsantos.cartao.security.jwt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class JWTRequestMatcher implements RequestMatcher {

	@Override
	public boolean matches(HttpServletRequest request) {
		if (request.getHeader(HttpHeaders.AUTHORIZATION) != null)
			return true;

		return false;
	}

}
