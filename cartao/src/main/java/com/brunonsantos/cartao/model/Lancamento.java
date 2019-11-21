package com.brunonsantos.cartao.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.brunonsantos.cartao.enuns.TipoLancamento;

import lombok.Data;

/**
 * @author bruno
 *
 */
@Data
@Entity
@Table(schema = "public", name = "lancamento")
public class Lancamento {

	@Id
	@SequenceGenerator(name = "lancamentoSeq", sequenceName = "public.lancamento_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lancamentoSeq")
	@Column(name = "id")
	private Long id;
	
	@NotNull
	@Column(name = "data")
	private LocalDateTime data;
	
	@NotNull
	@Column(name = "tipo")
	private TipoLancamento tipo;
	
	@NotNull
	@Column(name = "valor")
	private BigDecimal valor;
	
	@NotNull
	@Column(name = "descricao")
	private String descricao;
}
