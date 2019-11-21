package com.brunonsantos.cartao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brunonsantos.cartao.model.Cartao;

public interface CartaoRepository extends JpaRepository<Cartao, Long>{

}
