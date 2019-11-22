package com.brunonsantos.cartao.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticatedUser  implements UserDetails{

	private static final long serialVersionUID = 660218823893315137L;

	private Collection<GrantedAuthority> authorities;
	
	private Long id;
	private Long accessTokenId;
	private String refreshTokenId;
	private String email;
	
	public AuthenticatedUser() {
		super();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public String getUsername() {
		return this.getEmail();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public void setAuthorities(String... authorities) {
		this.authorities = Collections.unmodifiableCollection(AuthorityUtils.createAuthorityList(authorities));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAccessTokenId() {
		return accessTokenId;
	}

	public void setAccessTokenId(Long accessTokenId) {
		this.accessTokenId = accessTokenId;
	}

	public String getRefreshTokenId() {
		return refreshTokenId;
	}

	public void setRefreshTokenId(String refreshTokenId) {
		this.refreshTokenId = refreshTokenId;
	}


}
