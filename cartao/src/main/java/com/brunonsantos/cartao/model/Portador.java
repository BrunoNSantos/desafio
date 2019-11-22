package com.brunonsantos.cartao.model;

import java.util.ArrayList;
import java.util.Collections;
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
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.br.CPF;

import lombok.Data;

/**
 * @author bruno
 *
 */
@Data
@Entity
@Table(schema = "public", name = "portador")
public class Portador {

	@Id
	@SequenceGenerator(name = "portadorSeq", sequenceName = "public.portador_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "portadorSeq")
	@Column(name = "id")
	private Long id;
	
	@NotBlank
	@Column(name = "nome_cliente")
	private String nomeCliente;
	
	@NotBlank
	@Column(name = "email")
	private String email;
	
	@NotBlank
	@CPF
	@Column(name = "cpf")
	private String cpf;
	
	@Column(name = "senha", length = 200)
	private String senha;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "id_portador")
	private List<Cartao> cartoes;
	
	
 	synchronized public void addCartao(final Cartao cartao) {
		if (this.cartoes == null) {
			this.cartoes = new ArrayList<>();
		}
		this.cartoes.add(cartao);
	}

	
	synchronized public void removeCartao(final Cartao cartao) {
		if (this.cartoes != null) {
			this.cartoes.remove(cartao);
		}
	}

	public List<Cartao> getCartoes() {
		List<Cartao> result = Collections.emptyList();
		if (this.cartoes != null) {
			result = this.cartoes;
		}

		return Collections.unmodifiableList(result);
	}
}
