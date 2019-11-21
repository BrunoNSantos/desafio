package com.brunonsantos.proposta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brunonsantos.proposta.model.Proposta;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

}
