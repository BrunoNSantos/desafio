/**
 * 
 */
package com.brunonsantos.cartao.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.brunonsantos.cartao.security.AuthenticatedUser;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

/**
 * @author glauco
 *
 */
public class JWTAuthenticationFilter extends GenericFilterBean {

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		try {
			final String authorizationHeader = ((HttpServletRequest) request).getHeader(HttpHeaders.AUTHORIZATION);
			final AuthenticatedUser authenticatedUser = JWTService.parseToken(authorizationHeader);
			chain.doFilter(request, response);
		} catch (SignatureException | MalformedJwtException e) {
			if (response instanceof HttpServletResponse) {
				final HttpServletResponse httpResponse = (HttpServletResponse) response;

				httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		} catch (ExpiredJwtException e) {
			if (response instanceof HttpServletResponse) {
				final HttpServletResponse httpResponse = (HttpServletResponse) response;

				httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		} finally {
			SecurityContextHolder.clearContext();
		}
	}

}
