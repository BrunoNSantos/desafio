package com.brunonsantos.cartao.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brunonsantos.cartao.dto.LoginDTO;
import com.brunonsantos.cartao.dto.ResponseLoginDTO;
import com.brunonsantos.cartao.model.Portador;
import com.brunonsantos.cartao.service.AuthService;
import com.brunonsantos.cartao.utils.SessionManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Autenticacão")
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Resource
	private AuthService authService;
	
	@ApiIgnore
	@ApiOperation(value = "Login method", hidden = true, produces = "application/json")
	@PostMapping(value = "/login", produces = "application/json")
	public ResponseEntity<ResponseLoginDTO> login(HttpServletRequest request,
			@RequestBody(required = false) @Valid LoginDTO params) {
		Portador portador = this.authService.login(params.getEmail(), params.getSenha());
		SessionManager.createSessionForUser(request, portador);
		return ResponseEntity.ok(new ResponseLoginDTO(portador));	
	}
}
