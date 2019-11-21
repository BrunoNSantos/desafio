package com.brunonsantos.cartao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brunonsantos.cartao.model.Portador;

public interface PortadorRepository extends JpaRepository<Portador, Long>{

	Portador findByCpf(String cpf);

}
