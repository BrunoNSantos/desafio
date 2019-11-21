package com.brunonsantos.cartao.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author bruno
 *
 */
@Data
@Entity
@Table(schema = "public", name = "fatura")
public class Fatura {

	@Id
	@SequenceGenerator(name = "faturaSeq", sequenceName = "public.fatura_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "faturaSeq")
	@Column(name = "id")
	private Long id;
	
	@NotNull
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Cartao cartao;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(schema = "public", name = "lancamento_fatura")
	private List<Lancamento> lancamentos;
	
	@NotNull
	@Column(name = "mes")
	private Integer mes;
	
	@NotNull
	@Column(name = "ano")
	private Integer ano;
	
	@NotNull
	@Column(name = "data_fechamento")
	private LocalDate dataFechamento;
	
	@NotNull
	@Column(name = "data_vencimento")
	private LocalDate dataVencimento;
	
	@NotNull
	@Column(name = "valor_total")
	private BigDecimal valorTotal;
	
}
