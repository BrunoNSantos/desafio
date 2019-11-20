package com.brunonsantos.proposta.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brunonsantos.proposta.dto.CriarPropostaDTO;
import com.brunonsantos.proposta.exception.PropostaNaoEncontradaException;
import com.brunonsantos.proposta.model.Proposta;
import com.brunonsantos.proposta.service.PropostaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author bruno
 *
 */
@Api
@RestController
@RequestMapping("/proposta")
public class PropostaController {

	@Resource
	private PropostaService propostaService;

	@ApiOperation(nickname = "Criar Proposta", value = "Cria uma nova proposta de cadastro de cliente", produces = "application/json")
	@PostMapping(value = "/criar", produces = "application/json")
	public ResponseEntity<Proposta> criarProposta(
			@ApiParam(value = "Dados para criar um novo cliente") @RequestBody CriarPropostaDTO params) {
		return ResponseEntity.ok().body(this.propostaService.criarProposta(params));
	}

	@ApiOperation(nickname = "Listar Propostas", value = "Retorna a lista de todas as propostas", produces = "application/json")
	@PostMapping(value = "/listarPropostas", produces = "application/json")
	public ResponseEntity<List<Proposta>> listarPropostas() {
		return ResponseEntity.ok().body(this.propostaService.listarPropostas());
	}

	@ApiOperation(nickname = "Aprovar Proposta", value = "Realiza a aprovação de uma proposta", produces = "application/json")
	@PutMapping(value = "/aprovar/{idProposta}")
	public ResponseEntity<Void> aprovarProposta(@PathVariable Long idProposta) throws PropostaNaoEncontradaException {
		this.propostaService.aprovarProposta(idProposta);
		return ResponseEntity.ok().build();
	}

}
