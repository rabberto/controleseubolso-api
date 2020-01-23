package com.rbbsolucoes.controleseubolso.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import com.rbbsolucoes.controleseubolso.exception.RegraNegocioException;
import com.rbbsolucoes.controleseubolso.model.entity.Lancamento;
import com.rbbsolucoes.controleseubolso.model.enums.StatusLancamento;
import com.rbbsolucoes.controleseubolso.model.repository.LancamentoRepository;
import com.rbbsolucoes.controleseubolso.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService{

	
	private LancamentoRepository repository;
	
	public LancamentoServiceImpl(LancamentoRepository repository) {
		this.repository = repository;	
	}
	
	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PEDENTE);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validar(lancamento);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);
		
	}

	@Override
	@Transactional
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		Example example = Example.of(lancamentoFiltro, 
				ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		return repository.findAll(example);
	}

	@Override
	public void atualizaStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);
		
	}

	@Override
	public void validar(Lancamento lancamento) {
		
		if (lancamento.getDecricao() == null || lancamento.getDecricao().trim().equals("")){
			throw new RegraNegocioException("Informe uma Descriçao válida.");
		}
		
		if(lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Informe um Mês válido.");
		}
		
		if(lancamento.getAno() == null || lancamento.getAno().toString().trim().length() != 4) {
			throw new RegraNegocioException("Informe um Ano válido.");
		}
		
		if(lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
			throw new RegraNegocioException("Informe um Usuário.");
		}
		
		if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Informe um Valr válido.");
		}
		
		if(lancamento.getTipo() == null) {
			throw new RegraNegocioException("Informe um Tipo de Lançamento válido.");
		}
	}

}
