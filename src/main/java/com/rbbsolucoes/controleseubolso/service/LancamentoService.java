package com.rbbsolucoes.controleseubolso.service;

import com.rbbsolucoes.controleseubolso.model.entity.Lancamento;
import com.rbbsolucoes.controleseubolso.model.enums.StatusLancamento;

import java.util.List;

public interface LancamentoService {
	
	Lancamento salvar(Lancamento lancamento);
	
	Lancamento atualizar(Lancamento lancamento);
	
	void deletar(Lancamento lancamento);
	
	List<Lancamento> buscar( Lancamento lancamentoFiltro );
	
	void atualizaStatus(Lancamento lancamento, StatusLancamento status);	
	
	void validar(Lancamento lancamento);
}

