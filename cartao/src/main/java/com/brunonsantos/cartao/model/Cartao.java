package com.brunonsantos.cartao.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(schema = "public", name = "cartao")
public class Cartao {

	@Id
	@SequenceGenerator(name = "cartaoSeq", sequenceName = "public.cartao_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cartaoSeq")
	@Column(name = "id")
	private Long id;
	
	@NotNull
	@Column(name = "valor_limite")
	private BigDecimal valorLimite;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "id_cartao")
	private List<Fatura> faturas;

}
