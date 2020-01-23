package com.rbbsolucoes.controleseubolso.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rbbsolucoes.controleseubolso.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	
}
